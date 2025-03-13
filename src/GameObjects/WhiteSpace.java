/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Die Klasse zur Speicherung und Darstellung eines Leeren Felds im Spielfeld
 *
 * @author Marcel
 */
public class WhiteSpace extends GameObject {

    //Das Zeichen, welches das GameObject auf dem Terminal repräsentiert
    private static final char FACE = ' ';
    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private Terminal.Color BACK_COLOR = Terminal.Color.DEFAULT;
    //Vordergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color FORE_COLOR = Terminal.Color.DEFAULT;

    /**
     * Konstruktor über die x und y Koordinaten der Position
     *
     * @param x
     * @param y
     */
    public WhiteSpace(int x, int y) {
        super(x, y);
    }

    /**
     * Konstruktor über die Position eines anderen GameObjects
     *
     * @param other
     */
    public WhiteSpace(GameObject other) {
        super(other);
    }

    /**
     * Ausgabe des Zeichens, welches das GameObject auf dem Terminal darstellt
     *
     * @return, das Zeichen
     */
    @Override
    public char print() {
        return FACE;
    }

    /**
     * Gibt den Typ des GameObjects als String zurück
     *
     * @return, den Typ
     */
    @Override
    public String getTyp() {
        return "WhiteSpace";
    }

    /**
     * Ausgabe der Hintergrundfarbe
     *
     * @return die Hintergrundfarbe
     */
    @Override
    public Terminal.Color getBackGround() {
        return BACK_COLOR;
    }

    /**
     * Ausgabe der Vordergrundfarbe
     *
     * @return die Vordergrundfarbe
     */
    @Override
    public Terminal.Color getForeGround() {
        return FORE_COLOR;
    }

    /**
     * Aendert die Farbe der freien Flaeche zum Speichern, ob sie schon betreten
     * wurde
     */
    public void setMovedOn() {
        BACK_COLOR = Terminal.Color.GREEN;
    }
}
