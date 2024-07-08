package com.fintech.contractor.repository.sql;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.util.WildcatEnhancer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Repository class for executing SQL queries to retrieve contractors based on various filters.
 * @author Matushkin Anton
 */
@RequiredArgsConstructor
public class SQLContractorRepository {

    @NonNull
    private final JdbcTemplate jdbcTemplate;
    private static final String INITIAL_SQL =  "SELECT c.*, co.name as country_name, of.name as org_form_name, i.name as industry_name " +
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
        List<Object> params = new ArrayList<>();
        Integer offset = page * size;

        StringBuilder sqlBuilder = new StringBuilder(INITIAL_SQL);

        List<BiConsumer<SearchContractorPayload, List<Object>>> filters = List.of(
                (p, ps) -> addEqualCondition(p.id(), sqlBuilder, "c.id", ps),
                (p, ps) -> addEqualCondition(p.parentId(), sqlBuilder, "c.parent_id", ps),
                (p, ps) -> addLikeCondition(p.name(), sqlBuilder, "c.name", ps),
                (p, ps) -> addLikeCondition(p.nameFull(), sqlBuilder, "c.name_full", ps),
                (p, ps) -> addLikeCondition(p.inn(), sqlBuilder, "c.inn", ps),
                (p, ps) -> addLikeCondition(p.ogrn(), sqlBuilder, "c.ogrn", ps),
                (p, ps) -> addLikeCondition(p.country(), sqlBuilder, "co.name", ps),
                (p, ps) -> addLikeCondition(p.orgForm(), sqlBuilder, "of.name", ps),
                (p, ps) -> addIndustryCondition(p.industry(), sqlBuilder, ps)
        );

        for (BiConsumer<SearchContractorPayload, List<Object>> filter : filters) {
            filter.accept(payload, params);
        }

        sqlBuilder.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        String sql = sqlBuilder.toString();
        return jdbcTemplate.query(sql, rowMapper(), params.toArray());
    }

    /**
     * Util method adds an equal condition to the SQL query if the value is not null.
     * @param value   The value to match.
     * @param sb      The {@link StringBuilder} representing the SQL query.
     * @param column  The column name in the database.
     * @param params  The list of parameters to bind to the SQL query.
     */
    private <T> void addEqualCondition(T value, StringBuilder sb, String column, List<Object> params) {
        if (value != null) {
            sb.append(" AND ").append(column).append(" = ?");
            params.add(value);
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
    private void addLikeCondition(String value, StringBuilder sb, String column, List<Object> params) {
        if (value != null) {
            sb.append(" AND ").append(column).append(" LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(value));
        }
    }

    /**
     * AUtil method adds conditions to filter by {@link IndustryDTO}
     * if the industry information is provided in the payload.
     * @param industry The {@link IndustryDTO} containing industry filters.
     * @param sb       The {@link StringBuilder} representing the SQL query.
     * @param params   The list of parameters to bind to the SQL query.
     */
    private void addIndustryCondition(IndustryDTO industry, StringBuilder sb, List<Object> params) {
        if (industry != null) {
            if (industry.getId() != null) {
                sb.append(" AND i.id = ?");
                params.add(industry.getId());
            }
            if (industry.getName() != null) {
                sb.append(" AND i.name = ?");
                params.add(industry.getName());
            }
        }
    }

    /**
     * Method for mapping SQL result set rows to {@link ContractorDTO} objects.
     * @return A {@link RowMapper} instance for {@link ContractorDTO}.
     */
    private RowMapper<ContractorDTO> rowMapper() {
        return (rs, rowNum) -> {
            CountryDTO countryDTO = new CountryDTO(rs.getString("country"), rs.getString("country_name"));
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
