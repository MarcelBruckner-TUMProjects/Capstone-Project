/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Datenstruktur zur Repräsentation der dynamischen Hindernisse
 *
 * @author Marcel
 */
public class DynamicTrap extends GameObject {

    //Das Zeichen, welches das PowerUp auf dem Terminal repräsentiert
    private static final char FACE = 'D';
    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color BACK_COLOR = Terminal.Color.RED;
    //Vordergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color FORE_COLOR = Terminal.Color.GREEN;

    //Zeit zwischen den Bewegungen der dynamischen Hindernissen
    private static int verzoegerung = 200;

    //Momentane Bewegungsrichtung des Hindernis
    private MoveDirection moveDirection = MoveDirection.UP;

    /**
     * Erstellen eines neuen dynamischen Hindernis und setzen seiner Position.
     * @param x
     * @param y
     */
    public DynamicTrap(int x, int y) {
        super(x, y);
    }

    /**
     * Gibt die momentane Bewegungsrichtung zurück
     *
     * @return , die momentane Bewegungsrichtung
     */
    public MoveDirection getMoveDirection() {
        return this.moveDirection;
    }

    /**
     * Ausgabe des Zeichens, welches das PowerUp auf dem Terminal darstellt
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
        return "DynamicTrap";
    }

    /**
     * @param moveDirection the moveDirection to set
     */
    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    /**
     * Setzt die Bewegungsrichtung des dynamischen Hindernis auf den Kehrwert
     */
    public void reverseDirection() {
        switch (moveDirection) {
            case UP:
                moveDirection = MoveDirection.DOWN;
                break;
            case DOWN:
                moveDirection = MoveDirection.UP;
                break;
            case LEFT:
                moveDirection = MoveDirection.RIGHT;
                break;
            case RIGHT:
                moveDirection = MoveDirection.LEFT;
                break;
        }
    }

    /**
     * Gibt die Zeit zwischen den Schritten der dynamischen Hindernisse zurück
     *
     * @return
     */
    public static int getVerzoegerung() {
        return verzoegerung;
    }

    /**
     * Veraendert die Geschwindigkeit der dynamischen Hindernisse
     *
     * @param aenderung, die Aenderung der zeitlichen Abstände zwischen den
     * Schritten
     */
    public static void beschleunige(int aenderung) {
        if (verzoegerung - aenderung > 10) {
            verzoegerung -= aenderung;
        } else {
            beschleunige(aenderung / 2);
        }
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
