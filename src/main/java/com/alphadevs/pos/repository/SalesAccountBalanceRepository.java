package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.SalesAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the SalesAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SalesAccountBalanceRepository extends JpaRepository<SalesAccountBalance, Long>, JpaSpecificationExecutor<SalesAccountBalance> {

}
