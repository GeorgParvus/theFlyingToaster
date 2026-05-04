import java.util.Scanner;

/**
 * Mainklasse für 'The Flying Toaster'.
 * Verwaltet das Hauptmenü und die Benutzerinteraktion mit den Toastern.
 */
public class Main {

    // Globale ID-Vergabe für Toaster
    private static int nextId = 1;

    public static void main(String[] args) {
        boolean run = true;
        Scanner scanner = new Scanner(System.in);
        String input = "0";

        // Hauptschleife des Programms
        while (run) {
            switch (input) {
                case "0": // Hauptmenü-Anzeige
                    System.out.println("\n--- 'The flying Toaster' Hauptmenü ---");
                    System.out.println("[1] Toaster erstellen");
                    System.out.println("[2] Bestehende Toaster anzeigen");
                    System.out.println("[3] Toaster auswählen & benutzen");
                    System.out.println("[4] Programm beenden");
                    System.out.print("Auswahl: ");
                    input = scanner.nextLine();
                    break;

                case "1": // Toaster-Erstellung
                    System.out.println("\n--- Neuen Toaster konfigurieren ---");
                    System.out.print("Name eingeben: ");
                    String name = scanner.nextLine();
                    System.out.print("Farbe eingeben: ");
                    String farbe = scanner.nextLine();
                    System.out.println("Modell wählen: [1] Standard, [2] SuperToaster (mit Temperatur)");
                    String art = scanner.nextLine();

                    Toaster neuerToaster;
                    // Polymorphie: Ein SuperToaster ist auch ein Toaster
                    if (art.equals("2")) {
                        neuerToaster = new SuperToaster();
                    } else {
                        neuerToaster = new Toaster();
                    }

                    // Attribute setzen
                    neuerToaster.setName(name);
                    neuerToaster.setFarbe(farbe);
                    neuerToaster.setId(nextId);
                    neuerToaster.save(); // In der HashMap speichern
                    
                    System.out.println("Erfolg: '" + name + "' wurde mit ID " + nextId + " registriert.");
                    nextId++;
                    input = "0"; // Zurück zum Hauptmenü
                    break;

                case "2": // Liste aller Toaster ausgeben
                    System.out.println("\n--- Registrierte Geräte ---");
                    if (ToasterMemory.toasterMemory.isEmpty()) {
                        System.out.println("Keine Toaster in der Datenbank.");
                    } else {
                        // Iteration über alle gespeicherten Toaster
                        for (Toaster t : ToasterMemory.toasterMemory.values()) {
                            String typ = (t instanceof SuperToaster) ? "SuperToaster" : "Standard-Modell"; // Toasterklasse prüfem
                            System.out.println("ID " + t.id + ": " + t.name + " (" + typ + ", " + t.farbe + ")"); // Infos über Toaster ausgeben
                        }
                    }
                    input = "0";
                    break;

                case "3": // Toaster-Interaktion
                    System.out.print("\nID des gewünschten Toasters eingeben: ");
                    try {
                        int id = Integer.parseInt(scanner.nextLine());
                        Toaster t = ToasterMemory.toasterMemory.get(id);
                        if (t != null) {
                            // Untermenü für den spezifischen Toaster aufrufen
                            toasterInteraktion(t, scanner);
                        } else {
                            System.out.println("Fehler: Toaster mit ID " + id + " nicht gefunden.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Fehler: Bitte geben Sie eine gültige Zahl ein.");
                    }
                    input = "0";
                    break;

                case "4": // Beenden
                    System.out.println("Vielen Dank für die Nutzung von 'The flying Toaster'!");
                    run = false;
                    break;

                default:
                    System.out.println("Ungültige Eingabe.");
                    input = "0";
                    break;
            }
        }
        scanner.close();
    }

    /**
     * Untermenü für die Bedienung eines spezifischen Toasters.
     * @param t Der ausgewählte Toaster.
     * @param scanner Der Scanner für Benutzereingaben.
     */
    private static void toasterInteraktion(Toaster t, Scanner scanner) {
        boolean zurück = false;
        while (!zurück) {
            System.out.println("\n--- Gerät: " + t.name + " (ID " + t.id + ") ---");
            System.out.println("[1] Hebel drücken (Toast einführen)");
            System.out.println("[2] Timer einstellen (aktuell: " + t.zeit + "s)");
            System.out.println("[3] Start (Toasten)");
            System.out.println("[4] Auswurf-Knopf (Manuell beenden)");
            System.out.println("[5] Zurück zur Übersicht");
            System.out.print("Aktion: ");
            String aktion = scanner.nextLine();

            switch (aktion) {
                case "1": t.toastEinfuehren(); break;
                case "2":
                    System.out.print("Dauer in Sekunden: ");
                    try {
                        t.setZeit(Integer.parseInt(scanner.nextLine()));
                    } catch (NumberFormatException e) {
                        System.out.println("Fehler: Ungültige Zeit.");
                    }
                    break;
                case "3": t.toasten(); break; // Läuft im Hintergrund-Thread
                case "4": t.toastAuswerfen(); break; // Bricht Thread ggf. ab
                case "5": zurück = true; break;
                default: System.out.println("Ungültige Aktion."); break;
            }
        }
    }
}
