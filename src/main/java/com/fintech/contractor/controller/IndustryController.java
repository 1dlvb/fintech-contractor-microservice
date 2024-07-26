package com.fintech.contractor.controller;

import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.IndustryService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLogHttp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing industries.
 * @author Matushkin Anton
 */
@RestController
@RequestMapping("contractor/industry")
@RequiredArgsConstructor
@Tag(name = "Industry API", description = "API for managing industries")
public class IndustryController {

    @NonNull
    private final IndustryService industryService;

    /**
     * Saves or updates an industry.
     * @param industryDTO ({@link IndustryDTO}) the industry details to save or update.
     * @return a {@link ResponseEntity} containing the saved industry details.
     */
    @Operation(summary = "Save or update industry", description = "Saves or updates industry details.")
    @ApiResponse(responseCode = "200",
            description = "industry saved",
            content = @Content(schema = @Schema(implementation = IndustryDTO.class)))
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<IndustryDTO> saveOrUpdateIndustry(
            @RequestBody @Parameter(description = "Industry details") IndustryDTO industryDTO) {
        IndustryDTO savedIndustryDTO = industryService.saveOrUpdateIndustry(industryDTO);
        return ResponseEntity.ok(savedIndustryDTO);
    }

    /**
     * @return a {@link ResponseEntity} containing the list of all industries.
     * @see IndustryDTO
     */
    @Operation(summary = "Find all industries", description = "Retrieves all industries.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Industries found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = IndustryDTO.class)))),
    })
    @GetMapping("/all")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<IndustryDTO>> fetchAllCountries() {
        return ResponseEntity.ok(industryService.fetchAllIndustries());
    }

    /**
     * Retrieves an industry by ID.
     * @param id the ID of the industry.
     * @return a {@link ResponseEntity} containing the industry details if found, otherwise a 404 response.
     * @see IndustryDTO
     */
    @Operation(summary = "Find industry by ID", description = "Retrieves industry details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "industry found",
                    content = @Content(schema = @Schema(implementation = IndustryDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "industry not found")
    })
    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<IndustryDTO> findIndustryById(
            @PathVariable @Parameter(description = "ID of the industry") Long id) {
        try {
            IndustryDTO industryDTO = industryService.findIndustryById(id);
            return ResponseEntity.ok(industryDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an industry by ID.
     * @param id the ID of the industry to delete.
     * @return a {@link ResponseEntity} with no content if the industry was successfully deleted, otherwise a 404 response.
     * @see IndustryDTO
     */
    @Operation(summary = "Delete industry by ID", description = "Deletes industry by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "industry found",
                    content = @Content(schema = @Schema(implementation = IndustryDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "industry not found")
    })
    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteIndustry(
            @PathVariable @Parameter(description = "ID of the industry") Long id) {
        try {
            industryService.deleteIndustry(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
