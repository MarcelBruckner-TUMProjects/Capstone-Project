/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameObjects.PowerUps;

/**
 * Speciherort der verschiedenen PowerUp Typen
 *
 * @author Marcel
 */
public enum PowerUpTyp {
    //Das Leben des Spielers wird verdoppelt
    DOUBLE_HP,
    //Die dynamischen Hindernisse werden schneller
    DYNAMICS_SPEED_UP,
    //Die dynamischen Hindernisse werden langsamer
    DYNAMICS_SPEED_DOWN,
    //Das Leben des Players wird halbiert
    HALF_HP,
    //Der Spieler erhaelt einen Hinweis. Ueber die Tasten [2] und [3] kann er sich damit die Position des naechsten Schluessels oder des naechsten Ausgangs zeigen lassen
    HINT,
    //Der Spieler erhaelt ein Leben
    HP_UP,
    //Der Spieler verliert seinen Schluessel. Ist kein Schluessel mehr im Spiel, ist das Spiel verloren.
    LOSE_KEY,
    //Der Spieler ist fuer kurze Zeit unverwundbar und kann dynamische Hindernisse fressen. Er erhaelt beim auf ein dynamisches Hindernis treten keinen Schaden und das Hindernis verschwindet
    PACMAN,
    //Der Spieler erhaelt fuer kurze Zeit Unverwundbarkeit. Dynamische Hindernisse fuegen dem Spieler keinen Schaden zu.
    SHIELD,
    //Nach einsammel dieses PowerUps wird der gelaufene Weg markiert. Nach einem Neuladen des Levels oder eines Neustarts des Spiels muss das PowerUp erneut gesucht werden.
    PATH
}
