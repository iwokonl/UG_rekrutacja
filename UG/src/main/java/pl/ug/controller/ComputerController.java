package pl.ug.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.ug.dto.ComputerDto;
import pl.ug.dto.CreateComputerDto;
import pl.ug.service.ComputerService;

import java.time.LocalDate;
import java.util.List;

@RequestMapping("/computer")
@RequiredArgsConstructor
@RestController
public class ComputerController {

    private final ComputerService computerService;

    @PostMapping("/saveComputer")
    public ResponseEntity<ComputerDto> saveComputer(@RequestBody @Valid CreateComputerDto createComputerDto) {
        return new ResponseEntity<>(computerService.saveComputer(createComputerDto), HttpStatus.CREATED);
    }

    @GetMapping("/searchByNameFragmentAndDateRangePosting")
    public ResponseEntity<List<ComputerDto>> searchByNameFragmentAndDateRangePosting(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "dateFrom", required = false) LocalDate dateFrom,
            @RequestParam(value = "dateTo", required = false) LocalDate dateTo) {

        return ResponseEntity.ok(computerService.searchByNameFragmentAndDateRangePosting(name, dateFrom, dateTo));
    }

}
