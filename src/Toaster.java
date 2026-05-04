import static java.lang.Thread.sleep;

/*
*
 * Basisklasse für einen Toaster.
 * Beinhaltet die Logik zum Einführen, Auswerfen und asynchronen Toasten.
*/
public class Toaster {

    // --- Felder ---
    protected int id;                       // Eindeutige Identifikationsnummer
    protected String name;                  // Name des Toasters
    protected int schaechte = 2;            // Anzahl der Schächte (2)
    protected String farbe;                 // Farbe des Toasters
    protected ToastZustand toastZustand = ToastZustand.UNGETOASTET; // Aktuelle Bräunung
    protected int zeit;                     // Dauer des Toastvorgangs in Sekunden
    protected SchachtZustand schachtZustand = SchachtZustand.AUSGEWORFEN;// Mechanischer Zustand (oben/unten)
    
    // Thread-Management für nicht-blockierende Ausführung
    protected Thread toastThread;           // Hintergrund-Thread für den Toastvorgang
    protected boolean istToasting = false;  // Flag, ob der Toaster gerade aktiv ist


    /**
     * Standardkonstruktor.
     */
    public Toaster() {}

    /**
     * Simuliert das Herunterdrücken des Hebels/Einführen des Toasts.
     */
    public void toastEinfuehren() {
        if(this.schachtZustand == SchachtZustand.EINGEFUEHRT) {
            System.out.println("Toasts sind bereits eingeführt.");
        } else {
            this.schachtZustand = SchachtZustand.EINGEFUEHRT;
            this.toastZustand = ToastZustand.UNGETOASTET; // Neuer Toast ist immer ungetoastet
            System.out.println("Toast eingeführt.");
        }
    }

    /**
     * Wirft den Toast aus. 
     * Wenn ein Toastvorgang läuft, wird dieser durch einen Thread-Interrupt abgebrochen.
     */
    public void toastAuswerfen() {
        if (this.schachtZustand == SchachtZustand.AUSGEWORFEN) {
            System.out.println("Es befindet sich kein Toast im Schacht.");
            return;
        }

        // Falls noch getoastet wird, Thread stoppen
        if (istToasting && toastThread != null) {
            toastThread.interrupt(); 
            istToasting = false;
            System.out.println("Toastvorgang wurde manuell abgebrochen!");
        }

        this.schachtZustand = SchachtZustand.AUSGEWORFEN;
        System.out.println("Toast ausgeworfen. Endergebnis: " + this.toastZustand.getZustand());
    }

    /**
     * Startet den Toastvorgang in einem eigenen Thread.
     *
     */
    public void toasten() {
        // Validierung der Voraussetzungen
        if (this.schachtZustand != SchachtZustand.EINGEFUEHRT) {
            System.out.println("Fehler: Bitte führen Sie erst den Toast ein.");
            return;
        }
        if (istToasting) {
            System.out.println("Info: Toaster läuft bereits!");
            return;
        }

        istToasting = true;
        // Erstellung des Hintergrund-Threads
        toastThread = new Thread(() -> {
            System.out.println("\n[System] " + name + " startet Toastvorgang (" + zeit + "s)...");
            ToastZustand[] werte = ToastZustand.values();
            int zustandIndex = 0;
            
            try {
                for (int i = 0; i < this.zeit; i++) {
                    // Prüfen, ob der Thread von  durch toastAuswerfen gestoppt wurde
                    if (Thread.interrupted()) throw new InterruptedException();
                    
                    // Alle 15 Sekunden erhöht sich der Bräunungsgrad
                    if (i > 0 && i % 15 == 0 && zustandIndex < werte.length - 1) {
                        zustandIndex++;
                        this.setToastZustand(werte[zustandIndex]);
                        System.out.println("\n[Status] " + this.name + " meldet: " + this.toastZustand.getZustand());
                    }
                    sleep(1000); // Eine Sekunde warten
                }
                
                // Reguläres Ende des Vorgangs
                istToasting = false;
                System.out.println("\n[System] " + this.name + ": Zeit abgelaufen (Pling!).");
                this.toastAuswerfen(); // Automatischer Auswurf am Ende
            } catch (InterruptedException e) {

            }
        });
        toastThread.start(); // Startet die run()-Methode im Hintergrund
    }

    /**
     * Speichert Toaster-Objekt in ToasterMemory Map.
     */
    public void save() {
        ToasterMemory.toasterMemory.put(this.id, this);
    }

    // --- Getter und Setter ---

    protected void setToastZustand(ToastZustand toastZustand) {
        this.toastZustand = toastZustand;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setFarbe(String farbe) {
        this.farbe = farbe;
    }

    public void setZeit(int zeit) {
        this.zeit = zeit;
    }
}
