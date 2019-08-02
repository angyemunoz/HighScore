package com.exercise.highscore;

import java.util.List;

public class Game {

    private List<Gamer> gamersList;
    private Gamer currentGamer;
    private int gamersNumber;
    private int numTop;

    public Game() {
    }


    public Game(List<Gamer> GameresList, int gamersNumber, int numTop) {
        this.gamersList = GameresList;
        this.gamersNumber = gamersNumber;
        this.numTop = numTop;
    }

    public Game(List<Gamer> GameresList, int gamersNumber, int numTop, Gamer currentGamer) {
        this.gamersList = GameresList;
        this.gamersNumber = gamersNumber;
        this.numTop = numTop;
        this.currentGamer = currentGamer;
    }

    public List<Gamer> getGamersList() {
        return gamersList;
    }

    public void setGamersList(List<Gamer> gamersList) {
        this.gamersList = gamersList;
    }

    public int getGamersNumber() {
        return gamersNumber;
    }

    public void setGamersNumber(int gamersNumber) {
        this.gamersNumber = gamersNumber;
    }

    public int getNumTop() {
        return numTop;
    }

    public void setNumTop(int numTop) {
        this.numTop = numTop;
    }

    public Gamer getCurrentGamer() {
        return currentGamer;
    }

    public void setCurrentGamer(Gamer currentGamer) {
        this.currentGamer = currentGamer;
    }
}
