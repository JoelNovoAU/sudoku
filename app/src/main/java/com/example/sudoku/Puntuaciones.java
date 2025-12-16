package com.example.sudoku;

public class Puntuaciones {

    private int minutos;
    private int segundos;
    private int dificultad;
    private String nombre;
    public Puntuaciones(String nombre,int minutos, int segundos, int dificultad) {
        this.nombre = nombre;
        this.minutos = minutos;
        this.segundos = segundos;
        this.dificultad = dificultad;
    }

    public String getNombre() { return nombre; }

    public int getMinutos() { return minutos; }
    public int getSegundos() { return segundos; }
    public int getDificultad() { return dificultad; }

    public String getTiempoFormateado() {
        return String.format("%02d:%02d", minutos, segundos);
    }

    public String getDificultadTexto() {
        switch (dificultad) {
            case 1: return "Fácil";
            case 2: return "Medio";
            case 3: return "Difícil";
        }
        return "N/A";
    }
}
