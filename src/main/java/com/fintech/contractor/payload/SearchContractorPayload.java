package com.fintech.contractor.payload;

import com.fintech.contractor.dto.IndustryDTO;

public record SearchContractorPayload(String id,
                                      String parentId,
                                      String name,
                                      String nameFull,
                                      String inn,
                                      String ogrn,
                                      String countryName,
                                      IndustryDTO industry,
                                      String orgFormName) {
}
