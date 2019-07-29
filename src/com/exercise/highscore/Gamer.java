package com.exercise.highscore;

public class Gamer implements Comparable<Gamer>{

    private String name;
    private String score;


    public Gamer() {
    }

    public Gamer(String name, String score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int compareTo(Gamer o) {
        if(o.getScore() != null)
            return o.getScore().compareTo( this.getScore());
        return -1;
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + name + '\'' +
                ", puntaje=" + score +
                '}';
    }
}
