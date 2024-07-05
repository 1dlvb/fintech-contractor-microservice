package com.fintech.contractor.service;

import com.fintech.contractor.dto.CountryDTO;
import com.fintech.contractor.exception.NotActiveException;

import java.util.List;

public interface CountryService {

    List<CountryDTO> fetchAllCountries();

    CountryDTO saveOrUpdateCountry(CountryDTO countryDTO);

    CountryDTO findCountryById(String id) throws NotActiveException;

    void deleteCountry(String id) throws NotActiveException;

}
