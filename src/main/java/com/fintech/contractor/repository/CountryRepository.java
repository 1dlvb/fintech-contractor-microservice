package com.fintech.contractor.repository;

import com.fintech.contractor.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for working with countries in database.
 * @author Matushkin Anton
 * @see Country
 */
@Repository
public interface CountryRepository extends JpaRepository<Country, String> {
}
