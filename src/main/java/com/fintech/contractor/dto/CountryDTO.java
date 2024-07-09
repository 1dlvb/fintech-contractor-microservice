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

    @Schema(example = "ABH")
    private String id;
    @Schema(example = "Абхазия")
    private String name;

}
