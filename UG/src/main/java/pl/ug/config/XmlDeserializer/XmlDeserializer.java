package pl.ug.config.XmlDeserializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import pl.ug.dto.ComputerDtoXml;
import pl.ug.exception.GeneralAppException;
import pl.ug.mapper.ComputerMapper;
import pl.ug.model.Computer;
import pl.ug.wraper.ComputerListWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
public class XmlDeserializer {

    private final XmlMapper xmlMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public XmlDeserializer() {
        this.xmlMapper = new XmlMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ComputerDtoXml.class, new CustomComputerDtoSerializer());
        module.addDeserializer(LocalDateTime.class, new CustomLocalDateDeserializer());
        xmlMapper.registerModule(module);

        xmlMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public Path xmlFilePath() {
        return Path.of("src", "main", "resources", "computers_export.xml");
    }

    public void writeComputersToFile(List<ComputerDtoXml> computerList, File outputFile) throws IOException {
        ComputerListWrapper wrapper = new ComputerListWrapper(computerList);
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, wrapper);
    }

    public List<ComputerDtoXml> readComputersFromFile(File inputFile) throws IOException {
        if (!inputFile.exists()) {
            throw new IOException("File not found: " + inputFile.getAbsolutePath());
        }

        ComputerListWrapper wrapper = xmlMapper.readValue(inputFile, ComputerListWrapper.class);
        return wrapper.getComputers();
    }

    public synchronized void addComputerToFile(Computer computer) throws IOException {
        try {
            File outputFile = xmlFilePath().toFile();

            if (!outputFile.exists()) {
                if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().mkdirs();
                }
                outputFile.createNewFile(); //
                log.info("Created new XML file: {}", outputFile.getAbsolutePath());
            }

            List<ComputerDtoXml> existingComputers = outputFile.length() > 0
                    ? readComputersFromFile(outputFile)
                    : List.of();

            List<ComputerDtoXml> updatedComputers = new ArrayList<>(existingComputers);
            updatedComputers.add(ComputerMapper.mapComputerToComputerDtoXml(computer));

            updatedComputers.stream()
                    .forEach(e -> {
                        Set<ConstraintViolation<ComputerDtoXml>> violations = validator.validate(e);
                        if (!violations.isEmpty()) {
                            throw new GeneralAppException(
                                    "Validation failed for computer: " + e.name() + ". Violations: " + violations,
                                    HttpStatus.BAD_REQUEST
                            );
                        }
                    });

            writeComputersToFile(updatedComputers, outputFile);
            log.info("Added computer to XML file: {}", outputFile.getAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to write computer to XML file: {}", e.getMessage(), e);
            throw new GeneralAppException("Failed to write computer to XML file", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    private static class CustomComputerDtoSerializer extends StdSerializer<ComputerDtoXml> {

        protected CustomComputerDtoSerializer() {
            super(ComputerDtoXml.class);
        }

        @Override
        public void serialize(ComputerDtoXml value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeStartObject();
            gen.writeStringField("nazwa", value.name());
            gen.writeStringField("data_ksiegowania", value.postingDate().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
            gen.writeNumberField("koszt_USD", value.priceInUsd());
            gen.writeNumberField("koszt_PLN", value.priceInPln());
            gen.writeEndObject();
        }
    }
}