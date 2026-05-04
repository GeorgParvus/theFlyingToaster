/**
 * Definiert, ob der Toast-Schacht aktuell belegt
 * oder leer/ausgeworfen ist.
 */
public enum SchachtZustand {

    EINGEFUEHRT("EINGEFUEHRT"),
    AUSGEWORFEN("AUSGEWORFEN");

    private String zustand;

    /**
     * Konstruktor für den Enum-Wert .
     */
    SchachtZustand(String zustand) {
        this.zustand = zustand;
    }
}
