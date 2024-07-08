package com.fintech.contractor.service;

import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.exception.NotActiveException;

import java.util.List;

/**
 * Service interface for managing industries.
 * Defines methods for CRUD operations on countries.
 * @author Matushkin Anton
 */
public interface IndustryService {

    /**
     * Retrieves all industries.
     * @return a list of {@link IndustryDTO} objects representing all industries.
     */
    List<IndustryDTO> fetchAllIndustries();

    /**
     * Saves or updates an industry.
     * @param industryDTO the industry details to save or update.
     * @return the saved or updated {@link IndustryDTO} object.
     */
    IndustryDTO saveOrUpdateIndustry(IndustryDTO industryDTO);

    /**
     * Retrieves an industry by its ID.
     * @param id the ID of the industry to retrieve.
     * @return the {@link IndustryDTO} object with the specified ID.
     * @throws NotActiveException if the industry is not active.
     */
    IndustryDTO findIndustryById(Long id) throws NotActiveException;

    /**
     * Deletes an industry by its ID.
     * @param id the ID of the industry to delete.
     * @throws NotActiveException if the industry is not active.
     */
    void deleteIndustry(Long id) throws NotActiveException;

}
