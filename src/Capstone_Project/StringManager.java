/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Capstone_Project;

/**
 * Diese Klasse repräsentiert eine Sammlung von Strings für das Spiel
 *
 * @author Marcel
 */
public final class StringManager {

    /*
     * Visuelle Darstellungen von Strings
     */
    static final String[] WILLKOMMEN = {
        " __        ___ _ _ _                                          _          _           ",
        " \\ \\      / (_) | | | _____  _ __ ___  _ __ ___   ___ _ __   | |__   ___(_)_ __ ___  ",
        "  \\ \\ /\\ / /| | | | |/ / _ \\| '_ ` _ \\| '_ ` _ \\ / _ \\ '_ \\  | '_ \\ / _ \\ | '_ ` _ \\ ",
        "   \\ V  V / | | | |   < (_) | | | | | | | | | | |  __/ | | | | |_) |  __/ | | | | | |",
        "    \\_/\\_/  |_|_|_|_|\\_\\___/|_| |_| |_|_| |_| |_|\\___|_| |_| |_.__/ \\___|_|_| |_| |_|"};

    static final String[] GEWONNEN = {
        "  ____                                           ",
        " / ___| _____      _____  _ __  _ __   ___ _ __  ",
        "| |  _ / _ \\ \\ /\\ / / _ \\| '_ \\| '_ \\ / _ \\ '_ \\ ",
        "| |_| |  __/\\ V  V / (_) | | | | | | |  __/ | | |",
        " \\____|\\___| \\_/\\_/ \\___/|_| |_|_| |_|\\___|_| |_|"

    };

    static final String[] VERLOREN = {
        "__     __        _                      ",
        "\\ \\   / /__ _ __| | ___  _ __ ___ _ __  ",
        " \\ \\ / / _ \\ '__| |/ _ \\| '__/ _ \\ '_ \\ ",
        "  \\ V /  __/ |  | | (_) | | |  __/ | | |",
        "   \\_/ \\___|_|  |_|\\___/|_|  \\___|_| |_|"

    };

    static final String[] VERRUECKT = {
        "                     _   _      _    _               _          _                _       _   _     ",
        "__   _____ _ __ _ __(_) (_) ___| | _| |_ ___ _ __   | |    __ _| |__  _   _ _ __(_)_ __ | |_| |__  ",
        "\\ \\ / / _ \\ '__| '__| | | |/ __| |/ / __/ _ \\ '_ \\  | |   / _` | '_ \\| | | | '__| | '_ \\| __| '_ \\ ",
        " \\ V /  __/ |  | |  | |_| | (__|   <| ||  __/ | | | | |__| (_| | |_) | |_| | |  | | | | | |_| | | |",
        "  \\_/ \\___|_|  |_|   \\__,_|\\___|_|\\_\\\\__\\___|_| |_| |_____\\__,_|_.__/ \\__, |_|  |_|_| |_|\\__|_| |_|",
        "                                                                      |___/                        "
    };

    static final String[] DANKE = {
        " ____              _           __ _   _            ____        _      _            ",
        "|  _ \\  __ _ _ __ | | _____   / _(_) (_)_ __ ___  / ___| _ __ (_) ___| | ___ _ __  ",
        "| | | |/ _` | '_ \\| |/ / _ \\ | |_| | | | '__/ __| \\___ \\| '_ \\| |/ _ \\ |/ _ \\ '_ \\ ",
        "| |_| | (_| | | | |   <  __/ |  _| |_| | |  \\__ \\  ___) | |_) | |  __/ |  __/ | | |",
        "|____/ \\__,_|_| |_|_|\\_\\___| |_|  \\__,_|_|  |___/ |____/| .__/|_|\\___|_|\\___|_| |_|",
        "                                                        |_|                        "
    };

    static final String[] KEINE_SCHLUESSEL = {
        " _  __    _              ____       _     _ _   _              _                  _          ",
        "| |/ /___(_)_ __   ___  / ___|  ___| |__ | (_) (_)___ ___  ___| |  _ __ ___   ___| |__  _ __ ",
        "| ' // _ | | '_ \\ / _ \\ \\___ \\ / __| '_ \\| | | | / __/ __|/ _ | | | '_ ` _ \\ / _ | '_ \\| '__|",
        "| . |  __| | | | |  __/  ___) | (__| | | | | |_| \\__ \\__ |  __| | | | | | | |  __| | | | |   ",
        "|_|\\_\\___|_|_| |_|\\___| |____/ \\___|_| |_|_|\\__,_|___|___/\\___|_| |_| |_| |_|\\___|_| |_|_|   "
    };

    static final String[] LADE = {
        " _              _                  ",
        "| |    __ _  __| | ___             ",
        "| |   / _` |/ _` |/ _ \\            ",
        "| |__| (_| | (_| |  __/  _   _   _ ",
        "|_____\\__,_|\\__,_|\\___| (_) (_) (_)"
    };

    static final String[] GENERIERE = {
        "  ____                      _                                             _                   _ ",
        " / ___| ___ _ __   ___ _ __(_) ___ _ __ ___   _ __   ___ _   _  ___ ___  | |    _____   _____| |",
        "| |  _ / _ | '_ \\ / _ | '__| |/ _ | '__/ _ \\ | '_ \\ / _ | | | |/ _ / __| | |   / _ \\ \\ / / _ | |",
        "| |_| |  __| | | |  __| |  | |  __| | |  __/ | | | |  __| |_| |  __\\__ \\ | |__|  __/\\ V |  __| |",
        " \\____|\\___|_| |_|\\___|_|  |_|\\___|_|  \\___| |_| |_|\\___|\\__,_|\\___|___/ |_____\\___| \\_/ \\___|_|"
    };

    static final String[] LEVEL_NICHT_GEFUNDEN = {
        " _                   _         _      _     _                 __                 _            ",
        "| |    _____   _____| |  _ __ (_) ___| |__ | |_    __ _  ___ / _|_   _ _ __   __| | ___ _ __  ",
        "| |   / _ \\ \\ / / _ | | | '_ \\| |/ __| '_ \\| __|  / _` |/ _ | |_| | | | '_ \\ / _` |/ _ | '_ \\ ",
        "| |__|  __/\\ V |  __| | | | | | | (__| | | | |_  | (_| |  __|  _| |_| | | | | (_| |  __| | | |",
        "|_____\\___| \\_/ \\___|_| |_| |_|_|\\___|_| |_|\\__|  \\__, |\\___|_|  \\__,_|_| |_|\\__,_|\\___|_| |_|",
        "                                                  |___/                                       "
    };

    static final String[] PROBLEM_BEIM_LESEN = {
        " ____            _     _                  _          _             _                        ",
        "|  _ \\ _ __ ___ | |__ | | ___ _ __ ___   | |__   ___(_)_ __ ___   | |    ___ ___  ___ _ __  ",
        "| |_) | '__/ _ \\| '_ \\| |/ _ | '_ ` _ \\  | '_ \\ / _ | | '_ ` _ \\  | |   / _ / __|/ _ | '_ \\ ",
        "|  __/| | | (_) | |_) | |  __| | | | | | | |_) |  __| | | | | | | | |__|  __\\__ |  __| | | |",
        "|_|   |_|  \\___/|_.__/|_|\\___|_| |_| |_| |_.__/ \\___|_|_| |_| |_| |_____\\___|___/\\___|_| |_|"
    };

    /*
     * Stringvariablen zur leichteren Veränderung der Ausgaben
     */
    static final String INPUT_STREAM_NICHT_GESCHLOSSEN = "Input Stream konnte nicht geschlossen werden.";
    static final String LIVES_LEFT = "Verbleibende Leben: ";
    static final String KEY_COLLECTED = "Schlüssel gefunden: ";
    static final String NEUES_LEVEL_GENERIEREN = "Zufälliges Level generieren [F1]";
    static final String FORTSETZEN = "Spiel fortsetzen [F2]";
    static final String LADEN = "Spielstand laden [F3]";
    static final String SPEICHERN_BEENDEN = "Spiel speichern und beenden [F4]";
    static final String BEENDEN = "Spiel beenden [F5]";
    static final String LEGENDE = "Legende anzeigen [F6]";
    static final String NEUSTART = "Level neu starten. [F7]";
    static final String MENU = "Menü anzeigen [ESC]";
    static final String VIEL_SPASS = "Viel Spaß!!!";
    static final String INTRO_2 = "Aber Vorsicht, mehrere Hindernisse werden versuchen, sie aufzuhalten.";
    static final String INTRO_1 = "Finden sie einen Schlüssel, offnen sie den Ausgang und entkommen sie.";
    static final String ZUM_BEWEGEN = "Zum Bewegen die Pfeiltasten benutzen.";
    static final String WILLKOMMEN_BEIM = "Willkommen beim verrückten Labyrinth.";
    static final String TITLE = "Das Verrückte Labyrinth ((c) by Marcel Bruckner)";
    static final String SCHWIERIGKEITSGRAD = "Schwierigkeitsgrad eingeben [1 - 9]";
    static final String SCHWIERIGKEIT_GEWAEHLT = "Schwierigkeitsgrad gewählt: ";
    static final String GELAUFEN = " Schritte gelaufen";
    static final String HINT = "[2] für nächsten Schluessel, [3] für nächsten Ausgang";
    static final String SAVE_NICHT_GEFUNDEN = "Speicherstand nicht gefunden.";

    //Menue
    static final String[] WHOLE_MENUE = {FORTSETZEN, LADEN, SPEICHERN_BEENDEN, BEENDEN, LEGENDE, NEUSTART};

    //Legende
    static final String[] WHOLE_LEGENDE = {"Spieler", "Wand", "Eingang", "Ausgang", "Statisches Hindernis", "Dynamisches Hindernis", "Schlüssel", "Power Up"};
}
