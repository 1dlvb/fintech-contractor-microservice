package com.fintech.contractor.repository;

import com.fintech.contractor.model.OrgForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for working with org forms in database.
 * @author Matushkin Anton
 * @see com.fintech.contractor.model.OrgForm
 */
@Repository
public interface OrgFormRepository extends JpaRepository<OrgForm, Long> {
}
