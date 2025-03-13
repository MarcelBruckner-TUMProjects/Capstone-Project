package Capstone_Project;

import GameObjects.PowerUps.PowerUp;
import GameObjects.*;
import GameObjects.PowerUps.PowerUpTyp;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.ResizeListener;
import com.googlecode.lanterna.terminal.TerminalSize;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.swing.JFrame;

/**
 *
 * @author Marcel
 */
public class Spielfeld implements ResizeListener {

    /*
    IDs zum erstellen der GameObjects
     */
    private static final char ID_WALL = '0';
    private static final char ID_IN = '1';
    private static final char ID_OUT = '2';
    private static final char ID_STATIC_TRAP = '3';
    private static final char ID_DYNAMIC_TRAP = '4';
    private static final char ID_KEY = '5';
    private static final char ID_POWER_UP = '6';

    /*
    Dateipfade
     */
    private static final String INIT_GAME = "level.properties";
    private static final String SAVE_GAME = "save_game.properties";

    //Zeit zwischen Updates der Ausgabe
    private static final int MILLISECONDS_PER_FRAME = 0;
    //Hoehe der Statusanzeige
    private static final int OVERVIEW_HIGHT = 3;
    //Dauer der Anzeige beim verlassen des Spiels
    private static final long EXIT_TIMEOUT = 3000l;
    //Wahrscheinlichkeit, dass ein Dynamisches Hindernis an einer Kreuzung abbiegt
    private static final double CHANGE_DIRECTION_POSSIBILITY = 0.05;
    //Wahrscheinlichkeit, dass ein PowerUp anstatt eines WhiteSpaces erstellt wird
    private static final double POWER_UP_POSSIBILITY = 0.005;
    //Zeit, wie lang das PacMan und Unverwundbar PowerUp bestehen bleiben
    private static final double POWERUP_TIME = 5000;

    //Verkürzen der Eingabe der Standartfarbe
    private static final Terminal.Color DEFAULT_COLOR = Terminal.Color.DEFAULT;

    //Das Spielfeld; Zur Verwaltung der GameObjects
    private GameObject[][] spielfeld;
    //Der Spieler
    private Player player;
    //Liste der Dynamischen Hindernisse
    private ArrayList<DynamicTrap> dynamicTraps;
    //Liste der Eingänge
    private ArrayList<Entry> entrys;
    //Liste der Schluessel
    private ArrayList<GameObject> keys;
    //Liste der Ausgänge
    private ArrayList<GameObject> exits;

    //Ausgabe des Hinweis PowerUps
    private String lastHint = new String();

    //Spielfeld Dimensionen
    private int height, width;
    //Linke obere Ecke des darzustellenden Bereichs
    private int fromX, fromY;
    //Gesamt verstrichene Zeit seit Spielbeginn
    private int timeGone;
    //Anzahl der gelaufenen Schritte
    private int movesMade;

    //Zeitpunkt des letzten Updates der Dynamischen Hindernisse
    private Date lastDynamicsMove = new Date();
    //Zeitpunkt des letzten Hochzaehlens der Uhr
    private Date timeCounter;

    //Das Ausgabeterminal
    private final Terminal terminal;

    //Spiel ist vorbei
    private boolean isOver;
    //Legende ist gezeigt
    private boolean isLegendeShown;
    //Startbildschirm ist gezeigt
    private boolean isWelcomeScreen;
    //Menu ist gezeigt
    private boolean isMenuShown;
    //Neues Spiel wird geladen (Nicht aus Save Datei geladen)
    private boolean isNewGame;
    //Neues Level wird generiert
    private boolean isGeneratingNewLevel;
    //InitBildschirm ist gezeigt
    private boolean isInitBildschirm = true;

    /**
     * Konstruktor zum Erstellen des Terminals und Anzeigens des Starbildschirms
     */
    public Spielfeld() {
        //Erstellen des Terminals und noetige Einstellungen
        terminal = TerminalFacade.createSwingTerminal();
        terminal.addResizeListener(this);
        terminal.enterPrivateMode();
        terminal.setCursorVisible(false);
        ((SwingTerminal) terminal).getJFrame().setTitle(StringManager.TITLE);
        ((SwingTerminal) terminal).getJFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Zeigen der Initialen Willkommensnachricht für 3 Sekunden
        printStringArrayCentered(StringManager.WILLKOMMEN, 2, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringArrayCentered(StringManager.VERRUECKT, -5, DEFAULT_COLOR, DEFAULT_COLOR);
        sleepForMilliSeconds(3000);
        isInitBildschirm = false;
        //Anzeigen des Startbildschirms
        showWelcomeScreen();
        //Erste Eingabe zum Abfragen, ob neues Level generiert werden, neues Spiel geladen werden oder Spielstand geladen werden soll
        while (true) {
            update();
        }
    }

    /**
     * Initialisierung eines neuen Spiels
     *
     * @param loadPath , Der Pfad zur Datei, aus der das Level geladen wird.
     */
    private void init(String loadPath) {
        //Notwendige Initialisierungen zu Beginn eines Spiels
        timeGone = -1;
        movesMade = 0;
        //Wahr, wenn neues Spiel geladen wird und kein Spielstand geladen wird
        isNewGame = loadPath.equals(INIT_GAME);
        timeCounter = new Date();
        isOver = false;
        player = null;
        isWelcomeScreen = false;
        isGeneratingNewLevel = false;
        isLegendeShown = false;
        dynamicTraps = new ArrayList<>();
        entrys = new ArrayList<>();
        keys = new ArrayList<>();
        exits = new ArrayList<>();
        //Ausgabe des Wartetextes beim Laden eines groesseren Levels
        terminal.clearScreen();
        printStringArrayCentered(StringManager.LADE, 0, DEFAULT_COLOR, DEFAULT_COLOR);
        //Initialisierung des Spielfelds und aller GameObjects
        initSpielfeld(loadPath);
        //Setzen der linken oberen Ecke des darzustellenden Bereichs zu Beginn des Spiels anhängig von der initialen Spielerposition
        fromX = Math.max(0, player.getX() - terminal.getTerminalSize().getColumns() / 2);
        if (player.getX() <= terminal.getTerminalSize().getColumns()) {
            fromX = 0;
        }
        fromY = Math.max(0, player.getY() - terminal.getTerminalSize().getRows() / 2);
        if (player.getY() <= terminal.getTerminalSize().getRows() - OVERVIEW_HIGHT - 1) {
            fromY = 0;
        }
        if (fromY + terminal.getTerminalSize().getRows() - OVERVIEW_HIGHT > height && !(height < terminal.getTerminalSize().getRows())) {
            fromY = height - terminal.getTerminalSize().getRows() + OVERVIEW_HIGHT;
        }
        if (fromX + terminal.getTerminalSize().getColumns() > width && !(width < terminal.getTerminalSize().getColumns())) {
            fromX = width - terminal.getTerminalSize().getColumns();
        }
        //Ausgabe der Unveränderlichen Teile der Uebersicht
        printOverview();
        //Lade Level verschwinden lassen
        clearSpielfeld();
        //Initiale Ausgabe des darzustellenden Bereichs des Spielfelds
        printSpielfeld();
        //Setzen der Zeit auf 0 und erste Ausgabe der Zeit
        updateTime();
        //Menue wird nicht mehr dargestellt, Spiel laeuft
        isMenuShown = false;
        //Starzeitpunkt der letzten Bewegung der dynamischen Hindernisse damit zeitabhaengige Bewegung moeglich ist
        lastDynamicsMove = new Date();
        //Starten des Spiels
        playGame();
    }

    /**
     * Dauerschleife fuer das Spiel verzoegert um je ein paar Millisekunden
     */
    private void playGame() {
        while (true) {
            update();
            sleepForMilliSeconds(MILLISECONDS_PER_FRAME);
        }
    }

    /**
     * Initialisieren des Spielfelds aus einer Properties Datei und erstellen
     * aller GameObjects
     *
     * @param loadPath , Der Pfad zur Datei, aus der das Level geladen wird.
     */
    private void initSpielfeld(String loadPath) {
        //Interne Repräsentation der Properties Datei, aus der geladen wird
        Properties levelProperties = new Properties();
        //Inputstream zum Lesen aus der Properties Datei
        FileInputStream in = null;

        try {
            //Öffnen eines neuen Inputstreams zum lesen aus der Properties Datei und auslesen aus ihr
            in = new FileInputStream(loadPath);
            levelProperties.load(in);
        } catch (FileNotFoundException e) {
            //Abfangen des Fehlers falls die Level Datei nicht gefunden wurde, Ausgabe des Hinweises darauf und beenden des Spiels
            terminal.clearScreen();
            printStringArrayCentered(StringManager.LEVEL_NICHT_GEFUNDEN, -2, DEFAULT_COLOR, DEFAULT_COLOR);
            sleepForMilliSeconds(EXIT_TIMEOUT);
            terminal.clearScreen();
            printStringArrayCentered(StringManager.GENERIERE, -2, DEFAULT_COLOR, DEFAULT_COLOR);
            new GenerateParameters(5);
            sleepForMilliSeconds(EXIT_TIMEOUT);
            init(INIT_GAME);
        } catch (IOException e) {
            //Abfangen des Fehlers falls Probleme beim Lesen auftreten, Ausgabe des Hinweises darauf und beenden des Spiels
            printStringArrayCentered(StringManager.PROBLEM_BEIM_LESEN, -2, DEFAULT_COLOR, DEFAULT_COLOR);
            sleepForMilliSeconds(EXIT_TIMEOUT);
            System.exit(0);
        } finally {
            //Notwendige Aufräumarbeiten nach dem Auslesen aus der Properties Datei
            try {
                //Schliessen des Inputstreams
                in.close();
            } catch (Exception e) {
                //Abfangen des Fehlers falls Probleme beim Schliessen des Inputstreams auftreten, Ausgabe des Hinweises darauf und beenden des Spiels
                printStringAtRowFromMiddle(StringManager.INPUT_STREAM_NICHT_GESCHLOSSEN, 0, DEFAULT_COLOR, DEFAULT_COLOR);
                sleepForMilliSeconds(EXIT_TIMEOUT);
                System.exit(0);
            }
        }

        //Auslesen der Hoehe und Breite des Spielfelds
        height = Integer.parseInt(levelProperties.getProperty("Height"));
        width = Integer.parseInt(levelProperties.getProperty("Width"));

        //Initialisierung des Spielfelds
        spielfeld = new GameObject[width][height];

        //Wenn ein Spielstand geladen wird, auslesen der gespeicherten Zustaende
        if (!isNewGame) {
            //Initialisierung des gespeicherten Spielers
            String pos = levelProperties.getProperty("Player");
            int i = 0;
            while (!(pos.charAt(i) == ',')) {
                i++;
            }
            int x = Integer.parseInt(pos.substring(0, i));
            int y = Integer.parseInt(pos.substring(i + 1));
            player = new Player(x, y);

            //Hat der Spieler schon einen Schluessel gefunden
            player.setKeyCollected(levelProperties.getProperty("Key").equals("true"));
            //Wieviele Leben hat der Spieler beim Speichern
            player.setLives(Integer.parseInt(levelProperties.getProperty("Lives")));
            //Bereits verstrichene Zeit
            timeGone = Integer.parseInt(levelProperties.getProperty("Time"));
            //Bereits gelaufene Schritte
            movesMade = Integer.parseInt(levelProperties.getProperty("Moves"));
            //Ist ein Hinweis gesammelt worden und noch nicht eingesetzt
            if (levelProperties.getProperty("Hint").equals("true")) {
                player.collectHint();
            }
        }

        //Erstellen der GameObjects abhaengig von den gespeicherten Werten in der Properties Datei
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                try {
                    switch (levelProperties.getProperty(i + "," + j).charAt(0)) {
                        case ID_WALL: {
                            spielfeld[i][j] = new Wall(i, j);
                        }
                        break;
                        case ID_IN: {
                            Entry entry = new Entry(i, j);
                            spielfeld[i][j] = entry;
                            entrys.add(entry);
                        }
                        break;
                        case ID_OUT: {
                            Exit exit = new Exit(i, j);
                            spielfeld[i][j] = exit;
                            exits.add(exit);
                        }
                        break;
                        case ID_STATIC_TRAP: {
                            spielfeld[i][j] = new StaticTrap(i, j);
                        }
                        break;
                        case ID_DYNAMIC_TRAP: {
                            DynamicTrap dynamicTrap = new DynamicTrap(i, j);
                            spielfeld[i][j] = new WhiteSpace(dynamicTrap);
                            dynamicTraps.add(dynamicTrap);
                        }
                        break;
                        case ID_KEY: {
                            GameKey key = new GameKey(i, j);
                            spielfeld[i][j] = key;
                            keys.add(key);
                        }
                        break;
                        case ID_POWER_UP: {
                            PowerUpTyp typ = PowerUp.typFromOrdinal(levelProperties.getProperty(i + "," + j).charAt(1));
                            spielfeld[i][j] = new PowerUp(i, j, typ);
                        }
                        break;
                    }
                } catch (Exception e) {
                    //Wenn fuer eine Position im Spielfeld keine ID in der Properties Datei gefunden wird, wird in manchen Fällen ein PowerUp erzeugt
                    if (isNewGame && Math.random() < POWER_UP_POSSIBILITY) {
                        PowerUpTyp typ;
                        switch ((int) (Math.random() * 10)) {
                            case 1:
                                typ = PowerUpTyp.DOUBLE_HP;
                                break;
                            case 2:
                                typ = PowerUpTyp.DYNAMICS_SPEED_DOWN;
                                break;
                            case 3:
                                typ = PowerUpTyp.DYNAMICS_SPEED_UP;
                                break;
                            case 4:
                                typ = PowerUpTyp.HALF_HP;
                                break;
                            case 5:
                                typ = PowerUpTyp.HINT;
                                break;
                            case 6:
                                typ = PowerUpTyp.HP_UP;
                                break;
                            case 7:
                                typ = PowerUpTyp.LOSE_KEY;
                                break;
                            case 8:
                                typ = PowerUpTyp.PACMAN;
                                break;
                            case 9:
                                typ = PowerUpTyp.SHIELD;
                                break;
                            default:
                                typ = PowerUpTyp.PATH;
                                break;
                        }
                        spielfeld[i][j] = new PowerUp(i, j, typ);
                    } else {
                        //Wird kein PowerUp erzeugt, wird ein WhiteSpace erzeugt
                        spielfeld[i][j] = new WhiteSpace(i, j);
                    }
                }
            }
        }

        //Falls der Player nicht aus dem Speicherstand geladen wurde, wird er zufaellig auf einen der Eingaenge platziert
        if (player == null) {
            int entryNr = (int) (Math.random() * entrys.size());
            player = new Player(entrys.get(entryNr));
        }
        entrys = null;
    }

    /**
     * Loeschen des Spielfelds und Neuzeichnen des darzustellenden Bereichs
     */
    private void printSpielfeld() {
        //Einzeigen des Status des Spielers
        showStats();
        //Darstellen, ob ein Hinweis gesammelt wurde
        showHint();
        //Ausgabe der gelaufenen Schritte
        showMovesMade();
        //Loeschen des Spielfelds auf dem Terminal
        clearSpielfeld();
        //Ausgabe der GameObjects auf dem Terminal
        for (int i = 0; i < terminal.getTerminalSize().getColumns(); i++) {
            for (int j = 0; j < terminal.getTerminalSize().getRows() - OVERVIEW_HIGHT; j++) {
                try {
                    printGameObject(spielfeld[i + fromX][j + fromY]);
                } catch (Exception e) {
                }
            }
        }

        //Ausgabe des Players
        printGameObject(player);
        //Ausgabe der aktuellen Position des Players
        showPosition();
        //Spiel laeuft, Menue ist nicht gezeigt
        isMenuShown = false;
    }

    /**
     * Methode zum Bewegen des Spielers auf dem Spielfeld
     *
     * @param x, Die Weite des Schrittes in x-Richtung (-1, 0, 1)
     * @param y, Die Weite des Schrittes in y-Richtung (-1, 0, 1)
     */
    private void movePlayer(int x, int y) {

        //Wenn der Spieler die Spielfeldgrenzen ueberschreiten sollte, wird der Befehl zum Bewegen ignoriert
        if (player.getY() + y < 0 || player.getX() + x >= width || player.getX() + x < 0 || player.getY() + y >= height) {
            return;
        }

        //Auslesen des GameObjects aus dem Spielfeld an der Stelle, die betreten wird
        GameObject moveOn = spielfeld[player.getX() + x][player.getY() + y];

        //Wenn das zu betretene Feld ein Ausgang ist und der Spieler einen Schluessel hat, wird die Gewinnmeldung ausgegeben und das Spiel ist beendet und das Menue gezeigt
        if (moveOn.getTyp().equals("Exit") && player.isKeyCollected()) {
            showEndMessage(true);
            isOver = true;
            isMenuShown = true;
            return;
        }

        //Entscheiden was passiert, wenn das zu betretene Feld betreten werden kann, setzen der Spielerposition und ggf. aendern des darzustellenden Bereichs
        if (moveOn.canBeMovedOn()) {
            //Entfernen des Spielers an der alten Position
            printGameObject(spielfeld[player.getX()][player.getY()]);
            //Setzen der neuen SPielerposition
            player.setPosition(player.getX() + x, player.getY() + y);
            //Ausgabe der neuen Spielerposition
            showPosition();
            //Hochzaehlen der gelaufenen Schritte
            if (x != y) {
                movesMade++;
            }
            //Ausgabe der neuen Anzahl der gelaufenen Schritte
            showMovesMade();

            //Wenn der Spieler auf einem Hindernis steht, verliert er ein Leben und der neue Spielerstatus wird ausgegeben
            if ((spielfeld[player.getX()][player.getY()].getTyp().equals("DynamicTrap") || moveOn.getTyp().equals("StaticTrap")) && !player.isInvincible()) {
                player.loseLive();
                showStats();
            }

            //Wenn der Spieler das PacMan PowerUp besitzt, wird falls er auf ein Dynamisches Hindernis tritt, dieses zerstört
            if (player.isPacman()) {
                int dynNr = -1;
                for (int i = 0; i < dynamicTraps.size(); i++) {
                    if (dynamicTraps.get(i).atSamePositionAs(player)) {
                        dynNr = i;
                    }
                }
                if (dynNr != -1) {
                    dynamicTraps.remove(dynNr);
                }
            }

            //Wenn der Spieler auf einen Schluessel getreten ist, wird dieser im Spieler gespeichert, der Schluessel durch einen WhiteSpace ersetzt und der neue Spielerstatus ausgegeben
            if (spielfeld[player.getX()][player.getY()].getTyp().equals("Key")) {
                keys.remove((GameKey) spielfeld[player.getX()][player.getY()]);
                spielfeld[player.getX()][player.getY()] = new WhiteSpace(player);
                player.setKeyCollected(true);
                showStats();
            }

            //Verändern der Position der linken oberen Ecke des darzustellenden Bereichs beim Verlassen des Spielers des Bereichs
            //Rechts raus
            if (!(player.getX() - fromX < terminal.getTerminalSize().getColumns())) {
                fromX = Math.max(0, fromX + terminal.getTerminalSize().getColumns());
                if (fromX + terminal.getTerminalSize().getColumns() > width) {
                    fromX = width - terminal.getTerminalSize().getColumns();
                }
                printSpielfeld();
            } //Unten raus
            else if (!(player.getY() - fromY + OVERVIEW_HIGHT < terminal.getTerminalSize().getRows())) {
                fromY = Math.max(0, fromY + terminal.getTerminalSize().getRows() - OVERVIEW_HIGHT);
                if (fromY + terminal.getTerminalSize().getRows() - OVERVIEW_HIGHT > height) {
                    fromY = height - terminal.getTerminalSize().getRows() + OVERVIEW_HIGHT;
                }
                printSpielfeld();
            } //Links raus
            else if (!(player.getX() - fromX >= 0)) {
                fromX = Math.max(0, fromX - terminal.getTerminalSize().getColumns());
                printSpielfeld();
            } //Oben raus
            else if (!(player.getY() - fromY >= 0)) {
                fromY = Math.max(0, fromY - terminal.getTerminalSize().getRows() + OVERVIEW_HIGHT);
                printSpielfeld();
            }
            
            //Ausgabe des Spielers
            printGameObject(player);

            //Wenn der Spieler auf ein PowerUp getreten ist, wird die Methode zum entscheiden, was beim jeweiligen PowerUp passiert, aufgerufen
            if (spielfeld[player.getX()][player.getY()].getTyp().equals("PowerUp")) {
                movedOnPowerUp((PowerUp) spielfeld[player.getX()][player.getY()]);
            }
            
            //Wenn der Spieler den Pfad markiert beim Laufen, dann wird hier die Farbe des betretenen Felds geaendert
            if (spielfeld[player.getX()][player.getY()].getTyp().equals("WhiteSpace") && player.isPathMark()) {
                ((WhiteSpace) spielfeld[player.getX()][player.getY()]).setMovedOn();
            }
        }
    }

    /**
     * Methode zum Bewegen der dynamischen Hindernisse
     *
     * Die Hindernisse bewegen sich solange geradeaus, solange sie nicht an eine
     * nicht betretbare Stelle stossen. Stossen sie an eine Stelle, die nicht
     * betreten werden kann, wird eine mögliche neue Bewegungsrichtung
     * berechnet. Die neue Richtung ist abhängig ob links oder rechts vom
     * dynamischen Hindernis ein betretbares Feld ist. Ist auf beiden Seiten ein
     * betretbares Feld, entscheidet das Hindernis zufällig wohin es abbiegt.
     * Ist es in eine Sackgasse geraten, kehrt es um. Wenn das Hindernis auf
     * gerader Strecke abbiegen kann, wird es das mit einer gewissen
     * Wahrscheinlichkeit zufällig tun.
     */
    private void moveDynamicTraps() {
        //Iterieren ueber alle vorhanden Hindernisse
        for (DynamicTrap toMove : dynamicTraps) {
            //Reaktion auf einen Fehler beim Iterieren ueber die vorhanden Hindernisse
            if (toMove == null) {
                break;
            }
            //Variablen zum Speichern der Schrittweite in x und y Richtung der dynamischen Hindernisse
            int x = 0, y = 0;

            //Auslesen der aktuellen Bewegungsrichtung des dynamischen Hindernisses
            MoveDirection moveDirection = toMove.getMoveDirection();

            //Wenn der Spieler nicht an der Stelle des dynamischen Hindernisses steht, wird entschieden, ob das dynamische Hindernis einen Schritt
            //in seine aktuelle Bewegungsrichtung machen kann oder ob es eine neue Richtung einschlagen muss
            if (!player.atSamePositionAs(toMove)) {
                //Wenn das Hindernis nach oben oder unten navigiert, wird die Schrittweite in y-Richtung gesetzt
                if (moveDirection == MoveDirection.UP || moveDirection == MoveDirection.DOWN) {
                    if (moveDirection == MoveDirection.UP) {
                        y = -1;
                    } else {
                        y = 1;
                    }
                    //Wenn an die Stelle abhaengig von der Schrittweite gelaufen werden kann und das Hindernis nicht zufällig abbiegt, bleibt die Bewegungsrichtung erhalten
                    if (spielfeld[toMove.getX()][toMove.getY() + y].canBeMovedOn() && Math.random() > CHANGE_DIRECTION_POSSIBILITY); //Kann das Hindernis nicht an die Stelle laufen oder es hat sich zum Abbiegen entschieden und es kann in beide Richtungen abbiegen, wird die neue Richtung zufällig entschieden
                    else if (spielfeld[toMove.getX() - 1][toMove.getY()].canBeMovedOn() && spielfeld[toMove.getX() + 1][toMove.getY()].canBeMovedOn()) {
                        if (Math.random() < 0.5) {
                            toMove.setMoveDirection(MoveDirection.LEFT);
                        } else {
                            toMove.setMoveDirection(MoveDirection.RIGHT);
                        }
                    } //Kann das Hindernis nur nach links abbiegen, biegt es nach links ab
                    else if (spielfeld[toMove.getX() - 1][toMove.getY()].canBeMovedOn()) {
                        toMove.setMoveDirection(MoveDirection.LEFT);
                    } //Kann das Hindernis nur nach rechts abbiegen, biegt es nach rechts ab
                    else if (spielfeld[toMove.getX() + 1][toMove.getY()].canBeMovedOn()) {
                        toMove.setMoveDirection(MoveDirection.RIGHT);
                    } //Kann das Hindernis in keine Richtung abbiegen, kehrt es um
                    else if (!spielfeld[toMove.getX() + 1][toMove.getY()].canBeMovedOn()
                            && !spielfeld[toMove.getX() - 1][toMove.getY()].canBeMovedOn()
                            && !spielfeld[toMove.getX()][toMove.getY() + y].canBeMovedOn()) {
                        toMove.reverseDirection();
                    }
                } else {
                    //Analog zur Bewegung nach oben und unten mit abbiegen nach links und rechts. (Vgl. oben)
                    if (moveDirection == MoveDirection.RIGHT) {
                        x = 1;
                    } else {
                        x = -1;
                    }
                    if (spielfeld[toMove.getX() + x][toMove.getY()].canBeMovedOn()
                            && Math.random() > CHANGE_DIRECTION_POSSIBILITY); else if (spielfeld[toMove.getX()][toMove.getY() - 1].canBeMovedOn() && spielfeld[toMove.getX()][toMove.getY() + 1].canBeMovedOn()) {
                        if (Math.random() < 0.5) {
                            toMove.setMoveDirection(MoveDirection.UP);
                        } else {
                            toMove.setMoveDirection(MoveDirection.DOWN);
                        }
                    } else if (spielfeld[toMove.getX()][toMove.getY() - 1].canBeMovedOn()) {
                        toMove.setMoveDirection(MoveDirection.UP);
                    } else if (spielfeld[toMove.getX()][toMove.getY() + 1].canBeMovedOn()) {
                        toMove.setMoveDirection(MoveDirection.DOWN);
                    } else if (!spielfeld[toMove.getX()][toMove.getY() + 1].canBeMovedOn()
                            && !spielfeld[toMove.getX()][toMove.getY() - 1].canBeMovedOn()
                            && !spielfeld[toMove.getX() + x][toMove.getY()].canBeMovedOn()) {
                        toMove.reverseDirection();
                    }
                }

                //Setzen der Schrittweite nach Neuberechnung der Bewegungsrichtung
                switch (toMove.getMoveDirection()) {
                    case UP: {
                        x = 0;
                        y = -1;
                    }
                    break;
                    case DOWN: {
                        x = 0;
                        y = 1;
                    }
                    break;
                    case RIGHT: {
                        x = 1;
                        y = 0;
                    }
                    break;
                    case LEFT: {
                        x = -1;
                        y = 0;
                    }
                }

                //Wenn das dynamische Hindernis im darzustellenden Bereich liegt, wird die alte Position mit dem GameObject an dieser Stelle beschrieben.
                if (toMove.getX() - fromX < terminal.getTerminalSize().getColumns()
                        && toMove.getX() - fromX >= 0
                        && toMove.getY() - fromY + OVERVIEW_HIGHT < terminal.getTerminalSize().getRows()
                        && toMove.getY() - fromY >= 0) {
                    if (player.atSamePositionAs(toMove)) {
                        printGameObject(player);
                    } else {
                        printGameObject(spielfeld[toMove.getX()][toMove.getY()]);
                    }
                }
                //Die Position des dynamischen Hindernis wird neu gesetzt
                toMove.setPosition(toMove.getX() + x, toMove.getY() + y);

                //Das dynamische Hindernis wird an der neuen Stelle gezeichnet
                if (toMove.getX() - fromX < terminal.getTerminalSize().getColumns()
                        && toMove.getX() - fromX >= 0
                        && toMove.getY() - fromY + OVERVIEW_HIGHT < terminal.getTerminalSize().getRows()
                        && toMove.getY() - fromY >= 0) {
                    printGameObject(toMove);
                }
            }
        }
    }

    /**
     * Methode zum Verarbeiten des Inputs, der Bewegung der Dynamischen
     * Hindernisse, der Bewegung des Spielers und der konsistenten Darstellung
     * des Uebersicht
     */
    private void update() {
        //Variablen zur Speicherung der Schrittweite des Spielers in x und y Richtung
        int x = 0, y = 0;

        //Wenn der Spieler auf einem dynamischen Hindernis steht, wird ihm bei jeder Bewegung des dynamischen Hindernisses ein Leben abgezogen und das Hindernis bleibt stehen
        if (player != null && !isOver && !isWelcomeScreen && !isLegendeShown && new Date().getTime() - player.getLastLiveLost() > DynamicTrap.getVerzoegerung()) {
            for (DynamicTrap dT : dynamicTraps) {
                if (dT.atSamePositionAs(player)) {
                    player.loseLive();
                    showStats();
                }
            }
        }

        //Falls der Spieler Unverwundbar durch ein PowerUp ist, wird geprüft, wie lang er schon unverwundbar ist.
        //Ist er es seit ueber 5 Sekunden, wird er wieder verwundbar
        if (player != null && player.isInvincible() && !player.isPacman()) {
            if (new Date().getTime() - player.getBecameInvincible().getTime() > POWERUP_TIME) {
                player.loseInvincible();
                movePlayer(0, 0);
                showStats();
            }
        }

        //Falls der Spieler das PacMan PowerUp besitzt, wird geprüft, wie lang das schon so ist.
        //Ist es seit ueber 5 Sekunden, verliert er es.
        if (player != null && player.isPacman()) {
            if (new Date().getTime() - player.becamePacman().getTime() > POWERUP_TIME) {
                player.losePacman();
                movePlayer(0, 0);
                showStats();
            }
        }

        //Einlesen der Tastatureingabe
        Key input = einlesen();

        //Falls eine Eingabe getätigt wurde, wird entschieden, wie darauf reagiert wird
        if (input != null) {
            switch (input.getKind()) {
                //Wurde eine der Pfeiltasten gedrueckt, wird die Schrittweite des Spielers gesetzt
                case ArrowDown: {
                    y = 1;
                }
                break;
                case ArrowUp: {
                    y = -1;
                }
                break;
                case ArrowLeft: {
                    x = -1;
                }
                break;
                case ArrowRight: {
                    x = 1;
                }
                break;
                //Wurde Escape gedrueckt, wird das Menu aufgerufen
                case Escape: {
                    if (!isWelcomeScreen) {
                        showMenu();
                    }
                }
                break;
                //Ist der Startbildschirm gezeigt und F1 wird gedrueckt, wird ein neues Level generiert.
                case F1: {
                    if (isWelcomeScreen) {
                        isGeneratingNewLevel = true;
                        terminal.clearScreen();
                        printStringAtRowFromMiddle(StringManager.SCHWIERIGKEITSGRAD, 0, DEFAULT_COLOR, DEFAULT_COLOR);
                    }
                }
                //Ist das Menue gezeigt und es wird F2 gedrueckt, wird zum aktuellen Spiel zurückgekehrt
                break;
                case F2: {
                    if (!isLegendeShown && isMenuShown && !isOver && !isWelcomeScreen) {
                        printOverview();
                        printSpielfeld();
                    }
                }
                break;
                //F3 laedt, wenn der Startbildschirm oder das Menue gezeigt wird, den Speicherstand
                case F3: {
                    if (!isLegendeShown && isMenuShown && !isOver) {
                        loadSave();
                    }
                }
                //F4 speichert, wenn der Startbildschirm oder das Menue gezeigt wird, das aktuelle Spiel und beendet es
                break;
                case F4: {
                    if (!isLegendeShown && isMenuShown && !isOver && !isWelcomeScreen) {
                        save();
                    }
                }
                break;
                //F5 beendet wenn das Menue gezeigt wird oder das Level beendet ist, das Spiel
                case F5: {
                    if (!isLegendeShown && (isMenuShown || isOver) && !isWelcomeScreen) {
                        showExitMessage();
                    }
                }
                break;
                //F6 zeigt, wenn das Menue dargestellt ist, die Legende
                case F6: {
                    if (!isLegendeShown && isMenuShown && !isOver && !isWelcomeScreen) {
                        showLegende();
                    }
                }
                break;
                //F7 startet, wenn das Menue dargestellt ist, das aktuelle Level neu
                case F7: {
                    if (!isLegendeShown && isMenuShown || isOver) {
                        init(INIT_GAME);
                    }
                }
                break;
                //Reaktion auf das druecken einer Taste mit alphanumerischen Wert
                case NormalKey: {
                    hitNormalKey(input);
                }
            }

            //Wenn das Spiel laeuft und die x und y Schrittweite nicht gleich 0 sind, wird der Spieler um x und y bewegt
            if (x != y && !isMenuShown && !isLegendeShown && !isOver && !isGeneratingNewLevel && !isInitBildschirm) {
                movePlayer(x, y);
            }
        }

        //Wenn das Spiel laeuft und die letzte Bewegung der dynamischen Hindernisse lang genug her ist, werden sie bewegt und der Zeitpunkt der letzten Bewegung ueberschrieben
        if (new Date().getTime() - lastDynamicsMove.getTime() > DynamicTrap.getVerzoegerung() && !isMenuShown) {
            lastDynamicsMove = new Date();
            moveDynamicTraps();
        }

        //Wenn das Spiel laeuft und das letzte Aktualisieren der Zeit eine Sekunde her ist, wird die verstrichene Zeit aktualisiert
        if (!isMenuShown && !isOver && !isLegendeShown) {
            if (new Date().getTime() - timeCounter.getTime() >= 1000) {
                timeCounter = new Date();
                updateTime();
            }
        }
    }

    /**
     * Methode zur Reaktion auf den Druck einer Taste mit alphanumerischen Wert
     *
     * @param input, die Taste, die gedrueckt wurde
     */
    private void hitNormalKey(Key input) {
        //Eingabe des Schwierigkeitsgrades, wenn ein neues Level erstellt wird
        if (isGeneratingNewLevel && input.getCharacter() > '0' && input.getCharacter() <= '9') {
            terminal.clearScreen();
            printStringArrayCentered(StringManager.GENERIERE, -1, DEFAULT_COLOR, DEFAULT_COLOR);
            printStringAtRowFromMiddle(StringManager.SCHWIERIGKEIT_GEWAEHLT + (input.getCharacter() - '0'), -3, DEFAULT_COLOR, DEFAULT_COLOR);
            new GenerateParameters(input.getCharacter() - '0');
            sleepForMilliSeconds(EXIT_TIMEOUT);
            init(INIT_GAME);
        } //Benutzen eines Hinweises
        else if (player != null && !isGeneratingNewLevel && (input.getCharacter() == '2' || input.getCharacter() == '3') && player.isHintCollected()) {
            player.useHint();
            clearRow(StringManager.MENU.length(), terminal.getTerminalSize().getColumns(), 2);
            lastHint = "Naechster ";
            int nr;
            if (input.getCharacter() == '2') {
                nr = getNrOfNearestInCollection(keys);
                lastHint += "Schluessel: " + keys.get(nr).getX() + " - " + keys.get(nr).getY();
            } else if (input.getCharacter() == '3') {
                nr = getNrOfNearestInCollection(exits);
                lastHint += "Ausgang: " + exits.get(nr).getX() + " - " + exits.get(nr).getY();
            }
            showHint();
        }
    }

    /**
     * Methode zum Einlesen eines Tastendrucks und leeren des Puffers
     *
     * @return
     */
    private Key einlesen() {
        //Einlesen des Inputs
        Key input = terminal.readInput();
        //Leeren des Puffers
        while (terminal.readInput() != null);
        //Rueckgabe des Inputs
        return input;
    }

    /**
     * Methode zur Reaktion auf das Veraendern der Terminalgroesse
     *
     * @param ts
     */
    @Override
    public void onResized(TerminalSize ts) {
        if (isInitBildschirm) {
            terminal.clearScreen();
            printStringArrayCentered(StringManager.WILLKOMMEN, 2, DEFAULT_COLOR, DEFAULT_COLOR);
            printStringArrayCentered(StringManager.VERRUECKT, -5, DEFAULT_COLOR, DEFAULT_COLOR);
        } //Ist der Startbildschirm oder die Schwirigkeitsauswahl gezeigt, wird das beim Skalieren weiterhin mittig dargestellt
        else if (isWelcomeScreen) {
            if (isGeneratingNewLevel) {
                terminal.clearScreen();
                printStringAtRowFromMiddle(StringManager.SCHWIERIGKEITSGRAD, 0, DEFAULT_COLOR, DEFAULT_COLOR);
            } else {
                showWelcomeScreen();
            }
        } //Ist die Legende gezeigt, wird sie beim Skalieren weiterhin mittig dargestellt
        else if (isLegendeShown) {
            showLegende();
        } //Ist die Nachricht ueber Verlust oder Gewinn des Spiels gezwigt, wird sie beim Skalieren weiterhin mittig dargsetellt
        else if (isOver) {
            if (player.isDead()) {
                showEndMessage(false);
            } else {
                showEndMessage(true);
            }
        } //Ist das Menue gezeigt, wird es beim Skalieren weiterhin mittig dargestellt
        else if (isMenuShown) {
            showMenu();
        } //Laeuft das Spiel und das Terminal wird Skaliert, wird der darzustellende Bereich neu gezeichnet
        //Liegt nach dem Skalieren der Spieler ausserhalb des sichtbaren Bereichs, wird die linke obere Ecke des Bereichs neu berechnet
        else {
            if (player.getX() - fromX > terminal.getTerminalSize().getColumns()) {
                fromX = Math.max(0, player.getX() + 1 - terminal.getTerminalSize().getColumns());
            }
            if (player.getY() - fromY + OVERVIEW_HIGHT > terminal.getTerminalSize().getRows()) {
                fromY = Math.max(0, player.getY() + 1 - terminal.getTerminalSize().getRows() - OVERVIEW_HIGHT);
            }

            clearRow(0, terminal.getTerminalSize().getColumns(), 0);
            clearRow(0, terminal.getTerminalSize().getColumns(), 1);
            clearRow(0, terminal.getTerminalSize().getColumns(), 2);
            printOverview();
            showStats();
            printSpielfeld();
            movePlayer(0, 0);
            timeGone--;
            updateTime();
        }
    }

    /**
     * Anzeige des Spielerstatus oder der Nachricht des Todes, wenn der Spieler
     * keine Leben mehr hat
     */
    private void showStats() {
        einlesen();
        //Anzeigen des Verlusts des Spieles wenn der Spieler keine Leben mehr hat.
        if (player.isDead()) {
            showEndMessage(false);
            return;
        }

        //Ausgabe der Leben
        printStringAt(player.getLives() + " ", StringManager.LIVES_LEFT.length(), 0, DEFAULT_COLOR, DEFAULT_COLOR);

        //Ausgabe, ob ein Schluessel gefunden wurde.
        String output = "";
        if (player.isKeyCollected()) {
            output += "ja  ";
        } else {
            output += "nein";
        }
        printStringAt(output, StringManager.KEY_COLLECTED.length(), 1, DEFAULT_COLOR, DEFAULT_COLOR);

        if (player.isPacman()) {
            output = "    Gegner fressen    ";
        } else if (player.isInvincible()) {
            output = "    Unverwundbar     ";
        } else {
            output = "                         ";
        }
        printStringAtRowFromMiddle(output, terminal.getTerminalSize().getRows() / 2 - 1, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Ausgabe des Unveränderlichen Teils der Uebersicht
     */
    private void printOverview() {
        //Ausgabe, wie das Menue aufgerufen wird
        printStringAt(StringManager.MENU, 0, 2, DEFAULT_COLOR, DEFAULT_COLOR);
        //Ausgabe, wieviele Leben verbleiben
        printStringAt(StringManager.LIVES_LEFT, 0, 0, DEFAULT_COLOR, DEFAULT_COLOR);
        //Ausgabe, ob ein Schluessel gefunden wurde
        printStringAt(StringManager.KEY_COLLECTED, 0, 1, DEFAULT_COLOR, DEFAULT_COLOR);
        //Ausgabe der gelaufenen Schritte
        printStringAt(StringManager.GELAUFEN, terminal.getTerminalSize().getColumns() - StringManager.GELAUFEN.length(), 1, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Ausgabe beim Verlieren oder Gewinnen des Spiels
     *
     * @param output, Der auszugebene Text
     */
    private void showEndMessage(boolean won) {
        isOver = true;
        isMenuShown = true;
        einlesen();
        terminal.clearScreen();
        Terminal.Color back;
        Terminal.Color front;
        //Füllen des Terminals mit Gruen oder Rot je nach Spielausgang
        if (won) {
            back = Terminal.Color.GREEN;
            front = Terminal.Color.BLACK;
        } else {
            back = Terminal.Color.RED;
            front = DEFAULT_COLOR;
        }
        for (int i = 0; i < terminal.getTerminalSize().getColumns(); i++) {
            for (int j = 0; j < terminal.getTerminalSize().getRows(); j++) {
                printCharAt(' ', i, j, back, back);
            }
        }

        //Ausgabe der Gewinn- oder Verlustnachricht und der Moeglichkeiten zum fortfahren
        if (won) {
            printStringArrayCentered(StringManager.GEWONNEN, 2, back, front);
        } else {
            printStringArrayCentered(StringManager.VERLOREN, 2, back, front);
        }

        printStringAtRowFromMiddle(StringManager.BEENDEN, 0, back, front);
        printStringAtRowFromMiddle(StringManager.NEUSTART, -1, back, front);
    }

    /**
     * Zeichne das Menue auf das Terminal
     */
    private void showMenu() {
        isMenuShown = true;
        isLegendeShown = false;
        terminal.clearScreen();
        einlesen();
        printStringArrayCentered(StringManager.WHOLE_MENUE, -3, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Lade den gespeicherten Spielstand
     */
    private void loadSave() {
        File f = new File(SAVE_GAME);

        //Existiert ein Spielstand, wird er geladen
        if (f.exists() && !f.isDirectory()) {
            init(SAVE_GAME);
        } //Existiert keiner, wird das normale Level geladen
        else {
            terminal.clearScreen();
            printStringAtRowFromMiddle(StringManager.SAVE_NICHT_GEFUNDEN, -3, DEFAULT_COLOR, DEFAULT_COLOR);
            printStringArrayCentered(StringManager.LADE, -1, DEFAULT_COLOR, DEFAULT_COLOR);
            sleepForMilliSeconds(1000);
            init(INIT_GAME);
        }
    }

    /**
     * Methode zum Speichern des Spielstands
     */
    private void save() {
        try {
            String out;
            char c;
            PowerUpTyp pu = null;
            //Oeffnen des OutputStreams
            PrintWriter p = new PrintWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(new File(SAVE_GAME))));
            //Errechnen der Values
            for (GameObject[] columns : spielfeld) {
                for (GameObject gO : columns) {
                    switch (gO.getTyp()) {
                        case "Key":
                            c = ID_KEY;
                            break;
                        case "StaticTrap":
                            c = ID_STATIC_TRAP;
                            break;
                        case "Wall":
                            c = ID_WALL;
                            break;
                        case "Entry":
                            c = ID_IN;
                            break;
                        case "Exit":
                            c = ID_OUT;
                            break;
                        case "PowerUp": {
                            c = ID_POWER_UP;
                            pu = ((PowerUp) gO).getPowerUpTyp();
                        }
                        break;
                        default:
                            c = ' ';
                            break;
                    }
                    //Hinzufuegen der PowerUpArt, falls ein PowerUp gespeichert wird
                    if (!(c == ' ')) {
                        out = gO.getX() + "," + gO.getY() + "=" + c;
                        if (pu != null && c == ID_POWER_UP) {
                            out += pu.ordinal();
                        }
                        //Schreiben des Keys bestehend aus der Koordinate des GameObjects und der Value der ID
                        p.println(out);
                    }
                }
            }

            //Speichern der Dynamischen Hindernisse
            for (DynamicTrap dt : dynamicTraps) {
                p.println(dt.getX() + "," + dt.getY() + "=" + ID_DYNAMIC_TRAP);
            }

            //Speichern der Hoehe und Breite des Levels
            p.println("Height=" + height);
            p.println("Width=" + width);
            //Speichern der Player Position
            p.println("Player=" + player.getX() + "," + player.getY());
            //Speichern ob ein Schluessel gefunden wurde
            p.println("Key=" + player.isKeyCollected());
            //Speichern der Leben
            p.println("Lives=" + player.getLives());
            //Speichern der verstrichenen Zeit
            p.println("Time=" + timeGone);
            //Speichern der gelaufenen Schritte
            p.println("Moves=" + movesMade);
            //Speichern ob ein Hinweis gefunden ist
            p.println("Hint=" + player.isHintCollected());
            //Schliessen des OutputStreams
            p.close();
        } catch (FileNotFoundException ex) {
        }
        //Beenden des Spiels
        showExitMessage();
    }

    /**
     * Stellt die Legende auf dem Terminal dar
     */
    private void showLegende() {
        isLegendeShown = true;
        einlesen();
        terminal.clearScreen();
        printGameObject(new Player(terminal.getTerminalSize().getColumns() / 2 + fromX + 5, terminal.getTerminalSize().getRows() / 2 - 4 + fromY - OVERVIEW_HIGHT));
        printGameObject(new Wall(terminal.getTerminalSize().getColumns() / 2 + fromX + 3, terminal.getTerminalSize().getRows() / 2 - 3 + fromY - OVERVIEW_HIGHT));
        printGameObject(new Entry(terminal.getTerminalSize().getColumns() / 2 + fromX + 5, terminal.getTerminalSize().getRows() / 2 - 2 + fromY - OVERVIEW_HIGHT));
        printGameObject(new Exit(terminal.getTerminalSize().getColumns() / 2 + fromX + 5, terminal.getTerminalSize().getRows() / 2 - 1 + fromY - OVERVIEW_HIGHT));
        printGameObject(new StaticTrap(terminal.getTerminalSize().getColumns() / 2 + fromX + 11, terminal.getTerminalSize().getRows() / 2 + fromY - OVERVIEW_HIGHT));
        printGameObject(new DynamicTrap(terminal.getTerminalSize().getColumns() / 2 + fromX + 12, terminal.getTerminalSize().getRows() / 2 + 1 + fromY - OVERVIEW_HIGHT));
        printGameObject(new GameKey(terminal.getTerminalSize().getColumns() / 2 + fromX + 6, terminal.getTerminalSize().getRows() / 2 + 2 + fromY - OVERVIEW_HIGHT));
        printGameObject(new PowerUp(terminal.getTerminalSize().getColumns() / 2 + fromX + 5, terminal.getTerminalSize().getRows() / 2 + 3 + fromY - OVERVIEW_HIGHT, PowerUpTyp.HINT));

        printStringArrayCentered(StringManager.WHOLE_LEGENDE, -4, DEFAULT_COLOR, DEFAULT_COLOR);

        printStringAtRowFromMiddle(StringManager.MENU, -5, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Zeigt den Abschieds Text und beendet das Spiel nach kurzer Wartezeit
     */
    private void showExitMessage() {
        einlesen();
        terminal.clearScreen();
        printStringArrayCentered(StringManager.DANKE, -2, DEFAULT_COLOR, DEFAULT_COLOR);
        sleepForMilliSeconds(EXIT_TIMEOUT);
        System.exit(0);
    }

    /**
     * Zeigt den Startbildschrim auf dem Terminal
     */
    private void showWelcomeScreen() {
        einlesen();
        isWelcomeScreen = true;
        isMenuShown = true;
        isGeneratingNewLevel = false;
        terminal.clearScreen();
        printStringAtRowFromMiddle(StringManager.WILLKOMMEN_BEIM, 6, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.ZUM_BEWEGEN, 4, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.MENU, 3, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.INTRO_1, 1, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.INTRO_2, 0, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.VIEL_SPASS, -1, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.NEUES_LEVEL_GENERIEREN, -3, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.LADEN, -4, DEFAULT_COLOR, DEFAULT_COLOR);
        printStringAtRowFromMiddle(StringManager.NEUSTART, -5, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Hilfsmethode zur mittig zentrierten Darstellung eines Strings um ein paar
     * Reihen nach oben und unten verschoben
     *
     * @param output, der Auszugebende String
     * @param height, die Verschiebung nach oben und unten
     * @param backColor, die Hintergrundfarbe
     * @param foreColor, die Vordergrundfarbe
     */
    private void printStringAtRowFromMiddle(String output, int height, Terminal.Color backColor, Terminal.Color foreColor) {
        printStringAt(output, (terminal.getTerminalSize().getColumns() / 2) - output.length() / 2, terminal.getTerminalSize().getRows() / 2 - height, backColor, foreColor);
    }

    /**
     * Hilfsmethode der Darstellung eines Strings ab einer bestimmten Position
     *
     * @param output, der Auszugebende String
     * @param x, x Koordinate des ersten Buchstabens
     * @param y, y Koordinate des ersten Buchstabens
     * @param backColor, die Hintergrundfarbe
     * @param foreColor, die Vordergrundfarbe
     */
    private void printStringAt(String output, int x, int y, Terminal.Color backColor, Terminal.Color foreColor) {
        try {
            terminal.applyBackgroundColor(backColor);
            terminal.applyForegroundColor(foreColor);
            terminal.moveCursor(x, y);
            for (Character c : output.toCharArray()) {
                terminal.putCharacter(c);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Ausgabe eines Chars an einer bestimmten Position
     *
     * @param output, der Auszugebende Char
     * @param x, x Koordinate des ersten Buchstabens
     * @param y, y Koordinate des ersten Buchstabens
     * @param backColor, die Hintergrundfarbe
     * @param foreColor, die Vordergrundfarbe
     */
    private void printCharAt(char output, int x, int y, Terminal.Color backColor, Terminal.Color foreColor) {
        try {
            terminal.applyForegroundColor(foreColor);
            terminal.applyBackgroundColor(backColor);
            terminal.moveCursor(x, y);
            terminal.putCharacter(output);
        } catch (Exception e) {
        }
    }

    /**
     * Hilfsmethode zur einfacheren Schreibweise eines Sleep Befehls
     *
     * @param time
     */
    private void sleepForMilliSeconds(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Methode zum Updaten der Zeit
     */
    private void updateTime() {
        //Zeit hochzaehlen
        timeGone++;
        //Rausrechnen der Sekunden
        int seconds = timeGone % 60;
        //Rausrechnen der Minuten
        int minutes = (timeGone / 60) % 60;
        //Rausrechnen der Stunden
        int hours = (timeGone / 60 / 60) % 24;

        //Erstellen des Ausgabetexts
        String time = " " + hours + ":" + minutes + ":" + seconds;
        //Ausgabe des Texts
        printStringAt(time, terminal.getTerminalSize().getColumns() - time.length(), 0, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Anzeige wieviele Schritte gelaufen wurden
     */
    private void showMovesMade() {
        einlesen();
        String moves = " " + movesMade;
        printStringAt(moves, terminal.getTerminalSize().getColumns() - moves.length() - StringManager.GELAUFEN.length(), 1, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Methode zur Reaktion, wenn der Spieler auf ein PowerUp laeuft
     *
     * @param powerUp, das eingesammelte PowerUp
     */
    private void movedOnPowerUp(PowerUp powerUp) {
        //Ersetzen der Position des PowerUps
        spielfeld[powerUp.getX()][powerUp.getY()] = new WhiteSpace(powerUp);
        String output = "";
        switch (powerUp.getPowerUpTyp()) {
            //Doppeltes Leben PowerUp
            case DOUBLE_HP: {
                output = "  Doppeltes Leben  ";
                player.setLives(player.getLives() * 2);
                showStats();
            }
            break;
            //Langsamere Gegner PowerUp
            case DYNAMICS_SPEED_DOWN:
                output = "  Langsamere Gegner  ";
                DynamicTrap.beschleunige(-50);
                break;
            //Schnellere Gegner PowerUp
            case DYNAMICS_SPEED_UP:
                output = "  Schnellere Gegner  ";
                DynamicTrap.beschleunige(+50);
                break;
            //Halbes Leben PowerUp
            case HALF_HP: {
                output = "  Halbes Leben  ";
                player.halfLives();
                showStats();
            }
            break;
            //+1 Leben PowerUp
            case HP_UP: {
                output = "     +1 Leben     ";
                player.gainLive();
                showStats();
            }
            break;
            //Verliere deinen Schluessel PowerUp
            case LOSE_KEY: {
                output = "    Schluessel verloren     ";
                player.setKeyCollected(false);
                showStats();
                if (keys.isEmpty()) {
                    terminal.clearScreen();
                    printStringArrayCentered(StringManager.KEINE_SCHLUESSEL, -2, DEFAULT_COLOR, DEFAULT_COLOR);
                    sleepForMilliSeconds(4000);
                    showEndMessage(false);
                }
            }
            break;
            //Unverwundbar PowerUp
            case SHIELD: {
                output = "     Unverwundbar     ";
                player.becomeInvincible();
                movePlayer(0, 0);
            }
            break;
            //Hinweis PowerUp
            case HINT: {
                output = "    Hinweis gefunden    ";
                player.collectHint();
                showHint();
            }
            break;
            //Gegner fressen PowerUp
            case PACMAN: {
                output = "     Gegner fressen     ";
                player.becomePacman();
                movePlayer(0, 0);
            }
            break;
            //Der gelaufene Pfad wird nach einsammeln dieses PowerUps markiert
            case PATH: {
                output = "    Pfad markieren     ";
                player.startPathMark();
            }
            break;
        }

        //Ausgabe des gefundenen PowerUps
        if (!player.isDead() && !keys.isEmpty()) {
            printStringAtRowFromMiddle(output, terminal.getTerminalSize().getRows() / 2 - 1, DEFAULT_COLOR, DEFAULT_COLOR);
        }
    }

    /**
     * Berechnung des naechsten GameObjects aus einer Liste in Relation zum
     * Player
     *
     * @param gameObjects, die zu testende GameObjects
     */
    private int getNrOfNearestInCollection(ArrayList<GameObject> gameObjects) {
        double distance = player.computeDistance(gameObjects.get(0));
        int nr = 0;
        for (int i = 1; i < gameObjects.size(); i++) {
            if (player.computeDistance(gameObjects.get(i)) < distance) {
                distance = player.computeDistance(gameObjects.get(i));
                nr = i;
            }
        }
        return nr;
    }

    /**
     * Zeigen des Hinweis bzw. der Nachricht, wie er anzuzeigen ist
     */
    private void showHint() {
        String out;
        einlesen();
        clearRow(terminal.getTerminalSize().getColumns() / 2, terminal.getTerminalSize().getColumns(), 2);
        showStats();
        if (player.isHintCollected()) {
            out = StringManager.HINT;
        } else {
            out = lastHint;
        }
        printStringAt(out, terminal.getTerminalSize().getColumns() - out.length(), 2, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Stelle die Spielerposition auf dem Terminal dar
     */
    private void showPosition() {
        einlesen();
        printStringAtRowFromMiddle(" PosX: " + player.getX() + " - " + "PosY: " + player.getY() + " ", terminal.getTerminalSize().getRows() / 2, DEFAULT_COLOR, DEFAULT_COLOR);
    }

    /**
     * Loesche in einer Reihe einen bestimmten Bereich
     *
     * @param from, Startposition des Bereichs
     * @param to, Endposition des Bereichs
     * @param row, Reihe
     */
    private void clearRow(int from, int to, int row) {
        if (from >= terminal.getTerminalSize().getColumns()) {
            return;
        }
        for (int i = from; i < Math.min(to, terminal.getTerminalSize().getColumns()); i++) {
            try {
                terminal.applyForegroundColor(DEFAULT_COLOR);
                terminal.applyBackgroundColor(DEFAULT_COLOR);
                terminal.moveCursor(i, row);
                terminal.putCharacter(' ');
            } catch (Exception e) {
            }
        }
    }

    private void clearSpielfeld() {
        for (int i = OVERVIEW_HIGHT; i < terminal.getTerminalSize().getRows(); i++) {
            clearRow(0, terminal.getTerminalSize().getColumns(), i);
        }
    }

    /**
     * Ausgabe eines GameObjects auf dem Terminal
     *
     * @param gameObject, das auszugebene GameObject
     */
    private void printGameObject(GameObject gameObject) {
        if (gameObject == null) {
            return;
        }
        terminal.moveCursor(gameObject.getX() - fromX, gameObject.getY() - fromY + OVERVIEW_HIGHT);
        terminal.applyBackgroundColor(gameObject.getBackGround());
        terminal.applyForegroundColor(gameObject.getForeGround());
        terminal.putCharacter(gameObject.print());
    }

    /**
     * Gibt einen mehrzeiligen Text zentriert und um variabel viele Zeilen
     * verschoben aus
     *
     * @param output, der auszugebene Text
     * @param height, die Verschiebung nach oben und unten
     * @param backColor, die Hintergrundfarbe
     * @param foreColor, die Vordergrundfarbe
     */
    private void printStringArrayCentered(String[] output, int height, Terminal.Color backColor, Terminal.Color foreColor) {
        for (int i = 0; i < output.length; i++) {
            printStringAtRowFromMiddle(output[i], height - i + output.length, backColor, foreColor);
        }
    }
}
