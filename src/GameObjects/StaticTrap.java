/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Klasse zur Repraesentation eines statischen Hindernis
 * 
 * @author Marcel
 */
public class StaticTrap extends GameObject {

    //Das Zeichen, welches das GameObject auf dem Terminal repräsentiert
    private static final char FACE = 'S';
    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color BACK_COLOR = Terminal.Color.MAGENTA;
    //Vordergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color FORE_COLOR = Terminal.Color.CYAN;

    /**
     * Erstellen eines neuen statischen Hindernis und setzen seiner Position.
     * @param x
     * @param y
     */
    public StaticTrap(int x, int y) {
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
        return "StaticTrap";
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
