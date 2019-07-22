package com.exercise.highscore;

public class Jugador implements Comparable<Jugador>{

    private String nombre;
    private String puntaje;


    public Jugador() {
    }

    public Jugador(String nombre, String puntaje) {
        this.nombre = nombre;
        this.puntaje = puntaje;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(String puntaje) {
        this.puntaje = puntaje;
    }

    @Override
    public int compareTo(Jugador o) {
        if(o.getPuntaje() != null)
            return o.getPuntaje().compareTo( this.getPuntaje());
        return -1;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                ", puntaje=" + puntaje +
                '}';
    }
}
