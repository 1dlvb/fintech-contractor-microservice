package com.fintech.contractor.service;

import com.fintech.contractor.model.Industry;

import java.util.List;

public interface IndustryService {

    List<Industry> fetchAllIndustries();

    Industry saveOrUpdateIndustry(Industry industry);

    Industry findIndustryById(Long id);

    void deleteIndustry(Long id);

}
