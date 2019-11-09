package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.CustomerAccountBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CustomerAccountBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerAccountBalanceRepository extends JpaRepository<CustomerAccountBalance, Long> {

}
