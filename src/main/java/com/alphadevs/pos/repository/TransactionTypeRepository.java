package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.TransactionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TransactionType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionType, Long>, JpaSpecificationExecutor<TransactionType> {

}
