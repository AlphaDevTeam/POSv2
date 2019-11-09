package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.GoodsReceipt;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GoodsReceipt entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long> {

}
