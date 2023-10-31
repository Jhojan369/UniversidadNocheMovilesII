package com.example.universidadmartesnoche;

import android.widget.AdapterView;

public class ListSubjects extends Subjects{

    public ListSubjects(String codigo, String nombreMateria) {
        this.codigo = codigo;
        this.nombreMateria = nombreMateria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombreMateria() {
        return nombreMateria;
    }

    public void setNombreMateria(String nombreMateria) {
        this.nombreMateria = nombreMateria;
    }

}
