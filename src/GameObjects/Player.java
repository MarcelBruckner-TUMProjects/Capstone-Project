/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects;

import com.googlecode.lanterna.terminal.Terminal;
import java.util.Date;

/**
 * Die Klasse, die den Spieler darstellt
 *
 * @author Marcel
 */
public class Player extends GameObject {

    //Das Zeichen, welches das GameObject auf dem Terminal repräsentiert
    private static final char FACE = 'P';
    //Hintergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color BACK_COLOR = Terminal.Color.CYAN;
    //Vordergrundfarbe beim Ausgeben auf dem Terminal
    private static final Terminal.Color FORE_COLOR = Terminal.Color.RED;
    //Farbe, wenn das PacMan PowerUp aktiv ist    
    private static final Terminal.Color PAC_MAN = Terminal.Color.MAGENTA;
    //Ist ein Schluessel eingesammelt
    private boolean keyCollected = false;
    //Hat der Spieler das Unverwundbar PowerUp eingesammelt und ist es aktiv
    private boolean invincible = false;
    //Hat der Spieler das PowerUp zum Pfad markieren eingesammelt
    private boolean isPathMark = false;
    //Hat der Spieler das PacMan PowerUp eingesammelt
    private boolean pacman = false;
    //Anzahl der Leben des Spielers
    private int lives = 3;
    //Letzter Zeitpunkt, an dem ein Leben verloren wurde. Der Spieler ist nach dem verlieren eines Lebens einen kurzen Moment unverwundbar
    private Date lastLiveLost = new Date();
    //Zeitpunkt, wann das Unverwundbar PowerUp eingesammelt wurde
    private Date becameInvincible;
    //Zeitpunkt, wann das PacMan PowerUp eingesammelt wurde
    private Date becamePacman;
    //Ist ein Hinweis eingesammelt
    private boolean hintCollected = false;

    /**
     * Erstellen eines neuen Players und setzen seiner Position.
     *
     * @param x
     * @param y
     */
    public Player(int x, int y) {
        super(x, y);
    }

    /**
     * Konstruktor über die Position eines anderen GameObjects
     *
     * @param other
     */
    public Player(GameObject other) {
        super(other);
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
        return "Player";
    }

    /**
     * @return the keyCollected
     */
    public boolean isKeyCollected() {
        return keyCollected;
    }

    /**
     * @param keyCollected the keyCollected to set
     */
    public void setKeyCollected(boolean keyCollected) {
        this.keyCollected = keyCollected;
    }

    /**
     * @return the lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * @param lives the lives to set
     */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /**
     * Der Spieler erhaelt ein Leben.
     */
    public void gainLive() {
        lives++;
    }

    /**
     * Der Spieler verliert ein Leben, solange er nicht unverwundbar ist
     */
    public void loseLive() {
        if (invincible) {
            return;
        }
        this.lives--;
        lastLiveLost = new Date();
    }

    /**
     * Verdoppelt das Leben des Spielers
     */
    public void doubleLives() {
        lives *= 2;
    }

    /**
     * Halbiert das Leben des Spielers. Rundet ggf. auf
     */
    public void halfLives() {
        lives = (int) Math.ceil(((double) lives) / 2);
    }

    /**
     * Methode zum Pruefen, ob der Spieler kein Leben mehr besitzt
     *
     * @return, true, wenn keine Leben mehr vorhanden sind
     */
    public boolean isDead() {
        return lives <= 0;
    }

    /**
     * @return the lastLiveLost
     */
    public long getLastLiveLost() {
        return lastLiveLost.getTime();
    }

    /**
     * Der Spieler wird unverwundbar und der Zeipunkt wird gespeichert
     */
    public void becomeInvincible() {
        this.invincible = true;
        becameInvincible = new Date();
    }

    /**
     * @return the invincible
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Der Spieler ist nicht mehr unverwundbar
     */
    public void loseInvincible() {
        invincible = false;
    }

    /**
     * @return the becameInvincible
     */
    public Date getBecameInvincible() {
        return becameInvincible;
    }

    /**
     * @return the hintCollected
     */
    public boolean isHintCollected() {
        return hintCollected;
    }

    /**
     * Der Spieler erhaelt einen Hinweis
     */
    public void collectHint() {
        this.hintCollected = true;
    }

    /**
     * Der Spieler setzt einen Hinweis ein. Es bleibt keiner uebrig
     */
    public void useHint() {
        this.hintCollected = false;
    }

    /**
     * Der Spieler wird zum PacMan und unverwundbar. Der Zeitpunkt wird
     * gespeichert.
     */
    public void becomePacman() {
        this.pacman = true;
        this.invincible = true;
        becamePacman = new Date();
    }

    /**
     * Gibt den Zeitpunkt zurueck, an dem das PacMan PowerUp gefunden wurde
     *
     * @return , den Zeitpunkt
     */
    public Date becamePacman() {
        return becamePacman;
    }

    /**
     * Der Spieler verliert das PacMan PowerUp
     */
    public void losePacman() {
        this.pacman = false;
        this.invincible = false;
    }

    /**
     * Gibt zurueck, ob das PacMan PowerUp aktiv ist
     *
     * @return true, wenn es aktiv ist
     */
    public boolean isPacman() {
        return pacman;
    }

    /**
     * Ausgabe der Hintergrundfarbe
     *
     * @return die Hintergrundfarbe
     */
    @Override
    public Terminal.Color getBackGround() {
        if (pacman) {
            return PAC_MAN;
        } else if (invincible) {
            return FORE_COLOR;
        } else {
            return BACK_COLOR;
        }
    }

    /**
     * Ausgabe der Vordergrundfarbe
     *
     * @return die Vordergrundfarbe
     */
    @Override
    public Terminal.Color getForeGround() {
        if (pacman) {
            return PAC_MAN;
        } else if (invincible) {
            return BACK_COLOR;
        } else {
            return FORE_COLOR;
        }
    }

    /**
     * Der Spieler hat das Pfad markieren PowerUp eingesammelt
     */
    public void startPathMark() {
        isPathMark = true;
    }

    /**
     * Gibt aus, ob der Spieler das Pfad markieren PowerUp eingesammelt hat.
     *
     * @return
     */
    public boolean isPathMark() {
        return isPathMark;
    }
}
