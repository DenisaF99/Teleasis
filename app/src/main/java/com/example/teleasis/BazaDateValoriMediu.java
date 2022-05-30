package com.example.teleasis;

public class BazaDateValoriMediu {
    public String getValue_temperatura() {
        return value_temperatura;
    }

    public void setValue_temperatura(String value_temperatura) {
        this.value_temperatura = value_temperatura;
    }

    public String getValue_umiditate() {
        return value_umiditate;
    }

    public void setValue_umiditate(String value_umiditate) {
        this.value_umiditate = value_umiditate;
    }

    public String getValue_prezenta() {
        return value_prezenta;
    }

    public void setValue_prezenta(String value_prezenta) {
        this.value_prezenta = value_prezenta;
    }

    public String getValue_gaz() {
        return value_gaz;
    }

    public void setValue_gaz(String value_gaz) {
        this.value_gaz = value_gaz;
    }

    String value_temperatura, value_umiditate, value_prezenta, value_gaz;
    int id;
    String data;

    public BazaDateValoriMediu() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
