package com.fintech.contractor.repository;

import com.fintech.contractor.model.OrgForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgFormRepository extends JpaRepository<OrgForm, Long> {
}
