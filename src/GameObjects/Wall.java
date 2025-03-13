/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Klasse zur Repraesentation einer Wand
 * 
 * @author Marcel
 */
public class Wall extends GameObject {

    private static final char FACE = 'W';
    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color BACK_COLOR = Terminal.Color.WHITE;
    private static final Terminal.Color FORE_COLOR = Terminal.Color.DEFAULT;

    /**
     * Erstellen einer neuen Wand und setzen ihrer Position.
     * @param x
     * @param y
     */
    public Wall(int x, int y) {
        super(x, y);
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
        return "Wall";
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
     * Gibt zurück, ob auf das GameObject vom Spieler oder den dynamischen
     * Hindernissen gelaufen werden kann
     *
     * @return , false
     */
    @Override
    public boolean canBeMovedOn() {
        return false;
    }
}
