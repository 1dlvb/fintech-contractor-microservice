package com.fintech.contractor.service.impl;

import com.fintech.contractor.model.Country;
import com.fintech.contractor.repository.CountryRepository;
import com.fintech.contractor.service.CountryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    @NonNull
    private final CountryRepository repository;
    @Override
    public List<Country> fetchAllCountries() {
        return repository.findAll();
    }

    @Override
    public Country saveOrUpdateCountry(Country country) {
        if (country.getId() != null && repository.existsById(country.getId())) {
            Country existingCountry = repository.findById(country.getId()).orElse(null);
            if (existingCountry != null) {
                updateProperties(existingCountry, country);
                return repository.save(existingCountry);
            }
        }
        return repository.save(country);
    }

    @Override
    public Country findCountryById(String id) {
        Optional<Country> country = repository.findById(id);
        return country.orElseThrow(() ->
                new EntityNotFoundException("Country not found for ID: " + id));
    }

    @Override
    public void deleteCountry(String id) {
        Country country = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Country not found for ID: " + id));
        country.setIsActive(false);
        repository.save(country);
    }

    private void updateProperties(Country existingCountry, Country newCountryData) {
        existingCountry.setName(newCountryData.getName());
    }

}
