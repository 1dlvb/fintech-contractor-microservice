package com.fintech.contractor.service;

import com.fintech.contractor.dto.OrgFormDTO;
import com.fintech.contractor.exception.NotActiveException;

import java.util.List;

/**
 * Service interface for managing org forms.
 * Defines methods for CRUD operations on org forms.
 * @author Matushkin Anton
 */
public interface OrgFormService {

    /**
     * Retrieves all org forms.
     * @return a list of {@link OrgFormDTO} objects representing all org forms.
     */
    List<OrgFormDTO> fetchAllOrgForms();

    /**
     * Saves or updates an org form.
     * @param orgForm the org form details to save or update.
     * @return the saved or updated {@link OrgFormDTO} object.
     */
    OrgFormDTO saveOrUpdateOrgForm(OrgFormDTO orgForm);

    /**
     * Retrieves an org form by its ID.
     * @param id the ID of the org form to retrieve.
     * @return the {@link OrgFormDTO} object with the specified ID.
     * @throws NotActiveException if the org form is not active.
     */
    OrgFormDTO findOrgFormById(Long id) throws NotActiveException;

    /**
     * Deletes an org form by its ID.
     * @param id the ID of the org form to delete.
     * @throws NotActiveException if the org form is not active.
     */
    void deleteOrgForm(Long id) throws NotActiveException;

}
