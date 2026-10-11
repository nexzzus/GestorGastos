package com.nexzus.gestiongastos.repository;

import com.nexzus.gestiongastos.model.Income;
import com.nexzus.gestiongastos.repository.projection.TransactionProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Income, UUID> {

    @Query(value = """
    SELECT
        i.id AS id,
        i.title AS title,
        i.amount AS amount,
        i.created_at AS createdAt,
        i.description AS description,
        i.payment_method AS paymentMethod,
        c.id AS categoryId,
        c.name AS categoryName,
        c.description AS categoryDescription,
        'INCOME' AS type
    FROM income i
    INNER JOIN category c ON c.id = i.category_id
    WHERE i.user_id = :userId

    UNION ALL

    SELECT
        e.id AS id,
        e.title AS title,
        e.amount AS amount,
        e.created_at AS createdAt,
        e.description AS description,
        e.payment_method AS paymentMethod,
        c.id AS categoryId,
        c.name AS categoryName,
        c.description AS categoryDescription,
        'EXPENSE' AS type
    FROM expense e
    INNER JOIN category c ON c.id = e.category_id
    WHERE e.user_id = :userId

    ORDER BY createdAt DESC
    """,
            nativeQuery = true)
    List<TransactionProjection> findTransactionsByUserId(
            @Param("userId") UUID userId,
            Pageable pageable
    );
}
