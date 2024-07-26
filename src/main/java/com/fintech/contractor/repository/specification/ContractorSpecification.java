package com.fintech.contractor.repository.specification;

import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.payload.SearchContractorPayload;
import com.fintech.contractor.util.WildcatEnhancer;
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
        return (root, query, criteriaBuilder) -> {
            Stream<Predicate> predicateStream = Stream.of(
                    criteriaBuilder.isTrue(root.get("isActive")),
                    createEqualPredicate(criteriaBuilder, root, "id", payload.id()),
                    createEqualPredicate(criteriaBuilder, root, "parentId", payload.parentId()),
                    createLikePredicate(criteriaBuilder, root, "name", payload.name()),
                    createLikePredicate(criteriaBuilder, root, "nameFull", payload.nameFull()),
                    createLikePredicate(criteriaBuilder, root, "inn", payload.inn()),
                    createLikePredicate(criteriaBuilder, root, "ogrn", payload.ogrn())
            );

            List<Predicate> predicates = predicateStream
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            if (payload.country() != null) {
                Join<Contractor, Country> countryJoin = root.join("country", JoinType.LEFT);
                predicates.add(criteriaBuilder.like(countryJoin.get("id"),
                        WildcatEnhancer.enhanceWithWildcatMatching(payload.country())));
            }

            if (payload.industry() != null) {
                Join<Contractor, Industry> industryJoin = root.join("industry");
                predicates.add(criteriaBuilder.equal(industryJoin.get("id"), payload.industry().getId()));
                predicates.add(criteriaBuilder.equal(industryJoin.get("name"), payload.industry().getName()));
            }

            if (payload.orgForm() != null) {
                Join<Contractor, OrgForm> orgFormJoin = root.join("orgForm");
                predicates.add(criteriaBuilder.like(orgFormJoin.get("name"),
                        WildcatEnhancer.enhanceWithWildcatMatching(payload.orgForm())));
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

}
