package com.fintech.contractor.controller;

import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.CountryService;
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

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryController {

    @NonNull
    private final CountryService countryService;
    @PutMapping("/save")
    public ResponseEntity<CountryDTO> saveOrUpdateCountry(@RequestBody CountryDTO countryDTO) {
        CountryDTO savedCountryDTO = countryService.saveOrUpdateCountry(countryDTO);
        return ResponseEntity.ok(savedCountryDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CountryDTO>> fetchAllCountries() {
        return ResponseEntity.ok(countryService.fetchAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryDTO> findCountryById(@PathVariable String id) {
        try {
            CountryDTO countryDTO = countryService.findCountryById(id);
            return ResponseEntity.ok(countryDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable String id) {
        try {
            countryService.deleteCountry(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
