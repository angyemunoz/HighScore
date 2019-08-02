package com.exercise.highscore;

public class Gamer implements Comparable<Gamer>{

    private String name;
    private String score;
    private int position;


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

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public static Gamer createDefaultGamer(){
        return new Gamer("***","***");
    }
}
