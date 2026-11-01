package org.example.export.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import org.example.model.ActividadSocio;

import java.util.ArrayList;
import java.util.List;

public class Historial {
    public String documentoSocio;
    public String fechaDesde;
    public String fechaHasta;

    @JacksonXmlElementWrapper(localName = "actividades")
    @JacksonXmlProperty(localName = "actividad")
    public List<ActividadSocio> actividades = new ArrayList<>();
}
