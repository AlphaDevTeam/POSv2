package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.ItemBinCard;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ItemBinCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemBinCardRepository extends JpaRepository<ItemBinCard, Long>, JpaSpecificationExecutor<ItemBinCard> {

}
