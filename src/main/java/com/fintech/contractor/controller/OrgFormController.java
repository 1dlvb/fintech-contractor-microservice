package com.fintech.contractor.controller;

import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.OrgFormService;
import com.onedlvb.advice.LogLevel;
import com.onedlvb.advice.annotation.AuditLogHttp;
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

/**
 * REST Controller for managing org forms.
 * @author Matushkin Anton
 */
@RestController
@RequestMapping("/org_form")
@RequiredArgsConstructor
public class OrgFormController {

    @NonNull
    private final OrgFormService orgFormService;

    /**
     * Saves or updates an org form.
     * @param orgFormDTO ({@link OrgFormDTO}) the org form details to save or update.
     * @return a {@link ResponseEntity} containing the saved org form details.
     */
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<OrgFormDTO> saveOrUpdateOrgForm(@RequestBody OrgFormDTO orgFormDTO) {
        OrgFormDTO savedOrgFormDTO = orgFormService.saveOrUpdateOrgForm(orgFormDTO);
        return ResponseEntity.ok(savedOrgFormDTO);
    }

    /**
     * @return a {@link ResponseEntity} containing the list of all org forms.
     * @see OrgFormDTO
     */
    @GetMapping("/all")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<OrgFormDTO>> fetchAllCountries() {
        return ResponseEntity.ok(orgFormService.fetchAllOrgForms());
    }

    /**
     * Retrieves an org form by ID.
     * @param id the ID of the org form.
     * @return a {@link ResponseEntity} containing the org form details if found, otherwise a 404 response.
     * @see OrgFormDTO
     */
    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<OrgFormDTO> findOrgFormById(@PathVariable Long id) {
        try {
            OrgFormDTO orgFormDTO = orgFormService.findOrgFormById(id);
            return ResponseEntity.ok(orgFormDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Deletes an org form by ID.
     * @param id the ID of the org form to delete.
     * @return a {@link ResponseEntity} with no content if the org form was successfully deleted, otherwise a 404 response.
     * @see OrgFormDTO
     */
    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteOrgForm(@PathVariable Long id) {
        try {
            orgFormService.deleteOrgForm(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
