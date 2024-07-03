package com.fintech.contractor.controller;

import com.fintech.contractor.model.Country;
import com.fintech.contractor.service.CountryService;
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
    public ResponseEntity<Country> saveOrUpdateCountry(@RequestBody Country country) {
        Country savedCountry = countryService.saveOrUpdateCountry(country);
        return ResponseEntity.ok(savedCountry);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Country>> fetchAllCountries() {
        return ResponseEntity.ok(countryService.fetchAllCountries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Country> findCountryById(@PathVariable String id) {
        Country country = countryService.findCountryById(id);
        return ResponseEntity.ok(country);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable String id) {
        countryService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }

}
