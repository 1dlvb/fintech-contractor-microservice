package com.fintech.contractor.controller;

import com.fintech.contractor.dto.IndustryDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.IndustryService;
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

@RestController
@RequestMapping("/industry")
@RequiredArgsConstructor
public class IndustryController {

    @NonNull
    private final IndustryService industryService;
    @PutMapping("/save")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<IndustryDTO> saveOrUpdateIndustry(@RequestBody IndustryDTO industryDTO) {
        IndustryDTO savedIndustryDTO = industryService.saveOrUpdateIndustry(industryDTO);
        return ResponseEntity.ok(savedIndustryDTO);
    }

    @GetMapping("/all")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<List<IndustryDTO>> fetchAllCountries() {
        return ResponseEntity.ok(industryService.fetchAllIndustries());
    }

    @GetMapping("/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<IndustryDTO> findIndustryById(@PathVariable Long id) {
        try {
            IndustryDTO industryDTO = industryService.findIndustryById(id);
            return ResponseEntity.ok(industryDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @AuditLogHttp(logLevel = LogLevel.INFO)
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        try {
            industryService.deleteIndustry(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
