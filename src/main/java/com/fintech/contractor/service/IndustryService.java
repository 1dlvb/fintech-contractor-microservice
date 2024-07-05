package com.fintech.contractor.service;

import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.exception.NotActiveException;

import java.util.List;

public interface IndustryService {

    List<IndustryDTO> fetchAllIndustries();

    IndustryDTO saveOrUpdateIndustry(IndustryDTO industryDTO);

    IndustryDTO findIndustryById(Long id) throws NotActiveException;

    void deleteIndustry(Long id) throws NotActiveException;

}
