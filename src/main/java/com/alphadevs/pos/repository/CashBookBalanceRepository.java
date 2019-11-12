package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.CashBookBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CashBookBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CashBookBalanceRepository extends JpaRepository<CashBookBalance, Long>, JpaSpecificationExecutor<CashBookBalance> {

}
