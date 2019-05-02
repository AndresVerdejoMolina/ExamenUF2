package com.example.examenuf2;

public class Incidencia {

    public String descripcion;
    public String urlFoto;
    public String aula;

    public Incidencia(){}

    public Incidencia(String descripcion, String urlFoto, String aula){
        this.aula=aula;
        this.descripcion=descripcion;
        this.urlFoto=urlFoto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getAula() {
        return aula;
    }

    public void setAula(String aula) {
        this.aula = aula;
    }


}
