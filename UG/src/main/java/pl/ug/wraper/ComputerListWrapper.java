package pl.ug.wraper;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import pl.ug.dto.ComputerDtoXml;

import java.util.List;

@JacksonXmlRootElement(localName = "faktura")
public class ComputerListWrapper {

    @JacksonXmlElementWrapper(useWrapping = false)
    @com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty(localName = "komputer")
    private List<ComputerDtoXml> computers;

    public ComputerListWrapper() {
    }

    public ComputerListWrapper(List<ComputerDtoXml> computers) {
        this.computers = computers;
    }

    public List<ComputerDtoXml> getComputers() {
        return computers;
    }

    public void setComputers(List<ComputerDtoXml> computers) {
        this.computers = computers;
    }
}