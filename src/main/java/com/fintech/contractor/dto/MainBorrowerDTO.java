package com.fintech.contractor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing the main borrower information.
 * @author Matushkin Anton
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO for main borrower information")
public class MainBorrowerDTO {

    @JsonProperty("contractor_id")
    @Schema(description = "The unique identifier of the contractor",
            example = "123e4567-e89b-12d3-a456-426614174000")
    private String contractorId;

    @JsonProperty("has_main_deals")
    @Schema(description = "Indicates whether the contractor has main deals", example = "true")
    private boolean hasMainDeals;

}
