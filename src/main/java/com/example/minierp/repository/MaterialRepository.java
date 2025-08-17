package com.example.minierp.repository;

import com.example.minierp.entity.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    Optional<Material> findByCode(String code);

    boolean existsByCode(String code);

    @Query("""
      select m from Material m
      where m.deleted = false
        and (:q is null or lower(m.code) like lower(concat('%', :q, '%'))
                     or lower(m.name) like lower(concat('%', :q, '%')))
        and (:unit is null or m.unit = :unit)
        and (:minStock is null or m.stockQuantity >= :minStock)
        and (:maxStock is null or m.stockQuantity <= :maxStock)
      order by m.id desc
    """)
    List<Material> search(
            @Param("q") String q,
            @Param("unit") String unit,
            @Param("minStock") Integer minStock,
            @Param("maxStock") Integer maxStock
    );

    Page<Material> findByDeletedFalse(Pageable pageable);

    List<Material> findByDeletedFalse();

    @Query("""
        select m.category as category, sum(m.stockQuantity) as total
        from Material m
        where m.deleted = false
        group by m.category
    """)
    List<CategoryStockRow> sumStockByCategory();

    interface CategoryStockRow {
        String getCategory();
        Long getTotal();
    }
}
