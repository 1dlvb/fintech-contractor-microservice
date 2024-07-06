package com.fintech.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "parent_id", "name", "name_full", "inn", "ogrn", "country", "industry", "org_form" })
public class ContractorDTO {

    @JsonProperty("id")
    private String id;

    @JsonProperty("parent_id")
    private String parentId;

    @JsonProperty("name")
    private String name;

    @JsonProperty("name_full")
    private String nameFull;

    @JsonProperty("inn")
    private String inn;

    @JsonProperty("ogrn")
    private String ogrn;

    @JsonProperty("country")
    private CountryDTO country;

    @JsonProperty("industry")
    private IndustryDTO industry;

    @JsonProperty("org_form")
    private OrgFormDTO orgForm;

}
