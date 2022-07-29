package Bots;

import Game.GameSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Ez a class egy gépi ellenfél adattagjait és alap metódusait tartalmazza.
 */
public class Bot{
    public String name;
    public char id;
    public int ownedFields;
    public int ownedDices;
    public String color;
    public int diff;

    public Bot() {
        this.name = "";
        this.id = ' ';
        this.ownedFields = 0;
        this.ownedDices = 0;
        this.color = "";
        this.diff = 0;
    }

    public Bot(int number) {
        switch (number) {
            case 1:
                this.color = "\033[32m";
                this.name = "Green";
                this.id = 'G';
                this.ownedDices = 0;
                this.ownedFields = 0;
                break;
            case 2:
                this.color = "\033[33m";
                this.name = "Yellow";
                this.id = 'Y';
                this.ownedDices = 0;
                this.ownedFields = 0;
                break;
            case 3:
                this.color = "\033[34m";
                this.name = "Blue";
                this.id = 'B';
                this.ownedDices = 0;
                this.ownedFields = 0;
                break;
            case 4:
                this.color = "\033[36m";
                this.name = "Cyan";
                this.id = 'C';
                this.ownedDices = 0;
                this.ownedFields = 0;
                break;
        }
    }

    /**
     * A gépi ellenfél alap támadás metódusa, ami minden nehézségnél felül van definiálva.
     *
     * @see EasyBot
     * @see MediumBot
     * @see HardBot
     *
     * @param session A jelenlegi játék sessionje
     * @return Igaz, ha már nem lesz több támadása, hamis ha igen.
     */
    public boolean attack(GameSession session) {
        return true;
    }

    /**
     *  A megadott játék session táblája és a két koordináta alapján megálapítja,
     *  hogy hány olyan irány van, amerre a paraméterben kapott mezőről lehet támadni.
     *
     * @param session Egy játék session
     * @param sor A vizsgált mező sor koordinátája
     * @param oszlop A vizsgált mező oszlop koordinátája
     * @return Egy String típusú lista, ami tartalmazza a támadható irányok betűjeleit
     */
    public List<String> validDirections(GameSession session, int sor, int oszlop) {
        List<String> iranyok = new ArrayList<String>();

        //try felfele
        try {
            if (session.table[sor - 1][oszlop].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.add("F");
            }
        } catch (Exception ignored) {}

        //try lefele
        try {
            if (session.table[sor + 1][oszlop].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.add("L");
            }
        } catch (Exception ignored) {}

        //try balra
        try {
            if (session.table[sor][oszlop - 1].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.add("B");
            }
        } catch (Exception ignored) {}

        //try jobbra
        try {
            if (session.table[sor][oszlop + 1].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.add("J");
            }
        } catch (Exception ignored) {}

        return iranyok;
    }

    /**
     * Ellenőrzi, hogy a paraméterben kapott sor és oszlop koordinátákkal
     * megadottt mező ami a megadott session tábláján helyezkedik el,
     * ehhez a bothoz tartozik-e.
     *
     * @param session Egy játék session
     * @param sor A vizsgált mező sor koordinátája
     * @param oszlop A vizsgált mező oszlop koordinátája
     * @return Igaz, ha a ehhez a bothoz tartozik a mező, hamis, ha nem
     */
    public boolean validField(GameSession session, int sor, int oszlop) {
        boolean helyes = false;

        if ( oszlop >= 0 && oszlop < session.table.length) {
            if (sor >= 0 && sor < session.table.length) {
                if (session.table[sor][oszlop].ownerID == this.id) {
                    if (session.table[sor][oszlop].numberOfDices >= 2) {
                        helyes = true;
                    }
                }
            }
        }

        return helyes;
    }

    /**
     * Megállapítja, hogy ennek a botnak vannak-e játszható mezői.
     *
     * @param session Egy játék session
     * @return Igaz, ha van, hamis, ha nem
     */
    public boolean HavePlayableFields (GameSession session) {
        List<Boolean> fields = new ArrayList<>();
        for (int i = 0; i < session.table.length; i++) {
            for (int j = 0; j < session.table[i].length; j++) {
                if (validField(session, i, j) && !validDirections(session, i, j).isEmpty()) fields.add(true);
            }
        }

        return fields.contains(true);
    }
}
