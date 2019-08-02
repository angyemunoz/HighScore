package com.exercise.highscore;

import com.sun.xml.internal.ws.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        List<String> linesTable = linesFile.stream().skip(1).flatMap(line -> Arrays.stream(line.split(" "))).collect(Collectors.toList());
        for (String lineTable: linesTable ) {
            game = calculateDataGame(mainList, game, lineTable);
        }

        fillLastTableGame(mainList, game);

        return mainList;
    }

    private void fillLastTableGame(List<List<Gamer>> mainList, Game game) {
        Collections.sort(game.getGamersList());
        game.getGamersList().stream().forEach(x -> x.setPosition(game.getGamersList().indexOf(x)+1));
        if (isNecessaryCreateEmptyGamersAtTheEnd(game)) {
            createEmptyGamers(game.getGamersList(), game.getNumTop(), game.getGamersList().size());
        }
        mainList.add(game.getGamersList().subList(0, game.getNumTop()));
    }

    private Game calculateDataGame(List<List<Gamer>> mainList, Game game, String lineTable) {

        if (!lineTable.matches(VAL_NUMBER_REGEX)) {
            game.setCurrentGamer(new Gamer());
            game.getCurrentGamer().setName(lineTable);
        } else {
            //if there is no gamers and value top tell us that should be more than a gamer inside the list, we create empty gamers
            this.validateCreationEmptyGamers(game);
            game = restartGameIfItIsNecessary(mainList, game);
            this.assignValuesToGame(game, lineTable);
        }
        return game;
    }

    private Game restartGameIfItIsNecessary(List<List<Gamer>> mainList, Game game) {
        if (isGameReadyToRestart(game, game.getCurrentGamer())) {
            addGameToMainListOrdered(mainList, game);
            game = new Game(new ArrayList<Gamer>(),-1, -1,new Gamer());
        }
        return game;
    }

    private boolean isGameReadyToRestart(Game game, Gamer gamer) {
        return !Objects.isNull(gamer) &&
                !isNegativeNumber.test(game.getGamersNumber()) && !isNegativeNumber.test(game.getNumTop()) && !Objects.isNull(gamer.getScore());
    }

    private void addGameToMainListOrdered(List<List<Gamer>> mainList, Game game) {
        Collections.sort(game.getGamersList());
        game.getGamersList().stream().forEach(x -> x.setPosition(game.getGamersList().indexOf(x)+1));
        mainList.add(game.getGamersList(). subList(0, game.getNumTop()));
    }

    private void assignValuesToGame(Game game, String currentLine) {
        if (isNegativeNumber.test(game.getGamersNumber())) game.setGamersNumber(Integer.parseInt(currentLine));
        else if (isNegativeNumber.test(game.getNumTop())) game.setNumTop(Integer.parseInt(currentLine));
        else {
            game.getCurrentGamer().setScore(currentLine);
            game.getGamersList().add(game.getCurrentGamer());
        }
    }

    private void validateCreationEmptyGamers(Game game) {
        if (isNecessaryCreateEmptyGamers(game)) {
            createEmptyGamers(game.getGamersList(), game.getNumTop(), 0);
            game.setCurrentGamer(Gamer.createDefaultGamer());
        }
    }

    private boolean isNecessaryCreateEmptyGamers(Game game) {
        return (isZero.test(game.getGamersNumber()) && isGreaterThanZero.test(game.getNumTop()));
    }

    private boolean isNecessaryCreateEmptyGamersAtTheEnd(Game game) {
        return game.getGamersList().size() < game.getNumTop();
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

        List<Gamer> gamers = mainList.stream().flatMap(x -> x.stream()).collect(Collectors.toList());

        int lastScore = -1;
        fileLines.add(String.valueOf(cont++));

        for (Gamer gamer : gamers) {
            if (isPossibleIncreasePosition(lastScore, gamer)) gamer.setPosition(gamer.getPosition() - 1);
            fileLines.add(gamer.getPosition() + " " + gamer.getName() + " " + gamer.getScore());
            lastScore = !Objects.isNull(gamer.getScore()) && gamer.getScore().matches(VAL_NUMBER_REGEX) ? Integer.parseInt(gamer.getScore()) : 0;
        }

        return fileLines;
    }

    private boolean isPossibleIncreasePosition(Integer puntajeAnterior, Gamer Gamer) {

        return !Objects.isNull(Gamer.getScore()) &&
                !Predicate.isEqual(puntajeAnterior).test(Gamer.getScore());
    }

    /**
     * Output File is written
     *
     * @param fileLines
     * @throws IOException
     */
    private void writeOutputFile(List<String> fileLines) throws IOException {
        Path path = Files.createTempFile(Paths.get("").toAbsolutePath(), "output", ".txt");
        Files.write(path, fileLines, StandardCharsets.UTF_8);
    }

    /**
     * Creation a empty Gamers List, when there isn't enough gamers to fullfill the Top list
     *
     * @param gamersList
     * @param numTop:       gamers number that list needed
     * @param ini:          gamers number in current list
     */
    private void createEmptyGamers(List<Gamer> gamersList, int numTop, int ini) {
        gamersList.addAll(ini, Stream.generate(Gamer::createDefaultGamer).limit(numTop).collect(Collectors.toList()));

    }

    public static void main(String[] args) {

       /* List<Integer> numbers = Arrays.asList(6, 3, 7, 15, 2, 8, 1, 9, 10);
        int min = numbers.stream().min(Integer::compare).get();
        System.out.println(min);*/
        new HighScore().calculateHighScore();
        //new HighScore().orderNumberList();
    }

    public void orderList(){

        List<Gamer> gamers = createGamers();

        //gamers.stream().sorted(Comparator.comparing(g -> Integer.parseInt(g.getScore()))).forEach(System.out::println);

        gamers.stream().sorted(Comparator.comparing(Gamer::getScore)
                .thenComparing(Gamer::getName
                        )).forEach(System.out::println);
    }

    public void orderNumberList(){
        List<Integer> listNumber = Arrays.asList(5,8,3,4,1,7,0);
        listNumber.stream().sorted().forEach(System.out::println);
        Integer min = listNumber.stream().min(Integer::compare).get();
        Integer max = listNumber.stream().max(Integer::compareTo).get();
        System.out.println("Numero menor "+min);
        System.out.println("Numero mayor "+max);

    }

    private List<Gamer> createGamers() {
        return Arrays.asList(new Gamer("Angye", "9"), new Gamer("Andres", "7"), new Gamer("Angelica", "7") );
    }

    public void getMinValueFromList(){
        List<Gamer> gamers = createGamers();
        //gamers.stream().map(s -> s.getScore()).filter(sco-> Integer.parseInt(sco) > 20).collect(Collectors.toList()).forEach(System.out::println);

        gamers.stream().map(s -> s.getScore()).filter(sco -> Integer.parseInt(sco) > 20).collect(Collectors.toList());
        System.out.println(gamers.stream().min(Comparator.comparing(g -> Integer.parseInt(g.getScore()))).get());
        System.out.println("Valor minimo " + gamers.stream().map(s -> Integer.parseInt(s.getScore())).min(Integer::compareTo).get());
    }

    public void getListNamePerson(){
        List<Persona> personas = Arrays.asList(new Persona("angye", "munoz", 28), new Persona("andres", "bolivar", 32));

        personas.stream().map( persona -> persona.getName()).collect(Collectors.toList()).forEach(System.out::println);

        personas.stream().filter( persona -> persona.getAge() < 35).map(persona -> persona.getAge()).forEach(System.out::println);
    }

    static class Persona{
        private String name;
        private String lastName;
        private int age;

        public Persona(String name, String lastName, int age) {
            this.name = name;
            this.lastName = lastName;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Persona{" +
                    "name='" + name + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}
