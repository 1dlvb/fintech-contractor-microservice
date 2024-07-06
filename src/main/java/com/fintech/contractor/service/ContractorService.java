package com.fintech.contractor.service;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.payload.SearchContractorPayload;

import java.util.List;
import java.util.UUID;

public interface ContractorService {

    List<ContractorDTO> findContractors(SearchContractorPayload payload);

    ContractorDTO saveOrUpdateContractor(ContractorDTO contractorDTO);

    ContractorDTO findContractorById(String id) throws NotActiveException;

    void deleteContractor(String id) throws NotActiveException;

}
