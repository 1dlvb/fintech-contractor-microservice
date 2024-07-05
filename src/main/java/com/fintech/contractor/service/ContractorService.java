package com.fintech.contractor.service;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.exception.NotActiveException;

public interface ContractorService {

    ContractorDTO saveOrUpdateContractor(ContractorDTO contractorDTO);

    ContractorDTO findContractorById(String id) throws NotActiveException;

    void deleteContractor(String id) throws NotActiveException;

}
