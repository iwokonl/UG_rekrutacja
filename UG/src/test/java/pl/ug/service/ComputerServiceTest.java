package pl.ug.service;

import org.junit.jupiter.api.Test;
import pl.ug.config.XmlDeserializer.XmlDeserializer;
import pl.ug.dto.ComputerDto;
import pl.ug.exception.GeneralAppException;
import pl.ug.model.Computer;
import pl.ug.repository.ComputerRepository;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ComputerServiceTest {


    @Test
    void shouldThrowExceptionWhenNoComputersMatchCriteria() {
        ComputerRepository mockRepository = mock(ComputerRepository.class);
        XmlDeserializer mockXmlDeserializer = mock(XmlDeserializer.class);
        Path mockPath = mock(Path.class);
        NbpApiService mockNbpApiService = mock(NbpApiService.class);
        ComputerService service = new ComputerService(mockRepository, mockXmlDeserializer, mockNbpApiService, mockPath);

        when(mockRepository.searchByNameFragmentAndDateRangePosting(anyString(), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        GeneralAppException exception = assertThrows(GeneralAppException.class, () ->
                service.searchByNameFragmentAndDateRangePosting("NonExistent", LocalDate.now(), LocalDate.now())
        );

        assertEquals("No computer with the specified name found", exception.getMessage());
    }

    @Test
    void shouldReturnComputersMatchingNameAndDateRange() {
        ComputerRepository mockRepository = mock(ComputerRepository.class);
        XmlDeserializer mockXmlDeserializer = mock(XmlDeserializer.class);
        Path mockPath = mock(Path.class);
        NbpApiService mockNbpApiService = mock(NbpApiService.class);
        ComputerService service = new ComputerService(mockRepository, mockXmlDeserializer, mockNbpApiService, mockPath);

        Computer computer = new Computer();
        computer.setName("TestComputer");
        computer.setCurrencyConversionDate(LocalDate.now().atStartOfDay());

        when(mockRepository.searchByNameFragmentAndDateRangePosting(eq("Test"), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(computer));

        List<ComputerDto> result = service.searchByNameFragmentAndDateRangePosting("Test", LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        assertEquals(1, result.size());
        assertEquals("TestComputer", result.getFirst().name());
    }

    @Test
    void shouldHandleNullNameAndReturnAllComputersInDateRange() {
        ComputerRepository mockRepository = mock(ComputerRepository.class);
        XmlDeserializer mockXmlDeserializer = mock(XmlDeserializer.class);
        Path mockPath = mock(Path.class);
        NbpApiService mockNbpApiService = mock(NbpApiService.class);
        ComputerService service = new ComputerService(mockRepository, mockXmlDeserializer, mockNbpApiService, mockPath);

        Computer computer = new Computer();
        computer.setName("AnyComputer");
        computer.setCurrencyConversionDate(LocalDate.now().atStartOfDay());

        when(mockRepository.searchByNameFragmentAndDateRangePosting(eq(""), any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(computer));

        List<ComputerDto> result = service.searchByNameFragmentAndDateRangePosting(null, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        assertEquals(1, result.size());
        assertEquals("AnyComputer", result.getFirst().name());
    }
}