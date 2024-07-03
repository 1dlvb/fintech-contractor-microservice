package com.fintech.contractor.service;

import com.fintech.contractor.model.Country;

import java.util.List;

public interface CountryService {

    List<Country> fetchAllCountries();

    Country saveOrUpdateCountry(Country country);

    Country findCountryById(String id);

    void deleteCountry(String id);

}
