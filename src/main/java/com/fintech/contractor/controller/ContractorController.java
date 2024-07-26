package com.fintech.contractor.controller;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.dto.ContractorWithMainBorrowerDTO;
import com.fintech.contractor.dto.MainBorrowerDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.service.ContractorService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLogHttp;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing contractors.
 * Provides endpoints for CRUD operations and searching for contractors.
 *
 */
@RestController
@RequestMapping("/contractor")
@RequiredArgsConstructor
@Tag(name = "Contractor API", description = "API for managing contractors")
public class ContractorController {

    @NonNull
    private final ContractorService contractorService;

    /**
     * Saves or updates a contractor.
     * @param contractorDTO the contractor details to save or update.
     * @return a {@link ResponseEntity} containing the saved contractor details.
     */
    @Operation(summary = "Save or update contractor", description = "Saves or updates contractor details.")
    @ApiResponse(responseCode = "200",
            description = "Contractor saved",
            content = @Content(schema = @Schema(implementation = ContractorDTO.class)))
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<ContractorDTO> saveOrUpdateContractor(
            @RequestBody @Parameter(description = "Contractor details") ContractorDTO contractorDTO) {
        ContractorDTO savedContractorDTO = contractorService.saveOrUpdateContractor(contractorDTO);
        return ResponseEntity.ok(savedContractorDTO);
    }

    /**
     * Retrieves a contractor by ID.
     * @param id the ID of the contractor.
     * @return a {@link ResponseEntity} containing the contractor details if found, otherwise a 404 response.
     */
    @Operation(summary = "Find contractor by ID", description = "Retrieves contractor details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Contractor found",
                    content = @Content(schema = @Schema(implementation = ContractorDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Contractor not found")
    })
    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<ContractorDTO> findContractorById(
            @PathVariable @Parameter(description = "ID of the contractor") String id) {
        try {
            ContractorDTO contractorDTO = contractorService.findContractorById(id);
            return ResponseEntity.ok(contractorDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a contractor by ID.
     * @param id the ID of the contractor to delete.
     * @return a {@link ResponseEntity} with no content if the contractor was successfully deleted, otherwise a 404 response.
     */
    @Operation(summary = "Delete contractor by ID", description = "Deletes contractor by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Contractor deleted"),
            @ApiResponse(responseCode = "404", description = "Contractor not found")
    })
    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteContractor(
            @PathVariable @Parameter(description = "ID of the contractor to delete") String id) {
        try {
            contractorService.deleteContractor(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Searches for contractors based on the provided search payload and pagination parameters.
     * @param payload the search payload containing criteria for searching contractors.
     * @param page the page number to retrieve, default is 0.
     * @param size the page size, default is 10.
     * @return a {@link ResponseEntity} containing a list of contractors that match the search criteria.
     */
    @Operation(summary = "Search contractors", description = "Searches for contractors based on the search criteria.")
    @ApiResponse(responseCode = "200",
            description = "Contractors found",
            content = @Content(schema = @Schema(implementation = ContractorDTO.class)))
    @PostMapping("/search")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<ContractorDTO>> getContractors(
            @Parameter(description = "Search criteria") SearchContractorPayload payload,
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "Page number to retrieve")
            Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Parameter(description = "Number of contractors per page")
            Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contractorService.findContractors(payload, pageable));
    }

    /**
     * Searches for contractors using a custom SQL query based on the provided search payload and pagination parameters.
     * @param payload the search payload containing criteria for searching contractors.
     * @param page the page number to retrieve, default is 0.
     * @param size the page size, default is 10.
     * @return a {@link ResponseEntity} containing a list of contractors that match the search criteria.
     */
    @Operation(summary = "Search contractors with custom SQL",
            description = "Searches for contractors using a custom SQL query.")
    @ApiResponse(responseCode = "200",
            description = "Contractors found",
            content = @Content(schema = @Schema(implementation = ContractorDTO.class)))
    @PostMapping("/search/sql")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<ContractorDTO>> getContractorsSQL(
            @Parameter(description = "Search criteria") SearchContractorPayload payload,
            @RequestParam(name = "page", defaultValue = "0") @Parameter(description = "Page number to retrieve")
            Integer page,
            @RequestParam(name = "size", defaultValue = "10") @Parameter(description = "Number of contractors per page")
            Integer size) {
        return ResponseEntity.ok(contractorService.findContractorsSQL(payload, page, size));
    }

    /**
     * Updates the main borrower information for a contractor.
     * @param dto the DTO containing the main borrower information.
     * @return the updated contractor with main borrower information.
     */
    @AuditLogHttp(logLevel = LogLevel.INFO)
    @PatchMapping("/main-borrower")
    @Operation(summary = "Update Main Borrower", description = "Updates the main borrower for a contractor.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the main borrower information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ContractorWithMainBorrowerDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Contractor not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ContractorWithMainBorrowerDTO> updateMainBorrower(@RequestBody MainBorrowerDTO dto) {
        return ResponseEntity.ok(contractorService.updateMainBorrower(dto));
    }

}
