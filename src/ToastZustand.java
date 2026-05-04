/**
 * Repräsentiert die verschiedenen Bräunungsstufen eines Toasts.
 */
public enum ToastZustand {

    UNGETOASTET("ungetoastet"),
    LEICHTGETOASTET("leicht getoastet"),
    STARKGETOASTET("stark getoastet"),
    VERBRANNT("verbrannt");

    private String zustand;

    /**
     * Konstruktor für den Enum-Wert .
     */
    ToastZustand(String zustand) {
        this.zustand = zustand;
    }

    /**
     * Gibt die Textbeschreibung des Zustands zurück.
     */
    public String getZustand() {
        return zustand;
    }


}
