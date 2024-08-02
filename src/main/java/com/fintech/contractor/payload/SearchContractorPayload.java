package com.fintech.contractor.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fintech.contractor.dto.IndustryDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A record representing a payload for searching contractors.
 * @author Matushkin Anton
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({ "id", "parent_id", "name", "name_full", "inn", "ogrn", "country", "industry", "org_form" })
public class SearchContractorPayload {

        @Schema(description = "The ID of the contractor.", example = "fa23a2e5-1ef3-48cd-a970-127bc0ea2a47")
        private String id;

        @JsonProperty("parent_id")
        @Schema(description = "The parent ID of the contractor.", example = "feb362a1-5c42-4302-b3a7-5f9db7ee7988")
        private String parentId;

        @Schema(description = "The name of the contractor.", example = "sample name")
        private String name;

        @JsonProperty("name_full")
        @Schema(description = "The full name of the contractor.", example = "sample full name")
        private String nameFull;

        @Schema(description = "The individual tax number (INN) of the contractor.", example = "123456789")
        private String inn;

        @Schema(description = "The primary state registration number (OGRN) of the contractor.", example = "987654321")
        private String ogrn;

        @Schema(description = "The country name of the contractor.", example = "ABH")
        private String country;

        @Schema(description = "The industry details of the contractor.")
        private IndustryDTO industry;

        @JsonProperty("org_form")
        @Schema(description = "The organizational form name of the contractor.",
                example = "Автономная некоммерческая организация")
        private String orgForm;

        /**
         * @return true if all fields are null
         */
        public boolean isEmpty() {
                return  (id == null
                        && parentId == null
                        && name == null
                        && nameFull == null
                        && inn == null
                        && ogrn == null
                        && country == null
                        && industry == null
                        && orgForm == null);
        }

        /**
         * @return true if all fields except country are null
         */
        public boolean isEmptyExceptCountry() {

                return  (id == null
                        && parentId == null
                        && name == null
                        && nameFull == null
                        && inn == null
                        && ogrn == null
                        && country != null
                        && industry == null
                        && orgForm == null);
        }

}
