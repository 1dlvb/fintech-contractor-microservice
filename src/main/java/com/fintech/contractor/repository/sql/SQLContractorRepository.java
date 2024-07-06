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

@RequiredArgsConstructor
public class SQLContractorRepository {

    @NonNull
    private final JdbcTemplate jdbcTemplate;

    public List<ContractorDTO> findContractorByFilters(SearchContractorPayload payload, Integer page, Integer size) {
        List<Object> params = new ArrayList<>();
        Integer offset = page * size;

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT c.*, co.name as country_name, of.name as org_form_name, i.name as industry_name FROM contractor c " +
                "JOIN country co ON c.country = co.id " +
                "JOIN org_form of ON c.org_form = of.id " +
                "JOIN industry i ON c.industry = i.id WHERE 1=1"
        );
        if (payload.id() != null) {
            sqlBuilder.append(" AND id = ?");
            params.add(payload.id());
        }
        if (payload.parentId() != null) {
            sqlBuilder.append(" AND parent_id = ?");
            params.add(payload.parentId());
        }
        if (payload.name() != null) {
            sqlBuilder.append(" AND name LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(payload.name()));
        }
        if (payload.nameFull() != null) {
            sqlBuilder.append(" AND name_full LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(payload.nameFull()));
        }
        if (payload.inn() != null) {
            sqlBuilder.append(" AND inn LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(payload.inn()));
        }
        if (payload.ogrn() != null) {
            sqlBuilder.append(" AND ogrn LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(payload.ogrn()));
        }

        if (payload.country() != null) {
            sqlBuilder.append(" AND co.name LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(payload.country()));
        }
        if (payload.orgForm() != null) {
            sqlBuilder.append(" AND of.name LIKE ?");
            params.add(WildcatEnhancer.enhanceWithWildcatMatching(payload.orgForm()));
        }
        if (payload.industry() != null) {
            IndustryDTO industry = payload.industry();
            if (industry.getId() != null) {
                sqlBuilder.append(" AND i.id = ?");
                params.add(industry.getId());
            }
            if (industry.getName() != null) {
                sqlBuilder.append(" AND i.name = ?");
                params.add(industry.getName());
            }
        }

        sqlBuilder.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add(offset);

        String sql = sqlBuilder.toString();
        return jdbcTemplate.query(sql, rowMapper(), params.toArray());
    }

    private RowMapper<ContractorDTO> rowMapper() {
        return (rs, rowNum) -> {
            ContractorDTO contractorDTO = new ContractorDTO();
            contractorDTO.setId(rs.getString("id"));
            contractorDTO.setParentId(rs.getString("parent_id"));
            contractorDTO.setName(rs.getString("name"));
            contractorDTO.setNameFull(rs.getString("name_full"));
            contractorDTO.setInn(rs.getString("inn"));
            contractorDTO.setOgrn(rs.getString("ogrn"));

            CountryDTO countryDTO = new CountryDTO();
            countryDTO.setId(rs.getString("country"));
            countryDTO.setName(rs.getString("country_name"));
            contractorDTO.setCountry(countryDTO);

            OrgFormDTO orgFormDTO = new OrgFormDTO();
            orgFormDTO.setId(rs.getLong("org_form"));
            orgFormDTO.setName(rs.getString("org_form_name"));
            contractorDTO.setOrgForm(orgFormDTO);

            IndustryDTO industryDTO = new IndustryDTO();
            industryDTO.setId(rs.getLong("industry"));
            industryDTO.setName(rs.getString("industry_name"));
            contractorDTO.setIndustry(industryDTO);

            return contractorDTO;
        };
    }

}
