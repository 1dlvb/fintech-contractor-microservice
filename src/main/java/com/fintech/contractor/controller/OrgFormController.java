package com.fintech.contractor.controller;

import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.service.OrgFormService;
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
    public ResponseEntity<OrgForm> saveOrUpdateOrgForm(@RequestBody OrgForm orgForm) {
        OrgForm savedOrgForm = orgFormService.saveOrUpdateOrgForm(orgForm);
        return ResponseEntity.ok(savedOrgForm);
    }

    @GetMapping("/all")
    public ResponseEntity<List<OrgForm>> fetchAllOrgForms() {
        return ResponseEntity.ok(orgFormService.fetchAllOrgForms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrgForm> findOrgFormById(@PathVariable Long id) {
        OrgForm orgForm = orgFormService.findOrgFormById(id);
        return ResponseEntity.ok(orgForm);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteOrgForm(@PathVariable Long id) {
        orgFormService.deleteOrgForm(id);
        return ResponseEntity.noContent().build();
    }

}
