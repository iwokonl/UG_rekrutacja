package pl.ug.profile.root;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(1)
public class Init implements CommandLineRunner {

    private final ApplicationContext context;
    private final Path xmlFilePath;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @Override
    public void run(String... args) {
        try {
            File outputFile = xmlFilePath.toFile();

            if ("create".equalsIgnoreCase(ddlAuto) || "create-drop".equalsIgnoreCase(ddlAuto)) {
                if (!outputFile.exists()) {
                    if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
                        outputFile.getParentFile().mkdirs();
                    }
                    outputFile.createNewFile();
                    log.info("Created new XML file: {}", outputFile.getAbsolutePath());
                }
            }

            if ("create-drop".equalsIgnoreCase(ddlAuto)) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    if (outputFile.exists() && outputFile.delete()) {
                        log.info("Deleted XML file on shutdown: {}", outputFile.getAbsolutePath());
                    }
                }));
            }
        } catch (IOException e) {
            log.error("Failed to manage XML file: {}", e.getMessage(), e);
        }
    }
}