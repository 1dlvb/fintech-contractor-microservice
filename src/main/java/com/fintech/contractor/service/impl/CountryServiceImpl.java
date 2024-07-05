package com.fintech.contractor.service.impl;

import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.repository.CountryRepository;
import com.fintech.contractor.service.CountryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    @NonNull
    private final CountryRepository repository;
    @NonNull
    private final ModelMapper modelMapper;

    public List<CountryDTO> fetchAllCountries() {
        List<Country> countries = repository.findAll();
        return countries.stream()
                .map(country -> modelMapper.map(country, CountryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CountryDTO saveOrUpdateCountry(CountryDTO countryDTO) {
        Country country = modelMapper.map(countryDTO, Country.class);
        if (country.getId() != null && repository.existsById(country.getId())) {
            Country existingCountry = repository.findById(country.getId()).orElse(null);
            if (existingCountry != null) {
                updateProperties(existingCountry, country);
                country = repository.save(existingCountry);
            }
        } else {
            country = repository.save(country);
        }
        return modelMapper.map(country, CountryDTO.class);
    }

    @Override
    public CountryDTO findCountryById(String id) throws NotActiveException {
        Optional<Country> countryOptional = repository.findById(id);
        Country country = countryOptional.orElseThrow(() ->
                new EntityNotFoundException("Country not found for ID: " + id));

        if (country.getIsActive()) {
            return modelMapper.map(country, CountryDTO.class);
        } else {
            throw new NotActiveException("Country is not active");
        }
    }

    @Override
    public void deleteCountry(String id) throws NotActiveException {
        Optional<Country> countryOptional = repository.findById(id);
        Country country = countryOptional.orElseThrow(() ->
                new EntityNotFoundException("Country not found for ID: " + id));
        if (country.getIsActive()) {
            country.setIsActive(false);
            repository.save(country);
        } else {
            throw new NotActiveException("Country is not active");
        }
    }

    private void updateProperties(Country existingCountry, Country newCountryData) {
        existingCountry.setName(newCountryData.getName());
    }

}
