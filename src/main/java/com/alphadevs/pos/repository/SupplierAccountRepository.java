package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.SupplierAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SupplierAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SupplierAccountRepository extends JpaRepository<SupplierAccount, Long>, JpaSpecificationExecutor<SupplierAccount> {

}
