package com.alphadevs.pos.repository;
import com.alphadevs.pos.domain.JobStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the JobStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobStatusRepository extends JpaRepository<JobStatus, Long> {

}
