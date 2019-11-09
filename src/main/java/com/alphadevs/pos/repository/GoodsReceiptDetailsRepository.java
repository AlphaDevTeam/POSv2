package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.GoodsReceiptDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the GoodsReceiptDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsReceiptDetailsRepository extends JpaRepository<GoodsReceiptDetails, Long> {

}
