package com.fintech.contractor.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for representing an org form.
 * Contains basic information about an org form entity.
 * @author Matushkin Anton
 * @see com.fintech.contractor.model.OrgForm
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrgFormDTO {

    @Schema(example = "2")
    private Long id;
    @Schema(example = "Автономная некоммерческая организация")
    private String name;

}
