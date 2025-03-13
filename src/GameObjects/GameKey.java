/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Datenstruktur zur Repräsentation der Schluessel
 *
 * @author Marcel
 */
public class GameKey extends GameObject {

    //Das Zeichen, welches das GameObject auf dem Terminal repräsentiert
    private static final char FACE = 'K';
    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color BACK_COLOR = Terminal.Color.YELLOW;
    //Vordergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color FORE_COLOR = Terminal.Color.DEFAULT;

    /**
     * Erstellen eines neuen Schluessels und setzen seiner Position.
     * @param x
     * @param y
     */
    public GameKey(int x, int y) {
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
        return "Key";
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
}
