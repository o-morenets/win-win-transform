package travel.winwin.authapi.domain.processing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, UUID> {
}