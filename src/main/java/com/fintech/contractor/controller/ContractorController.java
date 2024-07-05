package com.fintech.contractor.controller;

import com.fintech.contractor.dto.ContractorDTO;
import com.fintech.contractor.exception.NotActiveException;
import com.fintech.contractor.service.ContractorService;
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

@RestController
@RequestMapping("/contractor")
@RequiredArgsConstructor
public class ContractorController {

    @NonNull
    private final ContractorService contractorService;
    @PutMapping("/save")
    public ResponseEntity<ContractorDTO> saveOrUpdateContractor(@RequestBody ContractorDTO contractorDTO) {
        ContractorDTO savedContractorDTO = contractorService.saveOrUpdateContractor(contractorDTO);
        return ResponseEntity.ok(savedContractorDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractorDTO> findContractorById(@PathVariable String id) {
        try {
            ContractorDTO contractorDTO = contractorService.findContractorById(id);
            return ResponseEntity.ok(contractorDTO);
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContractor(@PathVariable String id) {
        try {
            contractorService.deleteContractor(id);
            return ResponseEntity.noContent().build();
        } catch (NotActiveException | EntityNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
    }

}
