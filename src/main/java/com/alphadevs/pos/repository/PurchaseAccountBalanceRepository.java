package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.PurchaseAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseAccountBalanceRepository extends JpaRepository<PurchaseAccountBalance, Long>, JpaSpecificationExecutor<PurchaseAccountBalance> {

}
