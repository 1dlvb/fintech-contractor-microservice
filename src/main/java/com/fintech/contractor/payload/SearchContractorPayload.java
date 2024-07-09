package com.fintech.contractor.payload;

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
public record SearchContractorPayload(
        @Schema(description = "The ID of the contractor.")
        String id,

        @Schema(description = "The parent ID of the contractor.")
        String parentId,

        @Schema(description = "The name of the contractor.")
        String name,

        @Schema(description = "The full name of the contractor.")
        String nameFull,

        @Schema(description = "The individual tax number (INN) of the contractor.")
        String inn,

        @Schema(description = "The primary state registration number (OGRN) of the contractor.")
        String ogrn,

        @Schema(description = "The country name of the contractor.")
        String country,

        @Schema(description = "The industry details of the contractor.")
        IndustryDTO industry,

        @Schema(description = "The organizational form name of the contractor.")
        String orgForm) {
}
