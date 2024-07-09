package com.fintech.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a contractor.
 * Contains basic information about a contractor entity.
 * @author Matushkin Anton
 * @see com.fintech.contractor.model.Contractor
 */
@Data
@JsonPropertyOrder({ "id", "parent_id", "name", "name_full", "inn", "ogrn", "country", "industry", "org_form" })
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContractorDTO {

    @JsonProperty("id")
    @Schema(example = "fa23a2e5-1ef3-48cd-a970-127bc0ea2a47")
    private String id;

    @JsonProperty("parent_id")
    @Schema(example = "feb362a1-5c42-4302-b3a7-5f9db7ee7988")
    private String parent;

    @JsonProperty("name")
    @Schema(example = "sample name")
    private String name;

    @JsonProperty("name_full")
    @Schema(example = "sample full name")
    private String nameFull;

    @JsonProperty("inn")
    @Schema(example = "123456789")
    private String inn;

    @JsonProperty("ogrn")
    @Schema(example = "987654321")
    private String ogrn;

    @JsonProperty("country")
    @Schema(example = "ABH")
    private CountryDTO country;

    @JsonProperty("industry")
    @Schema(example = "2")
    private IndustryDTO industry;

    @JsonProperty("org_form")
    @Schema(example = "2")
    private OrgFormDTO orgForm;

}
