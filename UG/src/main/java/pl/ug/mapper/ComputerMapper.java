package pl.ug.mapper;


import pl.ug.dto.ComputerDto;
import pl.ug.dto.ComputerDtoXml;
import pl.ug.dto.CreateComputerDto;
import pl.ug.model.Computer;

public class ComputerMapper {


    public static ComputerDtoXml mapComputerToComputerDtoXml(Computer computer) {
        return new ComputerDtoXml(
                computer.getId(),
                computer.getName(),
                computer.getPostingDate(),
                computer.getCurrencyConversionDate(),
                computer.getPriceInPln(),
                computer.getPriceInUsd()
        );
    }

    public static ComputerDto mapComputerToComputerDto(Computer computer) {
        return new ComputerDto(
                computer.getId(),
                computer.getName(),
                computer.getPostingDate(),
                computer.getCurrencyConversionDate(),
                computer.getPriceInPln(),
                computer.getPriceInUsd()
        );
    }

    public static CreateComputerDto mapComputerToCreateComputerDto(Computer computer) {
        return new CreateComputerDto(
                computer.getName(),
                computer.getPostingDate(),
                computer.getCurrencyConversionDate(),
                computer.getPriceInUsd()
        );
    }

    public static Computer mapComputerDtoToComputer(ComputerDto computerDto) {
        return new Computer(
                computerDto.id(),
                computerDto.name(),
                computerDto.postingDate(),
                computerDto.currencyConversionDate(),
                computerDto.priceInPln(),
                computerDto.priceInUsd()
        );
    }

    public static Computer mapCreateComputerDtoToComputer(CreateComputerDto createComputerDto) {
        return new Computer(
                null,
                createComputerDto.name(),
                createComputerDto.postingDate(),
                createComputerDto.currencyConversionDate(),
                null,
                createComputerDto.priceInUsd()
        );
    }

}
