package Miscellaneous;

import DevSettings.DevSettings;
import Game.Game;
import Game.GameSession;
import Game.Field;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * Ez a class tartalmaz minden olyan metódust, ami a konzolra kiiratáshoz kell és
 * egyébb mellékszámítások végző metódusokat.
 *
 * Két fontos adattagja van: height és width
 */
public class Miscellaneous {
    public static int height = 15, width = 70;
    public static final String ANSI_RESET = "\033[0m";
    public static final String ANSI_RED = "\033[31m";


    /**
     * A képernyőn lévő dolgok "törlése".
     * (Ha az IDEA-ban futtatod a játékot, nem működik, csak parancssorban.)
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * A paraméterben megadott szöveg megjelenítése a képernyőn.
     *
     * A paraméterben kért String tömb hossza ugyan annyi kell hogy legyen,
     * mint a paraméterben várt "rows" értéke.
     *
     * Ha a String tömbben van olyan sor, aminek a hossza nagyobb mint a "column"
     * paraméter értéke, akkor az a sor rosszul fog megjelenni.
     *
     * @param text A kiiratni kívánt szöveg, soronként, egy String tömbben
     * @param rows A megjelenített terület sorainak száma
     * @param columns A megjelenített terület oszlopainak száma
     * @param orientation A kiiratni kívánt szöveg tájolása. Megadható irányok: "bal", "jobb", "közép".
     *                    Ha helytelenül adod meg az irányt, akkor alapértelmezetten balra igazítja a szöveget.
     * @param waitForResponse Egy logikai paraméter, amivel megadhatod, hogy a metódus
     *                        a kiiratás után várjon-e a felhasználó válaszára, vagy ne.
     *                        Igaz esetén vár az inputra, hamis esetén nem vár.
     * @param response Ha a "waitForResponse" paraméter igaz, akkor ebbe a paramétertagba írja bele
     *                 a kapott inputot, és adja vissza.
     *
     * @throws IndexOutOfBoundsException Ha nem megfelelő módon adod meg a kiiratáshoz szükséges paramétereket
     * @return A "response" paramétert
     */
    public static String out(String[] text, int rows, int columns, String orientation, Boolean waitForResponse, String response) {
        clearScreen();

        String[] lines = new String[rows];

        Arrays.fill(lines, "");

        for (int i = 0; i < columns; i++)
        {
            lines[0] += "=";

            for (int j = 1; j < rows - 1; j++)
            {
                lines[j] = "||";

                for (int k = 0; k < columns - 4 - text[j - 1].length(); k++)
                {
                    lines[j] += " ";

                    switch (orientation)
                    {
                        case "bal":
                            if (k == 0)
                            {
                                lines[j] += text[j - 1];
                            }
                            break;

                        case "közép":
                            if (k == (columns / 2) - (text[j - 1].length() / 2))
                            {
                                lines[j] = insert(lines[j], k, text[j - 1]);
                            }
                            break;

                        case "jobb":
                            if (k == columns - 4 - text[j - 1].length() - 1)
                            {
                                lines[j] = insert(lines[j], k, text[j - 1]);
                            }
                            break;

                        default:
                            if (k == 0)
                            {
                                lines[j] += text[j - 1];
                            }
                            break;
                    }
                }

                lines[j] += "||";
            }

            lines[lines.length - 1] += "=";
        }


        for (String s : lines) {
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '=' || s.charAt(j) == ' ' || s.charAt(j) == '|') {
                    System.out.print(ANSI_RED + s.charAt(j) + ANSI_RESET);
                } else {
                    System.out.print(s.charAt(j));
                }
            }
            System.out.println();
        }

        if (waitForResponse)
        {
            System.out.print("\nYour response: ");
            Scanner in = new Scanner(System.in);
            response = in.nextLine();
        }

        return response;
    }

    /**
     * A paraméterben kapott szöveg megadott helyébe beilleszti a másik paraméterben kapott szöveget.
     *
     * @param toWhich A szöveg, amibe be szeretnénk illeszteni
     * @param where A hely, ahova be szeretnénk illeszteni
     * @param what A szöveg, amit be szeretnénk illeszteni
     * @return A módosított szöveg
     */
    public static String insert(String toWhich, int where, String what) {
        // Create a new string
        StringBuilder newString = new StringBuilder();

        for (int i = 0; i < toWhich.length(); i++) {

            // Insert the original string character
            // into the new string
            newString.append(toWhich.charAt(i));

            if (i == where) {

                // Insert the string to be inserted
                // into the new string
                newString.append(what);
            }
        }

        // return the modified String
        return newString.toString();
    }

    /**
     * Készít egy Miscellaneous class "height" adattag hosszú, üres String-ekkel feltöltött String tipusú tömböt.
     *
     * @see Miscellaneous Miscellaneous.height
     *
     * @return az elkészített String tömböt
     */
    public static String[] newStringArray() {
        String[] array = new String[Miscellaneous.height];

        Arrays.fill(array, "");

        return array;
    }

    /**
     * Megállapítja a paraméterben kapott játék session jelenlegi állását
     * és ezt egy String tömbben adja visza.
     * Minden "sor"-ban egy játékos eredményei vannak.
     *
     * @param session Egy játék session
     * @return egy String tömb, ami tartalmazza a játék állását
     */
    public static String[] checkScores(GameSession session) {
        clearScreen();
        String[] scores = new String[1 + session.bots.length];

        Arrays.fill(scores, "");

        session.player.ownedFields = 0;
        session.player.ownedDices = 0;

        for (int i = 0; i < session.table.length; i++){
            for (int j = 0; j < session.table[i].length; j++){
                if (session.table[i][j].ownerID == session.player.id) {
                    session.player.ownedFields++;
                    session.player.ownedDices += session.table[i][j].numberOfDices;
                }
            }
        }

        if (session.player.ownedFields > 0) scores[0] = session.player.name + ": fields: " + session.player.ownedFields + ", dices: " + session.player.ownedDices;
        else scores[0] = session.player.name + " kiesett!";

        for (int i = 0; i < session.bots.length; i++){
            session.bots[i].ownedFields = 0;
            session.bots[i].ownedDices = 0;
        }

        for (int i = 0; i < session.table.length; i++){
            for (int j = 0; j < session.table[i].length; j++){
                for (int k = 0; k < session.bots.length; k++){
                    if (session.table[i][j].ownerID == session.bots[k].id) {
                        session.bots[k].ownedFields++;
                        session.bots[k].ownedDices += session.table[i][j].numberOfDices;
                    }
                }
            }
        }

        for (int i = 1; i < scores.length; i++){
            if (session.bots[i - 1].ownedFields > 0) scores[i] = session.bots[i - 1].name + ": fields: " + session.bots[i - 1].ownedFields + ", dices: " + session.bots[i - 1].ownedDices;
            else scores[i] = session.bots[i - 1].name + " kiesett!";
        }

        return scores;
    }

    /**
     * A paraméterben kapott táblából készít egy String tömböt.
     *
     * @param table A tábla,amiből a String tömb készül
     * @param tableOut A String tömb, amibe majd a tábla belekerül
     */
    public static void tableToStringArray(Field[][] table, String[] tableOut) {
        int m = 2;
        tableOut[1] = "          ";

        for (int i = 0; i < table.length; i++) {
            tableOut[1] += (char) (i + 65) + "     ";
        }

        for (int i = 0; i < table.length; i++) {
            tableOut[m] += "#" + (i + 1) + "  | ";
            for (int j = 0; j < table[i].length; j++) {
                tableOut[m] += table[i][j].ownerID + " " + table[i][j].numberOfDices + " | ";
            }

            tableOut[m] = tableOut[m].trim();

            m++;
            tableOut[m] = "    ";

            for (int j = 4; j < tableOut[m - 1].length(); j++) {
                tableOut[m] += "-";
            }

            m++;
        }

        tableOut[m - 1] = "";

    }

    /**
     * Egy kör kiiratása a konzolra.
     *
     * @param session Egy játék session
     * @param info Egy String tömb, ami tartalmazza, melyik kör van és ez kinek a köre
     * @param table Egy String tömb, amiben benne van a kiírni kívánt tábla
     * @param scores A játékosok eredményei
     * @param rows A konzolra kiírt terület sorainak száma
     * @param columns A konzolra kiírt terület oszlopainak száma
     *
     * @throws IndexOutOfBoundsException ha a "rows" nem egyenlő (tableOut.length + 1) + scores.length + info.length
     */
    public static void roundOut(GameSession session, String[] info, String[] table, String[] scores, int rows, int columns) {
        String[] lines = new String[rows];
        int c = 0;

        String[] sum = new String[table.length + scores.length + info.length];

        for (int i = 0; i < table.length; i++) {
            sum[i] = table[i];
            c = i;
        }

        for (String s : info) {
            sum[c] = s;
            c++;
        }

        for (String score : scores) {
            sum[c] = score;
            c++;
        }

        Arrays.fill(lines, "");

        for (int i = 0; i < columns; i++){
            lines[0] += "=";

            for (int j = 1; j < rows - 1; j++){
                lines[j] = "||";

                for (int k = 0; k < columns - 4 - sum[j - 1].length(); k++){

                    //közepre igazítva
                    if (k == ((columns / 2) - 1) - (sum[j - 1].length() / 2))
                    {
                        lines[j] = insert(lines[j], k, sum[j - 1]);
                    }

                    lines[j] += " ";
                }

                lines[j] += "||";
            }

            lines[lines.length - 1] += "=";
        }

        for (int i = 0; i < lines.length; i++){
            for (int j = 0; j < lines[i].length(); j++){
                if (lines[i].charAt(j) == '=' || lines[i].charAt(j) == ' ' || lines[i].charAt(j) == '|'){
                    System.out.print(ANSI_RESET + lines[i].charAt(j));
                } else if (lines[i].charAt(j) == session.player.id){
                    if (i > 2) {
                        System.out.print(session.player.color + lines[i].charAt(j));
                    }
                } else {
                    boolean flag = false;
                    if (i > 2) {
                        for (int k = 0; k < session.bots.length; k++){
                            if (lines[i].charAt(j) == session.bots[k].id) {
                                System.out.print(session.bots[k].color + lines[i].charAt(j));
                                flag = true;
                                break;
                            }
                        }
                    }

                    if (!flag) System.out.print(lines[i].charAt(j));
                }
            }

            System.out.println();
        }
    }

    /**
     * Egy játékvisszajátszás adatainak megjelenítése a konzolon.
     *
     * @param info Egy String tömb, ami tartalmazza, melyik kör van és ez kinek a köre
     * @param table Egy String tömb, amiben benne van a kiírni kívánt tábla
     * @param scores A játékosok eredményei
     * @param attack Egy cselekvés részleteit tartalmazó String tömb
     * @param rows A konzolra kiírt terület sorainak száma
     * @param columns A konzolra kiírt terület oszlopainak száma
     *
     * @throws IndexOutOfBoundsException ha a "rows" nem egyenlő (tableOut.length + 1) + attack.length + scores.length + info.length
     */
    public static void roundOutPlayback(String[] info, String[] table, String[] scores, String[] attack, int rows, int columns) {
        String[] lines = new String[rows];
        int c = 0;

        String[] sum = new String[table.length + scores.length + info.length + attack.length];

        for (int i = 0; i < table.length; i++) {
            sum[i] = table[i];
            c = i;
        }

        for (String s : info) {
            sum[c] = s;
            c++;
        }

        for (String s : attack) {
            sum[c] = s;
            c++;
        }

        for (String score : scores) {
            sum[c] = score;
            c++;
        }

        Arrays.fill(lines, "");

        for (int i = 0; i < columns; i++){
            lines[0] += "=";

            for (int j = 1; j < rows - 1; j++){
                lines[j] = "||";

                for (int k = 0; k < columns - 4 - sum[j - 1].length(); k++){

                    //közepre igazítva
                    if (k == ((columns / 2) - 1) - (sum[j - 1].length() / 2))
                    {
                        lines[j] = insert(lines[j], k, sum[j - 1]);
                    }

                    lines[j] += " ";
                }

                lines[j] += "||";
            }

            lines[lines.length - 1] += "=";
        }

        for (String s : lines) {
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == '=' || s.charAt(j) == '|' || s.charAt(j) == '-') {
                    System.out.print(ANSI_RED + s.charAt(j) + ANSI_RESET);
                } else System.out.print(s.charAt(j));
            }

            System.out.println();
        }

        try {
            Thread.sleep(DevSettings.getKiiratasIdotartamok());
        } catch (InterruptedException ignored) {}
    }

    /**
     * A paraméterben megadott kockákat kiosztja,
     * a szintén paraméterben megkapott id-val rendelkező,
     * az úgyszintén paraméterben kapott játék session tábláján
     * rajta lévő mezők között.
     *
     * @param session Egy játék session
     * @param id A játékos id-je akinek a plussz kockákat kell kiosztani
     * @param dices A kiosztandó kockák száma
     */
    public static void givePlusDices(GameSession session, char id, int dices) {
        if (dices != 0) {
            if (id == session.player.id) {
                do {
                    for (int i = 0; i < session.table.length; i++) {
                        for (int j = 0; j < session.table[i].length; j++) {
                            if (session.table[i][j].ownerID == id && session.table[i][j].numberOfDices < 8) {
                                if (new Random().nextInt(2) == 0 && dices > 0) {
                                    session.table[i][j].numberOfDices++;
                                    dices--;

                                    session.player.ownedDices++;
                                }
                            }
                        }
                    }
                } while (dices != 0);
            } else {
                for (int k = 0; k < session.bots.length; k++) {
                    if (session.bots[k].id == id) {
                        do {
                            for (int i = 0; i < session.table.length; i++) {
                                for (int j = 0; j < session.table[i].length; j++) {
                                    if (session.table[i][j].ownerID == id && session.table[i][j].numberOfDices < 8) {
                                        if (new Random().nextInt(2) == 0 && dices > 0) {
                                            session.table[i][j].numberOfDices++;
                                            dices--;

                                            for (int s = 0; s < session.bots.length; s++) {
                                                if (id == session.bots[s].id) session.bots[s].ownedDices++;
                                            }
                                        }
                                    }
                                }
                            }
                        } while (dices != 0);
                    }
                }
            }
        }

        Game.mainSession = session;
    }

    /**
     * Egy támadás minden dobásainak kiszámítása és megjelenítése a konzolon.
     *
     * @param session Egy játék session
     * @param direction Az irány, amerre támadunk
     * @param attackerRow A mező sora, ahonnan támadunk
     * @param attackerColumn A mező oszlopa, ahonnan támadunk
     */
    public static void combat(GameSession session, String direction, int attackerRow, int attackerColumn) {
        int defenderRow = attackerRow;
        int defenderColumn = attackerColumn;
        String winner = "";

        switch (direction) {
            case "L":
                defenderRow++;
                break;
            case "F":
                defenderRow--;
                break;
            case "J":
                defenderColumn++;
                break;
            case "B":
                defenderColumn--;
                break;
        }

        StringBuilder attackerThrows = new StringBuilder();
        StringBuilder defenderThrows = new StringBuilder();

        attackerThrows.append("Tamado dobasai:");
        defenderThrows.append("Vedekezo dobasok:");

        int attackerDices = session.table[attackerRow][attackerColumn].numberOfDices;
        int defenderDices = session.table[defenderRow][defenderColumn].numberOfDices;

        int attackerSum = 0;
        int defenderSum = 0;

        for (int i = 0; i < attackerDices; i++) {
            int tmp = new Random().nextInt(6) + 1;
            attackerThrows.append(" ").append(tmp);
            attackerSum += tmp;
        }

        for (int i = 0; i < defenderDices; i++) {
            int tmp = new Random().nextInt(6) + 1;
            defenderThrows.append(" ").append(tmp);
            defenderSum += tmp;
        }

        attackerThrows.append(", osszeg: ").append(attackerSum);
        defenderThrows.append(", osszeg: ").append(defenderSum);

        System.out.print(attackerThrows + " || " + defenderThrows);

        if (attackerSum > defenderSum) {
            defenderDices = attackerDices - 1;
            session.table[defenderRow][defenderColumn].ownerID = session.table[attackerRow][attackerColumn].ownerID;
            attackerDices = 1;

            winner = "Tamado nyert!";
        } else {
            attackerDices = 1;

            winner = "Vedekezo nyert!";
        }

        System.out.println("\n" + winner);

        session.table[attackerRow][attackerColumn].numberOfDices = attackerDices;
        session.table[defenderRow][defenderColumn].numberOfDices = defenderDices;

        session.addToSave(attackerRow, attackerColumn,
                defenderRow, defenderColumn,
                attackerThrows.toString(), defenderThrows.toString(),
                winner);

        session.addToSave();

        Game.mainSession = session;

        try {
            Thread.sleep(DevSettings.getKiiratasIdotartamok());
        } catch (InterruptedException ignored) {}
    }

    /**
     * Megállapítja, hogy a paraméterben kapott játék session-ben hány aktív játékos van.
     *
     * @param session Egy játék session
     * @return Az aktív játékosok száma
     */
    public static int existsAvailablePlayer(GameSession session) {
        int c = 0;
        for (int i = 0; i < session.bots.length; i++) {
            if (session.bots[i].ownedFields > 0) c++;
        }

        if (session.player.ownedFields > 0) c++;

        return c;
    }

    /**
     * A játék session nyertesének megállapítása
     *
     * @param session Egy játék session
     * @return A nyertes játékos neve
     */
    public static String winner(GameSession session) {
        if (session.player.ownedFields > 0) return session.player.name;
        else {
            for (int i = 0; i < session.bots.length; i++) {
                if (session.bots[i].ownedFields > 0) return session.bots[i].name;
            }
        }

        return "";
    }

    /**
     * Egy játék mentés file betöltése.
     *
     * @param file A betölteni kívánt file
     */
    public static void load(File file) {
        List<String> save = new ArrayList<>();

        try {
            Scanner myReader = new Scanner(file);

            while (myReader.hasNextLine()) {
                save.add(myReader.nextLine());
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int tableSize = (int) (Math.sqrt(save.get(0).split("::")[1].split(";").length));
        String presentPlayer = "";

        String[] tableOut = new String[tableSize * 2 + 3];
        Arrays.fill(tableOut, "");

        String[] scores = new String[(tableSize / 2)];
        Arrays.fill(scores, "");

        Field[][] table = new Field[tableSize][tableSize];

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                table[i][j] = new Field('0', 0);
            }
        }

        String[] info = new String[3];
        Arrays.fill(info, "");

        String[] attack = new String[4];
        Arrays.fill(attack, "");

        int row = (tableOut.length + 1) + scores.length + info.length + attack.length;
        int column = 100;

        for (String s : save) {

            switch (s.split("::")[0]) {
                case "T":
                    int c = 0;
                    for (Field[] fields : table) {
                        for (Field field : fields) {
                            field.ownerID = s.split("::")[1].split(";")[c].split("")[0].charAt(0);
                            field.numberOfDices = Integer.parseInt(s.split("::")[1].split(";")[c].split("")[1]);
                            c++;
                        }
                    }
                    Arrays.fill(tableOut, "");
                    tableToStringArray(table, tableOut);
                    break;

                case "S":
                    for (int i = 0; i < scores.length; i++) {
                        scores[i] = s.split("::")[1].split(";")[i];
                    }
                    break;

                case "R":
                    info[0] = s.split("::")[1].split(",")[0] + ". kor";
                    presentPlayer = s.split("::")[1].split(",")[1];
                    info[1] = "Jelenleg " + presentPlayer + " kore van!";

                    Arrays.fill(attack, "");
                    roundOutPlayback(info, tableOut, scores, attack, row, column);
                    break;

                case "A":
                    attack[0] = presentPlayer + " a(z) " + s.split("::")[1].split(";")[0].split(">")[0] + " mezorol tamadott a(z) " +
                            s.split("::")[1].split(";")[0].split(">")[1] + " mezore!";
                    attack[1] = s.split("::")[1].split(";")[1];
                    attack[2] = s.split("::")[1].split(";")[2];

                    roundOutPlayback(info, tableOut, scores, attack, row, column);
                    break;

                default:
                    String[] playMenu = newStringArray();
                    playMenu[4] = "GAME OVER";

                    playMenu[6] = s.split("::")[0];

                    Miscellaneous.out(playMenu, Miscellaneous.height, Miscellaneous.width, "közép", false, "");
            }
        }
    }
}
