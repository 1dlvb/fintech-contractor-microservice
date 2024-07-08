package com.fintech.contractor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity class representing an org form.
 * This class maps to the "org form" table in the database.
 * It includes basic information about an org form entity.
 * @author Matushkin Anton
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "org_form")
public class OrgForm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

}
