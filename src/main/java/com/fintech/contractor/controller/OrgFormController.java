package com.fintech.contractor.controller;

import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.OrgFormService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/org_form")
@RequiredArgsConstructor
public class OrgFormController {

    @NonNull
    private final OrgFormService orgFormService;
    @PutMapping("/save")
    public ResponseEntity<OrgFormDTO> saveOrUpdateOrgForm(@RequestBody OrgFormDTO orgFormDTO) {
        OrgFormDTO savedOrgFormDTO = orgFormService.saveOrUpdateOrgForm(orgFormDTO);
        return ResponseEntity.ok(savedOrgFormDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrgFormDTO>> fetchAllCountries() {
        return ResponseEntity.ok(orgFormService.fetchAllOrgForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgFormDTO> findOrgFormById(@PathVariable Long id) {
        try {
            OrgFormDTO orgFormDTO = orgFormService.findOrgFormById(id);
            return ResponseEntity.ok(orgFormDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrgForm(@PathVariable Long id) {
        try {
            orgFormService.deleteOrgForm(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
