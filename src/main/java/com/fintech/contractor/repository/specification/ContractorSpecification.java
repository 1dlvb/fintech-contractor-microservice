package com.fintech.contractor.repository.specification;

import com.fintech.contractor.model.Contractor;
import com.fintech.contractor.model.Country;
import com.fintech.contractor.model.Industry;
import com.fintech.contractor.model.OrgForm;
import com.fintech.contractor.payload.SearchContractorPayload;
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

public final class ContractorSpecification {

    private ContractorSpecification() {}

    public static Specification<Contractor> findContractorBySpecifications(SearchContractorPayload payload) {
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
                predicates.add(criteriaBuilder.like(countryJoin.get("name"),
                        enhanceWithWildcatMatching(payload.country())));
            }

            if (payload.industry() != null) {
                Join<Contractor, Industry> industryJoin = root.join("industry");
                predicates.add(criteriaBuilder.equal(industryJoin.get("id"), payload.industry().getId()));
                predicates.add(criteriaBuilder.equal(industryJoin.get("name"), payload.industry().getId()));
            }

            if (payload.orgForm() != null) {
                Join<Contractor, OrgForm> orgFormJoin = root.join("orgForm");
                predicates.add(criteriaBuilder.like(orgFormJoin.get("name"),
                        enhanceWithWildcatMatching(payload.orgForm())));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
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
