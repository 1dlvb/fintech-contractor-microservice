package com.fintech.contractor.service;

import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;

import java.util.List;

/**
 * Service interface for managing countries.
 * Defines methods for CRUD operations on countries.
 * @author Matushkin Anton
 */
public interface CountryService {

    /**
     * Retrieves all countries.
     * @return a list of {@link CountryDTO} objects representing all countries.
     */
    List<CountryDTO> fetchAllCountries();

    /**
     * Saves or updates a country.
     * @param countryDTO the country details to save or update.
     * @return the saved or updated {@link CountryDTO} object.
     */
    CountryDTO saveOrUpdateCountry(CountryDTO countryDTO);

    /**
     * Retrieves a country by its ID.
     * @param id the ID of the country to retrieve.
     * @return the {@link CountryDTO} object with the specified ID.
     * @throws NotActiveException if the country is not active.
     */
    CountryDTO findCountryById(String id) throws NotActiveException;

    /**
     * Deletes a country by its ID.
     * @param id the ID of the country to delete.
     * @throws NotActiveException if the country is not active.
     */
    void deleteCountry(String id) throws NotActiveException;

}
