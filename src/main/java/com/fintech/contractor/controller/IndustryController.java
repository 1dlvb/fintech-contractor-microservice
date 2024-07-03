package com.fintech.contractor.controller;

import com.fintech.contractor.model.Industry;
import com.fintech.contractor.service.IndustryService;
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
    public ResponseEntity<Industry> saveOrUpdateIndustry(@RequestBody Industry industry) {
        Industry savedIndustry = industryService.saveOrUpdateIndustry(industry);
        return ResponseEntity.ok(savedIndustry);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Industry>> fetchAllIndustries() {
        return ResponseEntity.ok(industryService.fetchAllIndustries());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Industry> findIndustryById(@PathVariable Long id) {
        Industry industry = industryService.findIndustryById(id);
        return ResponseEntity.ok(industry);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        industryService.deleteIndustry(id);
        return ResponseEntity.noContent().build();
    }

}
