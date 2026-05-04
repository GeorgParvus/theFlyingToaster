import java.util.*;

/**
 * Dient als einfacher In-Memory Speicher für alle erstellten Toaster.
 * Über eine HashMap können Toaster anhand ihrer ID im Programm abgerufen werden.
 */
public class ToasterMemory {

    /**
     * Map, die IDs auf Toaster-Objekte abbildet.
     */
    public static Map<Integer, Toaster> toasterMemory = new HashMap<>();

}
