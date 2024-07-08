package com.fintech.contractor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing an industry.
 * Contains basic information about an industry entity.
 * @author Matushkin Anton
 * @see com.fintech.contractor.model.Industry
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IndustryDTO {

    private Long id;
    private String name;

}
