package com.fintech.contractor.service.impl;

import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.repository.OrgFormRepository;
import com.fintech.contractor.service.OrgFormService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLog;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrgFormServiceImpl implements OrgFormService {

    @NonNull
    private final OrgFormRepository repository;

    @NonNull
    private final ModelMapper modelMapper;

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public List<OrgFormDTO> fetchAllOrgForms() {
        List<OrgForm> industries = repository.findAll();
        return industries.stream()
                .map(orgForm -> modelMapper.map(orgForm, OrgFormDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public OrgFormDTO saveOrUpdateOrgForm(OrgFormDTO orgFormDTO) {
        OrgForm orgForm = modelMapper.map(orgFormDTO, OrgForm.class);
        if (orgForm.getId() != null && repository.existsById(orgForm.getId())) {
            OrgForm existingOrgForm = repository.findById(orgForm.getId()).orElse(null);
            if (existingOrgForm != null) {
                updateProperties(existingOrgForm, orgForm);
                orgForm = repository.save(existingOrgForm);
            }
        } else {
            orgForm = repository.save(orgForm);
        }
        return modelMapper.map(orgForm, OrgFormDTO.class);
    }

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public OrgFormDTO findOrgFormById(Long id) throws NotActiveException {
        Optional<OrgForm> orgFormOptional = repository.findById(id);
        OrgForm orgForm = orgFormOptional.orElseThrow(() ->
                new EntityNotFoundException("OrgForm not found for ID: " + id));

        if (orgForm.getIsActive()) {
            return modelMapper.map(orgForm, OrgFormDTO.class);
        } else {
            throw new NotActiveException("OrgForm is not active");
        }
    }

    @Override
    @AuditLog(logLevel = LogLevel.INFO)
    public void deleteOrgForm(Long id) throws NotActiveException {
        Optional<OrgForm> orgFormOptional = repository.findById(id);
        OrgForm orgForm = orgFormOptional.orElseThrow(() ->
                new EntityNotFoundException("OrgForm not found for ID: " + id));
        if (orgForm.getIsActive()) {
            orgForm.setIsActive(false);
            repository.save(orgForm);
        } else {
            throw new NotActiveException("OrgForm is not active");
        }
    }

    private void updateProperties(OrgForm existingOrgForm, OrgForm newOrgFormData) {
        existingOrgForm.setName(newOrgFormData.getName());
    }

}
