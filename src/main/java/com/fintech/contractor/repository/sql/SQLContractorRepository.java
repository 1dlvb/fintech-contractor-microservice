package com.fintech.contractor.repository.sql;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.util.WildcatEnhancer;
import com.onedlvb.jwtlib.util.Roles;
import com.onedlvb.jwtlib.util.SecurityUtil;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Repository class for executing SQL queries to retrieve contractors based on various filters.
 * @author Matushkin Anton
 */
@RequiredArgsConstructor
public class SQLContractorRepository {

    @NonNull
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final String INITIAL_SQL =  "SELECT c.*, co.id as country_id, of.name as org_form_name, i.name as industry_name " +
            "FROM contractor c " +
            "JOIN country co ON c.country = co.id " +
            "JOIN org_form of ON c.org_form = of.id " +
            "JOIN industry i ON c.industry = i.id " +
            "WHERE c.is_active = true ";

    /**
     * Retrieves contractors from the database based on the provided search payload and pagination parameters.
     * @param payload The search payload containing filters to apply.
     * @param page    The page number to retrieve.
     * @param size    The number of results per page.
     * @return A list of {@link ContractorDTO} objects that match the search criteria.
     */
    public List<ContractorDTO> findContractorByFilters(SearchContractorPayload payload, Integer page, Integer size) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        Integer offset = page * size;

        StringBuilder sqlBuilder = new StringBuilder(INITIAL_SQL);
        List<ContractorDTO> listOfRoleBasedResults = roleBasedSqlBuilderUpdate(payload, sqlBuilder, params);
        if (listOfRoleBasedResults != null) {
            return listOfRoleBasedResults;
        }

        if (payload.isEmptyExceptCountry() && SecurityUtil.hasRole(Roles.CONTRACTOR_RUS)) {
            if (payload.getCountry().equals("RUS")) {
                sqlBuilder.append("AND co.id = 'RUS'");
                params.addValue("co.id", "RUS");
                return namedParameterJdbcTemplate.query(sqlBuilder.toString(), params, rowMapper());
            } else {
                return Collections.emptyList();
            }
        }

        List<BiConsumer<SearchContractorPayload, MapSqlParameterSource>> filters = List.of(
                (p, ps) -> addEqualCondition(p.getId(), sqlBuilder, "c.id", ps),
                (p, ps) -> addEqualCondition(p.getParentId(), sqlBuilder, "c.parent_id", ps),
                (p, ps) -> addLikeCondition(p.getName(), sqlBuilder, "c.name", ps),
                (p, ps) -> addLikeCondition(p.getNameFull(), sqlBuilder, "c.name_full", ps),
                (p, ps) -> addLikeCondition(p.getInn(), sqlBuilder, "c.inn", ps),
                (p, ps) -> addLikeCondition(p.getOgrn(), sqlBuilder, "c.ogrn", ps),
                (p, ps) -> addLikeCondition(p.getCountry(), sqlBuilder, "co.id", ps),
                (p, ps) -> addLikeCondition(p.getOrgForm(), sqlBuilder, "of.name", ps),
                (p, ps) -> addIndustryCondition(p.getIndustry(), sqlBuilder, ps)
        );

        for (BiConsumer<SearchContractorPayload, MapSqlParameterSource> filter : filters) {
            filter.accept(payload, params);
        }

        sqlBuilder.append(" LIMIT :size OFFSET :offset");
        params.addValue("size", size);
        params.addValue("offset", offset);

        return namedParameterJdbcTemplate.query(sqlBuilder.toString(), params, rowMapper());
    }

    /**
     * Updates the SQL query based on the user's role and search criteria.
     * <p>If the user has the CONTRACTOR_RUS role and the payload is empty, appends a filter for
     * a specific ID ('RUS') to the query and executes it.</p>
     *
     * <p>If the user has the CONTRACTOR_RUS role but the payload is not empty, returns an empty list.</p>
     *
     * <p>If the user does not have the CONTRACTOR_RUS role, returns null.</p>
     *
     * @param payload The search criteria used to build the query.
     * @param sqlBuilder The {@code StringBuilder} for constructing the query.
     * @param params The {@code MapSqlParameterSource} for query parameters.
     * @return A list of {@link ContractorDTO} if the query is executed; otherwise, an empty list or {@code null}.
     */
    @Nullable
    private List<ContractorDTO> roleBasedSqlBuilderUpdate(SearchContractorPayload payload, StringBuilder sqlBuilder, MapSqlParameterSource params) {
        if (payload.isEmpty() && SecurityUtil.hasRole(Roles.CONTRACTOR_RUS)) {
            sqlBuilder.append("AND co.id = 'RUS'");
            params.addValue("co.id", "RUS");
            return namedParameterJdbcTemplate.query(sqlBuilder.toString(), params, rowMapper());
        } else if (!payload.isEmpty() && SecurityUtil.hasRole(Roles.CONTRACTOR_RUS)) {
            return Collections.emptyList();
        }
        return null;
    }

    /**
     * Util method adds an equal condition to the SQL query if the value is not null.
     * @param value   The value to match.
     * @param sb      The {@link StringBuilder} representing the SQL query.
     * @param column  The column name in the database.
     * @param params  The list of parameters to bind to the SQL query.
     */
    private <T> void addEqualCondition(T value, StringBuilder sb, String column, MapSqlParameterSource params) {
        if (value != null) {
            sb.append(" AND ").append(column).append(" = :").append(column.replace(".", "_"));
            params.addValue(column.replace(".", "_"), value);
        }
    }

    /**
     * Util method adds a LIKE condition to the SQL query if the value is not null.
     * Uses {@link WildcatEnhancer} to enhance the value with wildcards for pattern matching.
     * @param value   The value to match with wildcards.
     * @param sb      The {@link StringBuilder} representing the SQL query.
     * @param column  The column name in the database.
     * @param params  The list of parameters to bind to the SQL query.
     */
    private void addLikeCondition(String value, StringBuilder sb, String column, MapSqlParameterSource params) {
        if (value != null) {
            String paramName = column.replace(".", "_") + "_like";
            sb.append(" AND ").append(column).append(" LIKE :").append(paramName);
            params.addValue(paramName, WildcatEnhancer.enhanceWithWildcatMatching(value));
        }
    }

    /**
     * AUtil method adds conditions to filter by {@link IndustryDTO}
     * if the industry information is provided in the payload.
     * @param industry The {@link IndustryDTO} containing industry filters.
     * @param sb       The {@link StringBuilder} representing the SQL query.
     * @param params   The list of parameters to bind to the SQL query.
     */
    private void addIndustryCondition(IndustryDTO industry, StringBuilder sb, MapSqlParameterSource params) {
        if (industry != null) {
            if (industry.getId() != null) {
                String idParamName = "industry_id";
                sb.append(" AND i.id = :").append(idParamName);
                params.addValue(idParamName, industry.getId());
            }
            if (industry.getName() != null) {
                String nameParamName = "industry_name";
                sb.append(" AND i.name = :").append(nameParamName);
                params.addValue(nameParamName, industry.getName());
            }
        }
    }

    /**
     * Method for mapping SQL result set rows to {@link ContractorDTO} objects.
     * @return A {@link RowMapper} instance for {@link ContractorDTO}.
     */
    private RowMapper<ContractorDTO> rowMapper() {
        return (rs, rowNum) -> {
            CountryDTO countryDTO = new CountryDTO(rs.getString("country"), rs.getString("country_id"));
            OrgFormDTO orgFormDTO = new OrgFormDTO(rs.getLong("org_form"), rs.getString("org_form_name"));
            IndustryDTO industryDTO = new IndustryDTO(rs.getLong("industry"), rs.getString("industry_name"));

            return new ContractorDTO(
                    rs.getString("id"),
                    rs.getString("parent_id"),
                    rs.getString("name"),
                    rs.getString("name_full"),
                    rs.getString("inn"),
                    rs.getString("ogrn"),
                    countryDTO,
                    industryDTO,
                    orgFormDTO);
        };
    }

}
