/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;

/**
 * Die Oberklasse fuer alle Spielobjekte
 *
 * @author Marcel
 */
public abstract class GameObject {

    //x und y Koordinaten des GameObjects
    private int x, y;

    /**
     * Erstellen eines neuen GameObjects und setzen seiner Position aus x und y
     * Koordinaten.
     * @param x
     * @param y
     */
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Erstellen eines neuen Schluessels und setzen seiner Position an der
     * Stelle des uebergebenen GameObjects.
     * @param other
     */
    public GameObject(GameObject other) {
        this.x = other.x;
        this.y = other.y;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        if (this.x + x < 0) {
            return;
        }
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        if (this.y + y < 0) {
            return;
        }
        this.y = y;
    }

    /**
     * Setzt die komplette Position des GameObjects (x und y Position)
     *
     * @param x
     * @param y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Ausgabe des Zeichens, welches das GameObject auf dem Terminal darstellt
     *
     * @return, das Zeichen
     */
    public abstract char print();

    /**
     * Ausgabe der Hintergrundfarbe
     *
     * @return die Hintergrundfarbe
     */
    public abstract Terminal.Color getBackGround();

    /**
     * Ausgabe der Vordergrundfarbe
     *
     * @return die Vordergrundfarbe
     */
    public abstract Terminal.Color getForeGround();

    /**
     * Gibt den Typ des GameObjects als String zurück
     *
     * @return, den Typ
     */
    public abstract String getTyp();

    /**
     * Prueft, ob das uebergebene GameObject an der selben Position ist wie das
     * betrachtete GameObject
     *
     * @param other
     * @return
     */
    public boolean atSamePositionAs(GameObject other) {
        return (this.x == other.getX() && this.y == other.getY());
    }

    /**
     * Berechnet die Entfernung (Luftlinie) zum uebergebenen GameObject
     *
     * @param other, das GameObject, dessen Entfernung berechnet wird
     * @return , die Entfernung
     */
    public double computeDistance(GameObject other) {
        return Math.sqrt(Math.pow(other.getX() - x, 2) + Math.pow(other.getY() - y, 2));
    }

    /**
     * Gibt zurück, ob auf das GameObject vom Spieler oder den dynamischen
     * Hindernissen gelaufen werden kann
     *
     * @return , standartmaessig true, wird in nicht betretbaren Klassen
     * ueberschrieben
     */
    public boolean canBeMovedOn() {
        return true;
    }
}
