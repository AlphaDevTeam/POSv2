package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.PurchaseAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PurchaseAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseAccountRepository extends JpaRepository<PurchaseAccount, Long>, JpaSpecificationExecutor<PurchaseAccount> {

}
