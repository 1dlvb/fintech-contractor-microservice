package com.fintech.contractor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing a country.
 * Contains basic information about a country entity.
 * @author Matushkin Anton
 * @see com.fintech.contractor.model.Country
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryDTO {

    @Schema(description = "The ID of the country.", example = "ABH")
    private String id;
    @Schema(description = "The name of the country.", example = "Абхазия")
    private String name;

}
