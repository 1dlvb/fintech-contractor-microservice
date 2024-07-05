package com.fintech.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fintech.contractor.model.Contractor;
import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "parent_id", "name", "nameFull", "inn", "ogrn", "country", "industry", "orgForm" })
public class ContractorDTO {

    private String id;

    @JsonProperty("parent_id")
    private Contractor parent;
    private String name;

    @JsonProperty("name_full")
    private String nameFull;
    private String inn;
    private String ogrn;
    private CountryDTO country;
    private IndustryDTO industry;
    private OrgFormDTO orgForm;

}
