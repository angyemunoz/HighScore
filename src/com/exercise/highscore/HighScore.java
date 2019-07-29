package com.exercise.highscore;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class HighScore {

    public static final String VAL_NUMBER_REGEX = "-?\\d+(\\.\\d+)?";
    private Predicate<Integer> isNegativeNumber = p -> p < 0;
    private Predicate<Integer> isZero = p -> p == 0;
    private Predicate<Integer> isGreaterThanZero = p -> p > 0;


    public void calculateHighScore() {

        try {
            List<String> linesFile = this.readFile();
            List<List<Gamer>> mainList = this.calculateHighScore(linesFile);
            List<String> outputLines = this.defineOutputLines(mainList);
            this.writeOutputFile(outputLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readFile() {

        try {
            Path file = Paths.get(Paths.get("").toAbsolutePath().toString(), "input.txt");
            return Files.readAllLines(file);

        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método que realiza los calculos de las tablas con los puntajes más altos
     * @param linesFile
     * @return
     */
    public List<List<Gamer>> calculateHighScore(List<String> linesFile) {

        List<List<Gamer>> mainList = new ArrayList<>();
        Game game = new Game(new ArrayList<Gamer>(),-1, -1);
        List<String> linesTableGame = linesFile.stream().skip(1).collect(Collectors.toList());

        for (String lineTableGame : linesTableGame) {
            StringTokenizer tokenizer = new StringTokenizer(lineTableGame);
            Gamer gamer = new Gamer();
            while (tokenizer.hasMoreElements()) {
                //Si el valor del token es un string, se trata del nombre del gamer
                game = calculateDataGame(mainList, game, gamer, tokenizer.nextToken());
            }
        }

        Collections.sort(game.getGamersList());
        if (game.getGamersList().size() < game.getNumTop()) {
            createEmptyGamers(game.getGamersList(), game.getNumTop(), game.getGamersList().size());
        }
        mainList.add(game.getGamersList().subList(0, game.getNumTop()));

        return mainList;
    }

    private Game calculateDataGame(List<List<Gamer>> mainList, Game game, Gamer gamer, String nextToken) {
        if (!nextToken.matches(VAL_NUMBER_REGEX)) {
            gamer.setName(nextToken);
        } else {
            //si no hay Gameres y el top indica que debe haber mas de un gamer en la lista, se crean Gameres vacios
            this.validateCreationEmptyGamers(game.getGamersList(), game.getGamersNumber(), game.getNumTop());

            //se reinician los datos
            game = restartGame(mainList, game, gamer);
            this.asignValueToGame(game, gamer, nextToken);
        }
        return game;
    }


    private Game restartGame(List<List<Gamer>> mainList, Game game, Gamer Gamer) {
        if (isGameReadyToRestart(game, Gamer)) {
            addGamerToMainListOrdered(mainList, game);
            game = new Game(new ArrayList<Gamer>(),-1, -1);
        }
        return game;
    }

    private boolean isGameReadyToRestart(Game game, Gamer Gamer) {
        return !isNegativeNumber.test(game.getGamersNumber()) && !isNegativeNumber.test(game.getNumTop()) && Objects.isNull(Gamer.getName());
    }

    private void addGamerToMainListOrdered(List<List<Gamer>> mainList, Game game) {
        Collections.sort(game.getGamersList());
        mainList.add(game.getGamersList().subList(0, game.getNumTop()));
    }

    private void asignValueToGame(Game game, Gamer Gamer, String nextToken) {
        if (isNegativeNumber.test(game.getGamersNumber())) game.setGamersNumber(Integer.parseInt(nextToken));
        else if (isNegativeNumber.test(game.getNumTop())) game.setNumTop(Integer.parseInt(nextToken));
        else {
            Gamer.setScore(nextToken);
            game.getGamersList().add(Gamer);
        }
    }

    private void validateCreationEmptyGamers(List<Gamer> GameresList, int gamersNumber, int numTop) {
        if (isNecesaryCreateEmptyGamers(gamersNumber, numTop)) {
            createEmptyGamers(GameresList, numTop, 0);
        }
    }

    private boolean isNecesaryCreateEmptyGamers(int gamersNumber, int numTop) {
        return isZero.test(gamersNumber) && isGreaterThanZero.test(numTop);
    }

    /**
     * Apartir de la lista principal, se crea un arraylist que contiene el contenido del
     * archivo de salida linea a linea
     *
     * @param mainList
     * @return
     * @throws IOException
     */
    private List<String> defineOutputLines(List<List<Gamer>> mainList) throws IOException {
        List<String> fileLines = new ArrayList<>();
        int cont = 1;
        for (List<Gamer> gamers : mainList) {

            int pos = 1, lastScore = -1;
            fileLines.add(String.valueOf(cont++));

            for (Gamer gamer : gamers) {
                if (isPossibleIncreasePosition(lastScore, gamer)) pos++;
                //if (!Objects.isNull(Gamer.getScore()) && !puntajeAnterior.equals(Gamer.getScore())) pos++;
                fileLines.add(pos + " " + gamer.getName() + " " + gamer.getScore());
                lastScore = !Objects.isNull(gamer.getScore()) && gamer.getScore().matches(VAL_NUMBER_REGEX) ? Integer.parseInt(gamer.getScore()) : 0;
            }
        }

        return fileLines;
    }

    private boolean isPossibleIncreasePosition(Integer puntajeAnterior, Gamer Gamer) {
        return !Objects.isNull(Gamer.getScore()) &&
                !Predicate.isEqual(puntajeAnterior).test(Gamer.getScore());
    }

    /**
     * Se escribe el archivo de salida
     *
     * @param fileLines
     * @throws IOException
     */
    private void writeOutputFile(List<String> fileLines) throws IOException {
        Path path = Files.createTempFile(Paths.get("").toAbsolutePath(), "output", ".txt");
        Files.write(path, fileLines, StandardCharsets.UTF_8);
    }

    /**
     * Se crea una lista de Gameres vacios, cuando no existen Gameres para completar la lista Top
     *
     * @param gamersList
     * @param numTop:       numero de Gameres que necesita la lista
     * @param ini:          numero de Gameres en la lista actual
     */
    private void createEmptyGamers(List<Gamer> gamersList, int numTop, int ini) {
        for (int i = ini; i < numTop; i++) {
            gamersList.add(new Gamer("***", "***"));
        }
    }

    public static void main(String[] args) {
        new HighScore().calculateHighScore();
    }

}
