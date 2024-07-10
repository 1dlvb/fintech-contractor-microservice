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
    @Schema(description = "The ID of the contractor.",
            example = "fa23a2e5-1ef3-48cd-a970-127bc0ea2a47")
    private String id;

    @JsonProperty("parent_id")
    @Schema(description = "The parent ID of the contractor. The parent is also a contractor.",
            example = "feb362a1-5c42-4302-b3a7-5f9db7ee7988")
    private String parent;

    @JsonProperty("name")
    @Schema(description = "The name of the contractor.",
            example = "sample name")
    private String name;

    @JsonProperty(defaultValue = "name_full")
    @Schema(description = "The full name of the contractor.", example = "sample full name")
    private String nameFull;

    @JsonProperty("inn")
    @Schema(description = "The individual tax number (INN) of the contractor.", example = "123456789")
    private String inn;

    @JsonProperty("ogrn")
    @Schema(description = "The primary state registration number (OGRN) of the contractor.", example = "987654321")
    private String ogrn;

    @JsonProperty("country")
    @Schema(description = "The country ID of the contractor.", example = "ABH")
    private CountryDTO country;

    @JsonProperty("industry")
    @Schema(description = "The industry ID of the contractor.", example = "2")
    private IndustryDTO industry;

    @JsonProperty("org_form")
    @Schema(description = "The organizational form ID of the contractor.", example = "2")
    private OrgFormDTO orgForm;

    @JsonProperty("name_full")
    public String getNameFull() {
        return nameFull;
    }

    @JsonProperty("name_full")
    public void setNameFull(String nameFull) {
        this.nameFull = nameFull;
    }

}
