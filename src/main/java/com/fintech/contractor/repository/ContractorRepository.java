package com.fintech.contractor.repository;

import com.fintech.contractor.model.Contractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractorRepository extends JpaRepository<Contractor, String>, JpaSpecificationExecutor<Contractor> {

}
