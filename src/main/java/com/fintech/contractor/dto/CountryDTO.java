package com.fintech.contractor.dto;

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

    private String id;
    private String name;

}
