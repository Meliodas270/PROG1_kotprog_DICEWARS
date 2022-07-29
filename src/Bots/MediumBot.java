package Bots;

import Game.GameSession;
import Miscellaneous.Miscellaneous;

import java.util.ArrayList;
import java.util.List;

/**
 * Egy közepes nehézségű gépi ellenfél, ami örökölte a Bot minden adattagját és metódusát.
 *
 * @see Bot
 */
public class MediumBot extends Bot {
    public MediumBot(int number) {
        super(number);
        this.name += " (M)";
    }

    /**
     * A közepes nehézségű gépi ellenfél támadás metódusa. A logikája,
     * hogy ha van lehetősége támadni, akkor fog támadni.
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
