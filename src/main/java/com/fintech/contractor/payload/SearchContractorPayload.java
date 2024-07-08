package com.fintech.contractor.payload;

import com.fintech.contractor.dto.IndustryDTO;

/**
 * A record representing a payload for searching contractors..
 *
 * @param id The ID of the contractor.
 * @param parentId The parent ID of the contractor.
 * @param name The name of the contractor.
 * @param nameFull The full name of the contractor.
 * @param inn The individual tax number (INN) of the contractor.
 * @param ogrn The primary state registration number (OGRN) of the contractor.
 * @param country The country of the contractor.
 * @param industry The industry details of the contractor.
 * @param orgForm The organizational form of the contractor.
 * @author Matushkin Anton
 */
public record SearchContractorPayload(String id,
                                      String parentId,
                                      String name,
                                      String nameFull,
                                      String inn,
                                      String ogrn,
                                      String country,
                                      IndustryDTO industry,
                                      String orgForm) {
}
