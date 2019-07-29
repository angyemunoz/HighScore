package com.exercise.highscore;

import java.util.List;

public class Game {

    private List<Gamer> gamersList;
    private int gamersNumber;
    private int numTop;

    public Game() {
    }

    public Game(List<Gamer> GameresList, int gamersNumber, int numTop) {
        this.gamersList = GameresList;
        this.gamersNumber = gamersNumber;
        this.numTop = numTop;
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
}
