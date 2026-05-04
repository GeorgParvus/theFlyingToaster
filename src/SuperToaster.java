import static java.lang.Thread.sleep;

/**
 * Erweiterte Toaster-Klasse.
 * Der SuperToaster kann überhitzen und im Hintergrund abkühlen.
 */
public class SuperToaster extends Toaster {

    // --- Zusätzliche Attribute ---
    int aktuelleTemperatur = 24;  // Startet bei Zimmertemperatur
    int maxTemperatur = 500;      // Überhitzungsgrenze
    boolean ueberhitzt = false;   // Sicherheitsabschaltung aktiv?

    public SuperToaster() {
        super();
    }

    /**
     * Überschreibt die toasten-Logik und ergänzt eine Temperaturprüfung.
     */
    @Override
    public void toasten() {
        if (this.schachtZustand != SchachtZustand.EINGEFUEHRT) {
            System.out.println("Fehler: Bitte führen Sie erst den Toast ein.");
            return;
        }

        if (ueberhitzt) {
            System.out.println("Sicherheitshinweis: SuperToaster ist noch zu heiß! (" + aktuelleTemperatur + "°C)");
            return;
        }
        
        if (istToasting) {
            System.out.println("Info: SuperToaster läuft bereits!");
            return;
        }

        istToasting = true;
        // Hintergrund-Thread für den Toastvorgang
        toastThread = new Thread(() -> {
            System.out.println("\n[System] Starte Super-Toastvorgang (" + zeit + "s)...");
            ToastZustand[] werte = ToastZustand.values();
            int zustandIndex = 0;

            try {
                for (int i = 0; i < this.zeit; i++) {
                    if (Thread.interrupted()) throw new InterruptedException();

                    // Bräunungsfortschritt
                    if (i > 0 && i % 15 == 0 && zustandIndex < werte.length - 1) {
                        zustandIndex++;
                        this.setToastZustand(werte[zustandIndex]);
                        System.out.println("\n[Status] SuperToaster: " + this.toastZustand.getZustand());
                    }

                    // Temperatur steigt pro Sekunde um 5 Grad
                    this.aktuelleTemperatur += 5;
                    
                    // Not-Abschaltung bei Überhitzung
                    if (this.aktuelleTemperatur >= this.maxTemperatur) {
                        this.ueberhitzt = true;
                        System.out.println("\n!!! NOT-STOPP: SuperToaster überhitzt bei " + aktuelleTemperatur + "°C !!!");
                        istToasting = false;
                        this.toastAuswerfen(); // Toast wird sofort ausgeworfen
                        abkuehlen();          // Hintergrund-Kühlung starten
                        return;
                    }
                    sleep(1000);
                }
                
                //  Ende des Toastvoergangs
                istToasting = false;
                System.out.println("\n[System] SuperToaster: Vorgang erfolgreich abgeschlossen.");
                this.toastAuswerfen();
            } catch (InterruptedException e) {

            }
        });
        toastThread.start();
    }

    /**
     * Startet einen separaten Thread, der die Temperatur langsam senkt.
     * Sobald 350°C (500 - (5 * 30)) unterschritten sind, ist der Toaster wieder einsatzbereit.
     */
    public void abkuehlen() {
        Thread abkuehlThread = new Thread(() -> {
            System.out.println("[System] Automatische Abkühlung aktiv...");
            while (aktuelleTemperatur > 24) {
                try {
                    sleep(500); // Alle 0,5 Sekunden kühlen
                    aktuelleTemperatur -= 10;
                    
                    // Reaktivierungsschwelle: 350°C (Puffer für einen 30s Toast)
                    if (ueberhitzt && aktuelleTemperatur <= 350) {
                        ueberhitzt = false;
                        System.out.println("\n[System] Info: SuperToaster ist wieder abgekühlt und einsatzbereit (" + aktuelleTemperatur + "°C).");
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
            aktuelleTemperatur = 24;
            System.out.println("[System] SuperToaster hat wieder Zimmertemperatur (24°C).");
        });
        abkuehlThread.start();
    }
}
