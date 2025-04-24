package pl.ug.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import pl.ug.model.log.InfoLog;
import pl.ug.service.LogService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Slf4j
@Component
public class FileBackupConfig implements ApplicationListener<ContextClosedEvent> {

    private static final String FILE_PATH = "logs/application.log";
    private static final String BACKUP_DIR = "logs/";
    private final LogService logService;

    public FileBackupConfig(LogService logService) {
        this.logService = logService;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        try {

            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String zipFileName = BACKUP_DIR + "application_" + timestamp + ".zip";


            try (FileOutputStream fos = new FileOutputStream(zipFileName);
                 ZipOutputStream zos = new ZipOutputStream(fos);
                 FileInputStream fis = new FileInputStream(FILE_PATH)) {

                ZipEntry zipEntry = new ZipEntry(new File(FILE_PATH).getName());
                zos.putNextEntry(zipEntry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) >= 0) {
                    zos.write(buffer, 0, length);
                }
            }

            File originalFile = new File(FILE_PATH);
            if (originalFile.exists() && !originalFile.delete()) {
                log.info("Failed to delete file: " + FILE_PATH);
                logService.saveLog(new InfoLog("Nie udało się usunąć pliku: " + FILE_PATH));
            }

            log.info("The file has been packed and deleted: " + zipFileName);
            logService.saveLog(new InfoLog("The file was packed and deleted: " + zipFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}