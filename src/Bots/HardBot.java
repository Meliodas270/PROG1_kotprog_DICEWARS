package Bots;

import Game.GameSession;
import Miscellaneous.Miscellaneous;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy nehéz nehézségű gépi ellenfél, ami örökölte a Bot minden adattagját és metódusát.
 *
 * @see Bot
 */
public class HardBot extends Bot {
    public HardBot(int number) {
        super(number);
        this.name += " (H)";
    }

    /**
     * A megadott játék session táblája és a két koordináta alapján megálapítja,
     * hogy hány olyan irány van, amerre a paraméterben kapott mezőről lehet támadni.
     * Ez a metódus már azt is figyelembe veszi, hogy a paraméterben kapott mezőn legalább anyi kocka van-e,
     * mit azon a mezőn amit meg szeretne támadni.
     *
     * Szóval csak az számít támadható iránynak,
     * ahol a felső két feltétel érvényesül.
     *
     * @param session Egy játék session
     * @param sor     A vizsgált mező sor koordinátája
     * @param oszlop  A vizsgált mező oszlop koordinátája
     * @return Egy String típusú lista, ami tartalmazza a támadható irányok betűjeleit
     */
    @Override
    public List<String> validDirections(GameSession session, int sor, int oszlop) {
        List<String> iranyok = new ArrayList<String>();

        //try felfele
        try {
            if (session.table[sor - 1][oszlop].ownerID != session.table[sor][oszlop].ownerID &&
                    session.table[sor][oszlop].numberOfDices >= session.table[sor - 1][oszlop].numberOfDices) {
                iranyok.add("F");
            }
        } catch (Exception ignored) {}

        //try lefele
        try {
            if (session.table[sor + 1][oszlop].ownerID != session.table[sor][oszlop].ownerID &&
                    session.table[sor][oszlop].numberOfDices >= session.table[sor + 1][oszlop].numberOfDices) {
                iranyok.add("L");
            }
        } catch (Exception ignored) {}

        //try balra
        try {
            if (session.table[sor][oszlop - 1].ownerID != session.table[sor][oszlop].ownerID &&
                    session.table[sor][oszlop].numberOfDices >= session.table[sor][oszlop - 1].numberOfDices) {
                iranyok.add("B");
            }
        } catch (Exception ignored) {}

        //try jobbra
        try {
            if (session.table[sor][oszlop + 1].ownerID != session.table[sor][oszlop].ownerID &&
                    session.table[sor][oszlop].numberOfDices >= session.table[sor][oszlop + 1].numberOfDices) {
                iranyok.add("J");
            }
        } catch (Exception ignored) {}

        return iranyok;
    }

    /**
     * A nehéz nehézségű gépi ellenfél támadás metódusa. A logikája,
     * hogy ha van lehetősége támadni, akkor fog támadni.
     *
     * Lehetőség: van olyan támadható mező, amire érvényesülnek a "validDirections" metódusban
     * megfogalmazott feltételek.
     *
     * @param session A jelenlegi játék sessionje
     * @return Igaz, ha már nem lesz több támadása, hamis ha igen
     */
    @Override
    public boolean attack(GameSession session) {
        if (HavePlayableFields(session)) {
            for (int i = 0; i < session.table.length; i++) {
                for (int j = 0; j < session.table[i].length; j++) {
                    List<String> iranyok = validDirections(session, i, j);

                    if (validField(session, i, j) && !iranyok.isEmpty()) {
                        String irany = "";

                        switch (iranyok.get(0)) {
                            case "L":
                                irany = "lefele";
                                break;
                            case "F":
                                irany = "felfele";
                                break;
                            case "J":
                                irany = "jobbra";
                                break;
                            case "B":
                                irany = "balra";
                                break;
                        }

                        System.out.println(this.name + " a " + ((char) (j + 65)) + "" + (i + 1) + " mezorol " + irany + " tamadt!");

                        Miscellaneous.combat(session, iranyok.get(0), i, j);

                        return false;
                    }
                }
            }
        }

        return true;
    }
}
