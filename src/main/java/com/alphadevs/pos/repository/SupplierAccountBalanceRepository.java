package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.SupplierAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SupplierAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierAccountBalanceRepository extends JpaRepository<SupplierAccountBalance, Long>, JpaSpecificationExecutor<SupplierAccountBalance> {

}
