package mdek.repository;

import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import mdek.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public class ProductSpecification {


    public static Specification<Product> nameContains(String query) {
        if (query == null || query.isBlank()) {
            return null;
        }
        return (root, queryObj, cb) -> cb.like(cb.lower(root.get("name")), "%" + query.toLowerCase() + "%");
    }


    public static Specification<Product> brandIn(List<Long> brandIds) {
        if (brandIds == null || brandIds.isEmpty()) {
            return null;
        }
        return (root, queryObj, cb) -> root.get("brand").get("id").in(brandIds);
    }


    public static Specification<Product> categoryIn(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return null;

        return (root, cq, cb) -> {
            Subquery<Long> sub = cq.subquery(Long.class);
            Root<Product> subRoot = sub.from(Product.class);

            var join = subRoot.join("categories", JoinType.INNER);

            sub.select(subRoot.get("id"))
                    .where(
                            cb.equal(subRoot.get("id"), root.get("id")),
                            join.get("id").in(categoryIds)
                    );

            return cb.exists(sub);
        };
    }

    public static Specification<Product> withFilters(String query, List<Long> brandIds, List<Long> categoryIds) {
        return Specification
                .where(nameContains(query))
                .and(brandIn(brandIds))
                .and(categoryIn(categoryIds));
    }
}
