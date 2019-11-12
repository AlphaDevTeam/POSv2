package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.ExUser;
import com.alphadevs.pos.domain.Location;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Location entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocationRepository extends JpaRepository<Location, Long>, JpaSpecificationExecutor<Location> {

    List<Location> findAllByUsers(ExUser exUser);
}
