package com.fintech.contractor.controller;

import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.CountryService;
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
 * REST Controller for managing countries.
 * @author Matushkin Anton
 */
@RestController
@RequestMapping("contractor/country")
@RequiredArgsConstructor
@Tag(name = "Country API", description = "API for managing countries")
public class CountryController {

    @NonNull
    private final CountryService countryService;

    /**
     * Saves or updates a country.
     * @param countryDTO ({@link CountryDTO}) the country details to save or update.
     * @return a {@link ResponseEntity} containing the saved country details.
     */
    @Operation(summary = "Save or update country", description = "Saves or updates country details.")
    @ApiResponse(responseCode = "200",
            description = "Country saved",
            content = @Content(schema = @Schema(implementation = CountryDTO.class)))
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<CountryDTO> saveOrUpdateCountry(
            @RequestBody @Parameter(description = "Country details") CountryDTO countryDTO) {
        CountryDTO savedCountryDTO = countryService.saveOrUpdateCountry(countryDTO);
        return ResponseEntity.ok(savedCountryDTO);
    }

    /**
     * @return a {@link ResponseEntity} containing the list of all countries.
     * @see CountryDTO
     */
    @Operation(summary = "Find all countries", description = "Retrieves all countries.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Countries found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = CountryDTO.class)))),
    })
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
    @Operation(summary = "Find country by ID", description = "Retrieves country details by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Country found",
                    content = @Content(schema = @Schema(implementation = CountryDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Country not found")
    })
    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<CountryDTO> findCountryById(
            @PathVariable @Parameter(description = "ID of the country") String id) {
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
    @Operation(summary = "Delete country by ID", description = "Deletes country by ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Country found",
                    content = @Content(schema = @Schema(implementation = CountryDTO.class))),
            @ApiResponse(responseCode = "404",
                    description = "Country not found")
    })
    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteCountry(
            @PathVariable @Parameter(description = "ID of the country") String id) {
        try {
            countryService.deleteCountry(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
