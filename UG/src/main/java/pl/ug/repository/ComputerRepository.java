package pl.ug.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.ug.model.Computer;

import java.time.LocalDateTime;
import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, Long> {


    @Query("SELECT c FROM Computer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :fragment, '%'))")
    List<Computer> searchByNameFragment(@Param("fragment") String fragment);

    @Query("""
            SELECT c FROM Computer c 
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :fragment, '%')) 
            AND c.postingDate BETWEEN :dateFrom AND :dateTo
            """)
    List<Computer> searchByNameFragmentAndDateRangePosting(
            @Param("fragment") String fragment,
            @Param("dateFrom") LocalDateTime dateFrom,
            @Param("dateTo") LocalDateTime dateTo
    );
}
