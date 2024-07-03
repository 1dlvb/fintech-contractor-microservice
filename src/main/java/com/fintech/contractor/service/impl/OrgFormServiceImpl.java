package com.fintech.contractor.service.impl;

import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.repository.OrgFormRepository;
import com.fintech.contractor.service.OrgFormService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrgFormServiceImpl implements OrgFormService {

    @NonNull
    private final OrgFormRepository repository;

    @Override
    public List<OrgForm> fetchAllOrgForms() {
        return repository.findAll();
    }

    @Override
    public OrgForm saveOrUpdateOrgForm(OrgForm orgForm) {
        if (orgForm.getId() != null && repository.existsById(orgForm.getId())) {
            OrgForm existingOrgForm = repository.findById(orgForm.getId()).orElse(null);
            if (existingOrgForm != null) {
                updateProperties(existingOrgForm, orgForm);
                return repository.save(existingOrgForm);
            }
        }
        return repository.save(orgForm);
    }

    @Override
    public OrgForm findOrgFormById(Long id) {
        Optional<OrgForm> orgForm = repository.findById(id);
        return orgForm.orElseThrow(() ->
                new EntityNotFoundException("OrgForm not found for ID: " + id));
    }

    @Override
    public void deleteOrgForm(Long id) {
        OrgForm orgForm = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrgForm not found for ID: " + id));
        orgForm.setIsActive(false);
        repository.save(orgForm);
    }

    private void updateProperties(OrgForm existingOrgForm, OrgForm newOrgFormData) {
        existingOrgForm.setName(newOrgFormData.getName());
    }

}
