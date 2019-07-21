import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HighScore {

    public void calculateHighScore() {

        try {
            List<String> linesFile = this.readFile();
            List<List<Jugador>> mainList = this.calculateHighScore(linesFile);
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
    public List<List<Jugador>> calculateHighScore(List<String> linesFile) {

        List<List<Jugador>> mainList = new ArrayList<>();
        StringTokenizer tokenizer;
        Pattern pattern = Pattern.compile("([A-Z])");
        List<Jugador> jugadoresList = new ArrayList<>();
        int gamersNumber = -1, numTop = -1, cont = 0;
        for (String line : linesFile) {
            cont++;
            if (cont == 1) {
                continue;
            }
            tokenizer = new StringTokenizer(line);
            Jugador jugador = new Jugador();
            while (tokenizer.hasMoreElements()) {
                String nextToken = tokenizer.nextToken();
                Matcher matcher = pattern.matcher(nextToken);
                //Si el valor del token es un string, se trata del nombre del jugador
                if (matcher.find()) {
                    jugador.setNombre(nextToken);
                } else {
                    //se reinician los datos
                    //si no hay jugadores y el top indica que debe haber mas de un jugador en la lista, se crean jugadores vacios
                    if (gamersNumber == 0 && numTop > 0) {
                        createEmptyGamers(jugadoresList, numTop, 0);
                    }
                    if (gamersNumber != -1 && numTop != -1 && jugador.getNombre() == null) {
                        Collections.sort(jugadoresList);
                        mainList.add(jugadoresList.subList(0, numTop));
                        jugadoresList = new ArrayList<>();
                        gamersNumber = -1;
                        numTop = -1;
                    }
                    if (gamersNumber == -1) gamersNumber = Integer.parseInt(nextToken);
                    else if (numTop == -1) numTop = Integer.parseInt(nextToken);
                    else {
                        jugador.setPuntaje(nextToken);
                        jugadoresList.add(jugador);
                    }

                }
            }
        }

        Collections.sort(jugadoresList);
        if (jugadoresList.size() < numTop) {
            createEmptyGamers(jugadoresList, numTop, jugadoresList.size());
        }
        mainList.add(jugadoresList.subList(0, numTop));

        return mainList;
    }

    /**
     * Apartir de la lista principal, se crea un arraylist que contiene el contenido del
     * archivo de salida linea a linea
     *
     * @param mainList
     * @return
     * @throws IOException
     */
    private List<String> defineOutputLines(List<List<Jugador>> mainList) throws IOException {
        List<String> fileLines = new ArrayList<>();
        int cont = 1;
        for (List<Jugador> jugadores : mainList) {

            int pos = 1;
            Integer puntajeAnterior = -1;
            fileLines.add(String.valueOf(cont++));

            for (Jugador jugador : jugadores) {
                if (jugador.getPuntaje() != null && !puntajeAnterior.equals(jugador.getPuntaje())) pos++;
                fileLines.add(pos + " " + jugador.getNombre() + " " + jugador.getPuntaje());
                puntajeAnterior = jugador.getPuntaje() != null && jugador.getPuntaje().matches("-?\\d+(\\.\\d+)?") ? Integer.parseInt(jugador.getPuntaje()) : 0;
            }
        }

        return fileLines;
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
     * Se crea una lista de jugadores vacios, cuando no existen jugadores para completar la lista Top
     *
     * @param jugadoresList
     * @param numTop:       numero de jugadores que necesita la lista
     * @param ini:          numero de jugadores en la lista actual
     */
    private void createEmptyGamers(List<Jugador> jugadoresList, int numTop, int ini) {
        for (int i = ini; i < numTop; i++) {
            jugadoresList.add(new Jugador("*** ***", ""));
        }
    }

    public static void main(String[] args) {
        new HighScore().calculateHighScore();
    }

}
