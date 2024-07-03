package com.fintech.contractor.service;

import com.fintech.contractor.model.OrgForm;

import java.util.List;

public interface OrgFormService {

    List<OrgForm> fetchAllOrgForms();

    OrgForm saveOrUpdateOrgForm(OrgForm orgForm);

    OrgForm findOrgFormById(Long id);

    void deleteOrgForm(Long id);

}
