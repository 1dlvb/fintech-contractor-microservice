package com.fintech.contractor.repository;

import com.fintech.contractor.model.Industry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for working with industries in database.
 * @author Matushkin Anton
 * @see Industry
 */
@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {
}
