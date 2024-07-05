package com.fintech.contractor.service;

import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;

import java.util.List;

public interface OrgFormService {

    List<OrgFormDTO> fetchAllOrgForms();

    OrgFormDTO saveOrUpdateOrgForm(OrgFormDTO orgForm);

    OrgFormDTO findOrgFormById(Long id) throws NotActiveException;

    void deleteOrgForm(Long id) throws NotActiveException;

}
