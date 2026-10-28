package org.example.export.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.ArrayList;
import java.util.List;

public class Estadisticas {
    public String fechaDesde;
    public String fechaHasta;

    @JacksonXmlElementWrapper(localName = "datos")
    @JacksonXmlProperty(localName = "estadistica")
    public List<DatoEstadistica> datos = new ArrayList<DatoEstadistica>();
}