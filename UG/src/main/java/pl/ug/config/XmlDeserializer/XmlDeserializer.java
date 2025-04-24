package pl.ug.config.XmlDeserializer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.ug.dto.ComputerDtoXml;
import pl.ug.wraper.ComputerListWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class XmlDeserializer {

    private final XmlMapper xmlMapper;

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