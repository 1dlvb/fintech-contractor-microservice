package com.fintech.contractor.controller;

import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.CountryService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLogHttp;
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
 * REST Controller for managing countries.
 * @author Matushkin Anton
 */
@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {

    @NonNull
    private final CountryService countryService;

    /**
     * Saves or updates a country.
     * @param countryDTO ({@link CountryDTO}) the country details to save or update.
     * @return a {@link ResponseEntity} containing the saved country details.
     */
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<CountryDTO> saveOrUpdateCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO savedCountryDTO = countryService.saveOrUpdateCountry(countryDTO);
        return ResponseEntity.ok(savedCountryDTO);
    }

    /**
     * @return a {@link ResponseEntity} containing the list of all countries.
     * @see CountryDTO
     */
    @GetMapping("/all")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<CountryDTO>> fetchAllCountries() {
        return ResponseEntity.ok(countryService.fetchAllCountries());
    }

    /**
     * Retrieves a country by ID.
     * @param id the ID of the country.
     * @return a {@link ResponseEntity} containing the country details if found, otherwise a 404 response.
     * @see CountryDTO
     */
    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<CountryDTO> findCountryById(@PathVariable String id) {
        try {
            CountryDTO countryDTO = countryService.findCountryById(id);
            return ResponseEntity.ok(countryDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes a country by ID.
     * @param id the ID of the country to delete.
     * @return a {@link ResponseEntity} with no content if the country was successfully deleted, otherwise a 404 response.
     * @see CountryDTO
     */
    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteCountry(@PathVariable String id) {
        try {
            countryService.deleteCountry(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
