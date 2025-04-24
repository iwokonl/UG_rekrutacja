package pl.ug.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ug.model.log.AbstractLog;

public interface LogRepository extends JpaRepository<AbstractLog, Long> {
}