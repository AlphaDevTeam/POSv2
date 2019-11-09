package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.Desings;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Desings entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesingsRepository extends JpaRepository<Desings, Long> {

}
