package com.fintech.contractor.service;

import com.fintech.contractor.model.Contractor;

public interface ContractorService {

    Contractor saveOrUpdateContractor(Contractor contractor);

    Contractor findContractorById(String id);

    void deleteContractor(String id);

}
