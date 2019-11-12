package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.Designs;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Designs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesignsRepository extends JpaRepository<Designs, Long>, JpaSpecificationExecutor<Designs> {

}
