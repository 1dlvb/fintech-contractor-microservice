package com.fintech.contractor.repository.specification;

import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.util.WildcatEnhancer;
import com.onedlvb.jwtlib.util.RolesEnum;
import com.onedlvb.jwtlib.util.SecurityUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Specification class for querying {@link Contractor} entities based on search criteria.
 * This class provides static methods to generate JPA {@link Specification} instances
 * for searching contractors with various criteria such as IDs, names, inns, ogrns, country,
 * industry, and org forms.
 * @author Matushkin Anton
 */
public final class ContractorSpecification {

    private ContractorSpecification() {}

    /**
     * Generates a JPA Specification for querying {@link Contractor} entities based on the provided search payload.
     * @param payload The search payload containing criteria to filter contractors.
     * @return A {@link Specification} instance representing the query criteria.
     */
    public static Specification<Contractor> findContractorsBySpecifications(SearchContractorPayload payload) {
        SearchContractorPayload finalPayload = roleBasedPayloadModification(payload);
        if (finalPayload == null) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.disjunction();
        }

        return (root, query, criteriaBuilder) -> {
            Stream<Predicate> predicateStream = Stream.of(
                    criteriaBuilder.isTrue(root.get("isActive")),
                    createEqualPredicate(criteriaBuilder, root, "id", finalPayload.getId()),
                    createEqualPredicate(criteriaBuilder, root, "parentId", finalPayload.getParentId()),
                    createLikePredicate(criteriaBuilder, root, "name", finalPayload.getName()),
                    createLikePredicate(criteriaBuilder, root, "nameFull", finalPayload.getNameFull()),
                    createLikePredicate(criteriaBuilder, root, "inn", finalPayload.getInn()),
                    createLikePredicate(criteriaBuilder, root, "ogrn", finalPayload.getOgrn())
            );

            List<Predicate> predicates = predicateStream
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (finalPayload.getCountry() != null) {
                Join<Contractor, Country> countryJoin = root.join("country", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(countryJoin.get("id"),
                        WildcatEnhancer.enhanceWithWildcatMatching(finalPayload.getCountry())));
            }

            if (finalPayload.getIndustry() != null) {
                Join<Contractor, Industry> industryJoin = root.join("industry");
                predicates.add(criteriaBuilder.equal(industryJoin.get("id"), finalPayload.getIndustry().getId()));
                predicates.add(criteriaBuilder.equal(industryJoin.get("name"), finalPayload.getIndustry().getName()));
            }

            if (finalPayload.getOrgForm() != null) {
                Join<Contractor, OrgForm> orgFormJoin = root.join("orgForm");
                predicates.add(criteriaBuilder.like(orgFormJoin.get("name"),
                        WildcatEnhancer.enhanceWithWildcatMatching(finalPayload.getOrgForm())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Util method to create an equal predicate for a given field and value.
     * @param cb The {@link CriteriaBuilder} instance.
     * @param root The root entity {@link Root}.
     * @param fieldName The name of the field in database.
     * @param value The value to compare against.
     * @return A {@link Predicate} representing the equal comparison, or null if value is null.
     */
    private static Predicate createEqualPredicate(CriteriaBuilder cb, Root<Contractor> root, String fieldName, Object value) {
        return value != null ? cb.equal(root.get(fieldName), value) : null;
    }

    /**
     * Util method to create a like predicate for a given field and value.
     * @param cb The {@link CriteriaBuilder} instance.
     * @param root The root entity {@link Root}.
     * @param fieldName The name of the field in database.
     * @param value The value to match against.
     * @return A {@link Predicate} representing the like comparison, or null if value is null.
     */
    private static Predicate createLikePredicate(CriteriaBuilder cb, Root<Contractor> root, String fieldName, String value) {
        return value != null ? cb.like(root.get(fieldName), WildcatEnhancer.enhanceWithWildcatMatching(value)) : null;
    }

    /**
     * Modifies the given payload based on the roles of the user.
     * If the user has SUPERUSER or CONTRACTOR_SUPERUSER roles, the payload is returned as is.
     * If the payload is empty, it returns a payload with type based on the user role.
     * If the payload is not empty except for country, it returns null.
     * Otherwise, it handles the payload based on the user's roles and returns the modified payload.
     *<p>
     * @param payload The original payload containing search criteria.
     * @return The modified payload or null based on the user's roles and payload content.
     */
    private static SearchContractorPayload roleBasedPayloadModification(SearchContractorPayload payload) {
        boolean hasSuperuser = SecurityUtil.hasRole(RolesEnum.SUPERUSER);
        boolean hasContractorSuperuser = SecurityUtil.hasRole(RolesEnum.CONTRACTOR_SUPERUSER);

        if (hasSuperuser || hasContractorSuperuser) {
            return payload;
        }

        if (payload.isEmpty()) {
            return handleEmptyPayload();
        }
        if (!payload.isEmptyExceptCountry()) {
            return null;
        }
        boolean hasContractorRus = SecurityUtil.hasRole(RolesEnum.CONTRACTOR_RUS);

        return handleNonEmptyPayload(payload, hasContractorRus);
    }

    /**
     * Handles the case where the payload is empty.
     * It creates a new payload with the country based on the user's role.
     * <p>
     * @return The modified payload or null if the user has no relevant roles.
     */
    private static SearchContractorPayload handleEmptyPayload() {
        boolean hasContractorRusRole = SecurityUtil.hasRole(RolesEnum.CONTRACTOR_RUS);

        String country = null;
        if (hasContractorRusRole) {
            country = "RUS";
        }

        if (country != null) {
            return SearchContractorPayload.builder().country(country).build();
        }
        return null;
    }

    /**
     * Handles the case where the payload is not empty except for country.
     * It checks the type against the user's roles and returns the payload if it matches.
     * <p>
     * @param payload The original payload containing search criteria.
     * @param hasContractorRus Indicates if the user has the CONTRACTOR_RUS role.
     * @return The modified payload or null based on the user's roles and payload content.
     */
    private static SearchContractorPayload handleNonEmptyPayload(SearchContractorPayload payload,
                                                                 boolean hasContractorRus) {
        String country = payload.getCountry();
        if (country == null) {
            return null;
        }

        if (hasContractorRus) {
            boolean hasRelevantType = country.equals("RUS");
            payload = SearchContractorPayload.builder().country(country).build();
            return hasRelevantType ? payload : null;
        }

        return null;
    }

}
