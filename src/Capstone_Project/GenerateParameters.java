package Capstone_Project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Diese Klasse stellt eine Datenstruktur zum zufälligen Erstellen eines neuen
 * Labyrinths dar. Es ist abhängig vom gewaehlten Schwireigkeitsgrad und
 * zufälligen Werten für die Eckdaten des Labyrinths.
 *
 * @author Marcel
 */
public class GenerateParameters {

    //Dateipfad zur Parameterdatei, welche die Daten für den Generator haelt.
    private static final String SAVE_FILE = "parameters.txt";

    //Die Eckdaten des zu generierenden Labyrinths
    private int width;
    private int height;
    private int inNr;
    private int outNr;
    private int staticNr;
    private int dynamicNr;
    private int keyNr;
    private double density;

    /**
     * Erstellt die Parameterdatei fuer den Generator und ruft ihn auf
     *
     * @param schwierigkeit , der Schwierigkeitsgrad des Labyrinths
     */
    public GenerateParameters(int schwierigkeit) {
        randomMode(schwierigkeit);
        generateTxt();
        String[] args = new String[0];
        Generate.main(args);
    }

    /**
     * Schreibt die Eckdaten fuer das Labyrinth in die Ausgabedatei
     */
    private void generateTxt() {
        try {
            PrintWriter p = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(new File(SAVE_FILE))));
            p.println("Height=" + height);
            p.println("Width=" + width);
            p.println("NrIn=" + inNr);
            p.println("NrOut=" + outNr);
            p.println("Keys=" + keyNr);
            p.println("StaticTraps=" + staticNr);
            p.println("DynamicTraps=" + dynamicNr);
            p.println("Density=" + density);
            p.close();
        } catch (FileNotFoundException ex) {

        }
    }

    /**
     * Erstellt zufällige Werte für das neue Labyrinth in Abhängigkeit vom
     * Schwierigkeitsgrad
     *
     * @param schwierigkeit , der Schwierigkeitsgrad des Labyrinths
     */
    private void randomMode(int schwierigkeit) {
        width = (int) (Math.random() * 50 + 50) * schwierigkeit;
        height = (int) (Math.random() * 30 + 20) * schwierigkeit;
        inNr = (int) (Math.random() * 4 + 1) * schwierigkeit;
        outNr = (int) (Math.random() * 4 + 1) * schwierigkeit;
        staticNr = (int) (Math.random() * 3 + 1) * schwierigkeit;
        dynamicNr = (int) (Math.random() * 3 + 1) * schwierigkeit;
        keyNr = 2 * schwierigkeit;
        density = Math.random() * 2 + 1 * schwierigkeit;
    }
}
