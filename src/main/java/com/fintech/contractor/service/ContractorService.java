package com.fintech.contractor.service;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.payload.SearchContractorPayload;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing contractors.
 * Defines methods for CRUD operations and searching contractors.
 * @author Matushkin Anton
 */
public interface ContractorService {

    /**
     * Retrieves a list of contractors based on the provided search payload and pagination parameters.
     * @param payload  the search payload containing criteria for searching contractors.
     * @param pageable pagination information for controlling the result set size and page number.
     * @return a list of {@link ContractorDTO} objects that match the search criteria.
     */
    List<ContractorDTO> findContractors(SearchContractorPayload payload, Pageable pageable);

    /**
     * Retrieves a list of contractors using a custom SQL query based on the provided search payload and pagination parameters.
     * @param payload the search payload containing criteria for searching contractors.
     * @param page    the page number to retrieve, starting from 0.
     * @param size    the page size, indicating the maximum number of contractors per page.
     * @return a list of {@link ContractorDTO} objects that match the search criteria.
     */
    List<ContractorDTO> findContractorsSQL(SearchContractorPayload payload, Integer page, Integer size);

    /**
     * Saves or updates a contractor.
     * @param contractorDTO the contractor details to save or update.
     * @return the saved or updated {@link ContractorDTO} object.
     */
    ContractorDTO saveOrUpdateContractor(ContractorDTO contractorDTO);

    /**
     * Retrieves a contractor by ID.
     * @param id the ID of the contractor to retrieve.
     * @return the {@link ContractorDTO} object with the specified ID.
     * @throws NotActiveException if the contractor is not active.
     */
    ContractorDTO findContractorById(String id) throws NotActiveException;

    /**
     * Deletes a contractor by ID.
     * @param id the ID of the contractor to delete.
     * @throws NotActiveException if the contractor is not active.
     */
    void deleteContractor(String id) throws NotActiveException;

}
