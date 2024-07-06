package com.fintech.contractor.repository.specification;

import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.payload.SearchContractorPayload;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public final class ContractorSpecification {

    private ContractorSpecification() {}

    public static Specification<Contractor> findContractorBySpecifications(SearchContractorPayload payload) {
        List<Predicate> filterConditions = new ArrayList<>();
        return (root, query, criteriaBuilder) -> {
            List<BiFunction<CriteriaBuilder, Root<Contractor>, Predicate>> fieldPredicates = List.of(
                    (cb, r) -> createEqualPredicate(cb, r, "id", payload.id()),
                    (cb, r) -> createEqualPredicate(cb, r, "parent.id", payload.parentId()), // Note: Changed to "parent.id"
                    (cb, r) -> createLikePredicate(cb, r, "name", payload.name()),
                    (cb, r) -> createLikePredicate(cb, r, "nameFull", payload.nameFull()),
                    (cb, r) -> createLikePredicate(cb, r, "inn", payload.inn()),
                    (cb, r) -> createLikePredicate(cb, r, "ogrn", payload.ogrn())
            );

            fieldPredicates.stream()
                    .map(predicate -> predicate.apply(criteriaBuilder, root))
                    .filter(Objects::nonNull)
                    .forEach(filterConditions::add);

            if (payload.countryName() != null) {
                Join<Contractor, Country> join = root.join("country");
                filterConditions.add(criteriaBuilder.like(join.get("name"),
                        enhanceWithWildcatMatching(payload.countryName())));
            }

            if (payload.industry() != null) {
                Join<Contractor, Industry> join = root.join("industry");
                filterConditions.add(criteriaBuilder.equal(join.get("id"), payload.industry().getId()));
                filterConditions.add(criteriaBuilder.equal(join.get("name"),
                        enhanceWithWildcatMatching(payload.industry().getName())));
            }

            if (payload.orgFormName() != null) {
                Join<Contractor, OrgForm> join = root.join("orgForm");
                filterConditions.add(criteriaBuilder.like(join.get("name"),
                        enhanceWithWildcatMatching(payload.orgFormName())));
            }

            return criteriaBuilder.and(filterConditions.toArray(new Predicate[0]));
        };
    }

    private static Predicate createEqualPredicate(CriteriaBuilder cb, Root<Contractor> root, String fieldName, Object value) {
        return value != null ? cb.equal(root.get(fieldName), value) : null;
    }

    private static Predicate createLikePredicate(CriteriaBuilder cb, Root<Contractor> root, String fieldName, String value) {
        return value != null ? cb.like(root.get(fieldName), enhanceWithWildcatMatching(value)) : null;
    }

    private static String enhanceWithWildcatMatching(String string) {
        return String.format("%%%s%%", string);
    }

}
