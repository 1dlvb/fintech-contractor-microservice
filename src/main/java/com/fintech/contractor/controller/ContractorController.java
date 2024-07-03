package com.fintech.contractor.controller;

import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.service.ContractorService;
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
    public ResponseEntity<Contractor> saveOrUpdateContractor(@RequestBody Contractor contractor) {
        Contractor savedContractor = contractorService.saveOrUpdateContractor(contractor);
        return ResponseEntity.ok(savedContractor);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contractor> findContractorById(@PathVariable String id) {
        Contractor contractor = contractorService.findContractorById(id);
        return ResponseEntity.ok(contractor);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteContractor(@PathVariable String id) {
        contractorService.deleteContractor(id);
        return ResponseEntity.noContent().build();
    }

}
