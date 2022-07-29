package Player;

import DevSettings.DevSettings;
import Game.GameSession;
import Miscellaneous.Miscellaneous;

import java.util.*;

/**
 * Az emberi játékoshoz tartozó minden adattagot és metódust tartalmazó class.
 */
public class Player {
    public String name;
    public char id;
    public int ownedFields;
    public int ownedDices;
    public String color;

    public Player(String name, char id, int ownedFields, int ownedDices, String color) {
        this.name = name;
        this.ownedFields = ownedFields;
        this.ownedDices = ownedDices;
        this.color = color;
        this.id = id;
    }

    /**
     * A játék session-ben megnézi, hogy a paraméterben kapott sor és oszlop
     * indexekkel jelölt mező a játékos játszható mezője-e, tud e erről a mezőről támadni.
     * Ha nem, akkor a hibának megfelelő visszajelzést ad.
     *
     * @param session Egy játék session
     * @param sor A vizsgált mező sor indexe
     * @param oszlop A vizsgált mező oszlop indexe
     * @return Igaz, ha játszható a vizsgált mező; hamis, ha nem
     */
    public boolean ValidField (GameSession session, int sor, int oszlop) {
        boolean helyes = false;

        if ( oszlop >= 0 && oszlop < session.table.length) {
            if (sor >= 0 && sor < session.table.length) {
                if (session.table[sor][oszlop].ownerID == this.id) {
                    if (session.table[sor][oszlop].numberOfDices >= 2) {
                        helyes = true;
                    } else {
                        System.out.println("Ezen a mezon nincs legalabb 2 kocka, valasz olyat, amin van!");
                    }
                } else {
                    System.out.println("Ez " + session.table[sor][oszlop].ownerID + " mezoje, valasz olyan mezot ami a tied!");
                }
            } else {
                System.out.println("Rossz sor, valassz olyat ami rajta van a tablan!");
            }
        } else {
            System.out.println("Rossz oszlop, valassz olyat ami rajta van a tablan!");
        }

        return helyes;
    }

    /**
     * Megállapítja, hogy a paraméterben kapott sor és oszlop
     * indexekkel jelölt mezőről milyen irányokban lehet támadni.
     *
     * @param session Egy játék session
     * @param sor A vizsgált mező sor indexe
     * @param oszlop A vizsgált mező oszlop indexe
     * @return Egy "TreeMap(String, String)", ami tartalmazza a lehetséges irányokat (rövidítés, teljes név)
     */
    public TreeMap<String, String> ValidDirections (GameSession session, int sor, int oszlop) {

        TreeMap<String, String> iranyok = new TreeMap<>() {
            @Override
            public String toString() {
                final StringBuilder builder = new StringBuilder();
                builder.append("( ");
                for (final Map.Entry<String, String> entry : this.entrySet())
                {
                    builder.append(entry.getValue());
                    builder.append(", ");
                }

                builder.delete(builder.length() - 2, builder.length());
                builder.append(" ) ");

                return builder.toString();
            }
        };

        //try felfele
        try {
            if (session.table[sor - 1][oszlop].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.put("F", "(F)el");
            }
        } catch (Exception ignored) {}

        //try lefele
        try {
            if (session.table[sor + 1][oszlop].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.put("L", "(L)e");
            }
        } catch (Exception ignored) {}

        //try balra
        try {
            if (session.table[sor][oszlop - 1].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.put("B", "(B)alra");
        }
        } catch (Exception ignored) {}

        //try jobbra
        try {
            if (session.table[sor][oszlop + 1].ownerID !=  session.table[sor][oszlop].ownerID) {
                iranyok.put("J", "(J)obbra");
            }
        } catch (Exception ignored) {}

        return iranyok;
    }

    /**
     * Az emberi játékos támadás metódusa.
     *
     * @param session Egy játék session
     * @return Igaz, ha már nem lesz több támadása, hamis ha igen
     */
    public boolean Attack (GameSession session) {
        if (!DevSettings.isPlayerMindigAtadjaAKoret()) {
            String response;
            boolean helyes = false;
            boolean joirany = false;
            int oszlop;
            int sor;

            TreeMap<String, String> iranyok;

            do {
                System.out.print("\nMelyik mezorol szeretnel tamadni? (pl.: A2, j8) [\"END\", hogy befejezd a korod] ");
                Scanner in = new Scanner(System.in);

                try {
                    response = in.nextLine().toUpperCase(Locale.ROOT);

                    if (response.equals("END")) return true;

                    oszlop = response.charAt(0) - 65;
                    sor = Integer.parseInt(response.substring(1)) - 1;

                    if (helyes = ValidField(session, sor, oszlop)) {
                        do {
                            iranyok = ValidDirections(session, sor, oszlop);

                            if (iranyok.isEmpty()) {
                                System.out.println("Errol a mezorol nem tudsz tamadni!");
                                return false;
                            } else System.out.print("Merre szeretnel tamadni? " + iranyok);

                            response = in.nextLine().toUpperCase(Locale.ROOT);

                            if (iranyok.containsKey(response)) {
                                joirany = true;

                            } else System.out.println("Helyesen add meg az inputot!");

                            if (joirany) {
                                Miscellaneous.combat(session, response, sor, oszlop);
                            }

                        } while (!joirany);
                    }
                } catch (Exception e) {
                    System.out.println("Helyesen add meg az inputot!");
                }
            } while (!helyes);

            return false;
        }

        return true;


    }
}
