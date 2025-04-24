package pl.ug.profile.recruitment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.ug.controller.ComputerController;
import pl.ug.dto.CreateComputerDto;
import pl.ug.model.log.ExceptionLog;
import pl.ug.service.LogService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("recruitment")
@Order(Ordered.LOWEST_PRECEDENCE)
public class InitRecruitment implements CommandLineRunner {

    private final ComputerController computerController;
    private final LogService logService;
    private final ApplicationContext context;

    @Override
    @Transactional
    public void run(String... args) {

        try {
            computerController.saveComputer(new CreateComputerDto(
                    "komputer ACER Aspire",
                    LocalDateTime.of(2025, 1, 3, 0, 0),
                    LocalDateTime.of(2025, 1, 3, 0, 0),
                    BigDecimal.valueOf(345)
            ));

            computerController.saveComputer(new CreateComputerDto(
                    "komputer DELL Latitude",
                    LocalDateTime.of(2025, 1, 10, 0, 0),
                    LocalDateTime.of(2025, 1, 10, 0, 0),
                    BigDecimal.valueOf(543)
            ));

            computerController.saveComputer(new CreateComputerDto(
                    "komputer HP Victus",
                    LocalDateTime.of(2025, 1, 19, 0, 0),
                    LocalDateTime.of(2025, 1, 19, 0, 0),
                    BigDecimal.valueOf(346)
            ));

        } catch (Exception e) {
            log.error("An error occurred: " + e.getMessage(), e);
            logService.saveLog(new ExceptionLog("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
            int exitCode = SpringApplication.exit(context, () -> 1);
            System.exit(exitCode);
        }
    }
}
