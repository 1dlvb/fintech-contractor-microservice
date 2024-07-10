package com.fintech.contractor.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fintech.contractor.dto.IndustryDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * A record representing a payload for searching contractors.
 * @param id The ID of the contractor.
 * @param parentId The parent ID of the contractor.
 * @param name The name of the contractor.
 * @param nameFull The full name of the contractor.
 * @param inn The individual tax number (INN) of the contractor.
 * @param ogrn The primary state registration number (OGRN) of the contractor.
 * @param country The country name of the contractor.
 * @param industry The industry details of the contractor.
 * @param orgForm The organizational form name of the contractor.
 * @author Matushkin Anton
 */
@JsonPropertyOrder({ "id", "parent_id", "name", "name_full", "inn", "ogrn", "country", "industry", "org_form" })
public record SearchContractorPayload(
        @Schema(description = "The ID of the contractor.", example = "fa23a2e5-1ef3-48cd-a970-127bc0ea2a47")
        String id,

        @JsonProperty("parent_id")
        @Schema(description = "The parent ID of the contractor.", example = "feb362a1-5c42-4302-b3a7-5f9db7ee7988")
        String parentId,

        @Schema(description = "The name of the contractor.", example = "sample name")
        String name,

        @JsonProperty("name_full")
        @Schema(description = "The full name of the contractor.", example = "sample full name")
        String nameFull,

        @Schema(description = "The individual tax number (INN) of the contractor.", example = "123456789")
        String inn,

        @Schema(description = "The primary state registration number (OGRN) of the contractor.", example = "987654321")
        String ogrn,

        @Schema(description = "The country name of the contractor.", example = "Абхазия")
        String country,

        @Schema(description = "The industry details of the contractor.")
        IndustryDTO industry,

        @JsonProperty("org_form")
        @Schema(description = "The organizational form name of the contractor.",
                example = "Автономная некоммерческая организация")
        String orgForm) {
}
