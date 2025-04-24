package pl.ug.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.model.log.AbstractLog;
import pl.ug.repository.LogRepository;

@Service
public class LogService {

    private final LogRepository logRepository;

    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Transactional
    public void saveLog(AbstractLog log) {
        logRepository.save(log);
    }
}