package com.fintech.contractor.controller;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.service.ContractorService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLogHttp;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for managing contractors.
 * Provides endpoints for CRUD operations and searching for contractors.
 * @author Matushkin Anton
 */
@RestController
@RequestMapping("/contractor")
@RequiredArgsConstructor
public class ContractorController {

    @NonNull
    private final ContractorService contractorService;

    /**
     * Saves or updates a contractor.
     * @param contractorDTO ({@link ContractorDTO}) the contractor details to save or update.
     * @return a {@link ResponseEntity} containing the saved contractor details.
     */
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<ContractorDTO> saveOrUpdateContractor(@RequestBody ContractorDTO contractorDTO) {
        ContractorDTO savedContractorDTO = contractorService.saveOrUpdateContractor(contractorDTO);
        return ResponseEntity.ok(savedContractorDTO);
    }

    /**
     * Retrieves a contractor by ID.
     * @param id the ID of the contractor.
     * @return a {@link ResponseEntity} containing the contractor details if found, otherwise a 404 response.
     * @see ContractorDTO
     */
    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<ContractorDTO> findContractorById(@PathVariable String id) {
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
     * @see ContractorDTO
     */
    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteContractor(@PathVariable String id) {
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
     * @see ContractorDTO
     */
    @GetMapping("/search")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<ContractorDTO>> getContractors(
            SearchContractorPayload payload,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(contractorService.findContractors(payload, pageable));
    }

    /**
     * Searches for contractors using a custom SQL query based on the provided search payload and pagination parameters.
     * @param payload the search payload containing criteria for searching contractors.
     * @param page the page number to retrieve, default is 0.
     * @param size the page size, default is 10.
     * @return a {@link ResponseEntity} containing a list of contractors that match the search criteria.
     * @see ContractorDTO
     */
    @GetMapping("/search/sql")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<ContractorDTO>> getContractorsSQL(
            SearchContractorPayload payload,
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(contractorService.findContractorsSQL(payload, page, size));
    }

}
