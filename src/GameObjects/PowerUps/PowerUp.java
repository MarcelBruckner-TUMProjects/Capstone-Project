/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.PowerUps;

import GameObjects.GameObject;
import com.googlecode.lanterna.terminal.Terminal;

/**
 * Die Klasse zur Darstellung und Speicherung von PowerUps
 *
 * @author Marcel
 */
public class PowerUp extends GameObject {

    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color BACK_COLOR = Terminal.Color.GREEN;
    //Vordergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color FORE_COLOR = Terminal.Color.DEFAULT;
    //Das Zeichen, welches das PowerUp auf dem Terminal repräsentiert
    private static final char FACE = 'U';
    //Der Typ des PowerUps
    private final PowerUpTyp typ;

    /**
     * Konstruktor über die x und y Koordinaten der Position und des Typs
     * @param x
     * @param y
     * @param typ
     */
    public PowerUp(int x, int y, PowerUpTyp typ) {
        super(x, y);
        this.typ = typ;
    }

    /**
     * Konstruktor über die Position eines anderen GameObjects und des Typs
     * @param other
     * @param typ
     */
    public PowerUp(GameObject other, PowerUpTyp typ) {
        super(other);
        this.typ = typ;
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
        return "PowerUp";
    }

    /**
     * Ausgabe des speziellen Typs des PowerUps
     *
     * @return den PowerUptyp
     */
    public PowerUpTyp getPowerUpTyp() {
        return typ;
    }

    /**
     * Berechnung des PowerUptyps ausgehend von der Zahlendarstellung im
     * Typ-Enum
     *
     * @param o ,der Zahlenwert des Typs
     * @return , den Typ des PowerUps
     */
    public static PowerUpTyp typFromOrdinal(char o) {
        switch (o) {
            case '0':
                return PowerUpTyp.DOUBLE_HP;
            case '1':
                return PowerUpTyp.DYNAMICS_SPEED_UP;
            case '2':
                return PowerUpTyp.DYNAMICS_SPEED_DOWN;
            case '3':
                return PowerUpTyp.HALF_HP;
            case '4':
                return PowerUpTyp.HINT;
            case '5':
                return PowerUpTyp.HP_UP;
            case '6':
                return PowerUpTyp.LOSE_KEY;
            case '7':
                return PowerUpTyp.PACMAN;
            case '8':
                return PowerUpTyp.SHIELD;
            default:
                return PowerUpTyp.PATH;
        }
    }
}
