package Game;

import Bots.Bot;
import Bots.EasyBot;
import Bots.MediumBot;
import DevSettings.DevSettings;
import Miscellaneous.Miscellaneous;
import Player.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Ez a class tartalmazza az összes olyan metódust, ami a működéséhez szükséges.
 */
public class Game {
    public static GameSession mainSession = new GameSession();

    /**
     * Ez a metódus megjeleníti a játék kezdő képernyőjét, ahol kiválaszthatjuk,
     * hogy új játékot kezdjünk, egy előző játékunkat töltsük be
     * vagy a játékszabályokat olvassuk el.
     */
    public static void menuPage(){
        String[] intro = Miscellaneous.newStringArray();

        String response = "";

        intro[3] = "Udvozollek a Dice Wars-ban!";

        intro[7] = "(J)atek             (B)etoltes";

        intro[intro.length - 4] = "(K)ilepes";

        response = Miscellaneous.out(intro, Miscellaneous.height, Miscellaneous.width, "közép", true, response);

        switch (response.toLowerCase(Locale.ROOT)){
            case "j":
                playMenuPage();
                break;


            case "b":
                loadPage();
                break;

            case "k":
                break;

            default:
                System.out.println("A felsorolt lehetosegek kozul valassz!");

                try {
                    Thread.sleep(DevSettings.getKiiratasIdotartamok());
                } catch(InterruptedException ignored) {}

                menuPage();

                break;
        }
    }

    /**
     * Ez a metódus megjeleníti az új játék elkezdéséhez szükséges képernyőt, ahol
     * kiválaszthatod, hány gépi ellenfél ellen szeretnél játszani.
     *
     */
    public static void playMenuPage(){
        String[] playmenu = Miscellaneous.newStringArray();

        String response = "";

        playmenu[4] = "Hany ellenfelet szeretnel magad ellen?";

        playmenu[6] = "Adj meg egy egesz szamot 1 es 4 kozott!";

        response = Miscellaneous.out(playmenu, Miscellaneous.height, Miscellaneous.width, "közép", true, response);

        initializingGame(response);

        int players = 0;

        do {
            if (mainSession.presentPlayerId == 'P') mainSession.kor++;
            round(mainSession);
            players = Miscellaneous.existsAvailablePlayer(mainSession);
        } while (players > 1);

        playmenu[4] = "JATEK VEGE";

        playmenu[6] = "A nyertes: " + Miscellaneous.winner(mainSession);

        mainSession.saveList.add(playmenu[6]);

        playmenu[8] = "Szeretned elmenteni a jatekot?";
        playmenu[9] = "( (I)gen / (N)em )";

        response = Miscellaneous.out(playmenu, Miscellaneous.height, Miscellaneous.width, "közép", true, response);

        if (response.toUpperCase(Locale.ROOT).equals("I")) {
            mainSession.save();
        }

        menuPage();
    }

    /**
     * Ez a metódus megjeleníti azt a menüt, ahol kiválaszthatod, hogy melyik
     * előző játékot szeretnéd betölteni. Ha nincs előző játék,
     * akkor a "Nincs mentes!" felirat jelenik meg.
     *
     */
    public static void loadPage(){
        File saves = new File("Saves" + File.separator + "saves.txt");

        List<String> listOfFiles = new ArrayList<>();

        try {
            Scanner myReader = new Scanner(saves);

            while (myReader.hasNextLine()) {
                listOfFiles.add(myReader.nextLine());
            }

            myReader.close();
        } catch (FileNotFoundException ignored) {}

        String[] load = new String[listOfFiles.size() + 9];
        Arrays.fill(load, "");

        load[1] = "Betoltes";
        load[load.length - 4] = "(K)ilepes";

        if (listOfFiles.size() > 0) {
            for (int i = 3, j = 0; j < listOfFiles.size(); i++){
                load[i] = (j + 1) + ") " + listOfFiles.get(j);
                j++;
            }
        } else {
            load[3] = "Nincs mentes!";
        }

        String response = "";
        response = Miscellaneous.out(load, load.length, Miscellaneous.width, "közép", true, response);

        try {
            int i = Integer.parseInt(response);

            if (i > 0 && i <= listOfFiles.size()) {
                Miscellaneous.load(new File("Saves" + File.separator + listOfFiles.get(i - 1)));

                Scanner scan = new Scanner(System.in);
                System.out.print("Nyomj meg egy gombot a tovabblepeshez . . . ");
                scan.nextLine();

                menuPage();
            } else {
                System.out.println("A felsorolt lehetosegek kozul valassz!");

                try {
                    Thread.sleep(DevSettings.getKiiratasIdotartamok());
                } catch(InterruptedException ignored) {}

                loadPage();
            }
        } catch (NumberFormatException e) {
            if ("k".equals(response.toLowerCase(Locale.ROOT))) {
                menuPage();
            } else {
                System.out.println("A felsorolt lehetosegek kozul valassz!");

                try {
                    Thread.sleep(DevSettings.getKiiratasIdotartamok());
                } catch(InterruptedException ignored) {}

                loadPage();
            }
        }
    }

    /**
     * Ez a metódus létrehoz egy új session-t amikor elkezdesz egy új játékot,
     * inicializálja a játékost,
     * random feltölti a pályát,
     * random generálja a gépi ellenfelek nehézségeit,
     * elmenti mikor készült el a session,
     * és ezt a mainSession globális változóba el is menti.
     *
     * @see GameSession
     * @see Bot
     * @see Player
     * @see Field
     *
     * @param numOfBots Hány bot ellen szeretnél játszani?
     *
     */
    public static void initializingGame(String numOfBots){
        // TODO: 2021. 04. 05. bármennyi botra működjön az inicializáslás 
        int num = 0;

        try {
            num = Integer.parseInt(numOfBots);
        } catch(Exception e) {
            playMenuPage();
        }

        num += 1;

        if (2 <= num && num <= 5) {
            GameSession newSession = new GameSession();
            newSession.player = new Player("Player", 'P', 0, 0, "\u001B[31m");
            newSession.presentPlayerId = newSession.player.id;

            newSession.table = new Field[num * 2][num * 2];

            newSession.bots = new Bot[num - 1];
            for (int i = 0; i < newSession.bots.length; i++){
                switch (new Random().nextInt(3)) {
                    case 0:
                        newSession.bots[i] = new EasyBot(i + 1);
                        break;
                    case 1:
                        newSession.bots[i] = new MediumBot(i + 1);
                        break;
                    case 2:
                        newSession.bots[i] = new Bots.HardBot(i + 1);
                        break;
                }

            }

            newSession.table = Field.NewTable(newSession);

            newSession.addToSave();

            newSession.creationTime = String.valueOf(System.currentTimeMillis());

            mainSession = newSession;
        } else {
            playMenuPage();
        }
    }

    /**
     * Egy kör lejátszása. A paraméterben megadott session "round" adattagjából megállapítja,
     * hogy ebben a körben kinek a köre van és megadja a lehetőséget hogy a kört játszó játékos
     * lépjen. Ez mind addig van lehetősége lépni, míg át nem adja a körét,
     * akkor kiosztásra kerülnek a plussz kockái,
     * egyel nő a "round" adattag és elmentjük a sessiont a mainSession-be.
     *
     * @param session Egy játék session
     *
     * @see GameSession
     *
     */
    public static void round(GameSession session) {
        String[] tableOut = new String[session.table.length * 2 + 3];
        Arrays.fill(tableOut, "");

        String[] info = new String[3];
        Arrays.fill(info, "");

        info[0] = mainSession.kor + ". kor";

        if (session.round % (session.bots.length + 1) == 0) {
            info[1] = "Jelenleg " + session.bots[session.bots.length - 1].name + " van!";
            session.presentPlayerId = session.bots[session.bots.length - 1].id;
        }
        else if (session.round % (session.bots.length + 1) == 1) {
            info[1] = "Jelenleg " + session.player.name + " van!";
            session.presentPlayerId = session.player.id;
        }
        else {
            info[1] = "Jelenleg " + session.bots[(session.round % (session.bots.length + 1) - 2)].name + " van!";
            session.presentPlayerId = session.bots[(session.round % (session.bots.length + 1) - 2)].id;
        }

        mainSession.addToSave(mainSession.round);

        Miscellaneous.tableToStringArray(session.table, tableOut);

        String[] scores = Miscellaneous.checkScores(session);

        session.addToSave(scores);

        int sor = (tableOut.length + 1) + scores.length + info.length;
        Miscellaneous.roundOut(session, info, tableOut, scores, sor, tableOut[2].length() + 21);

        mainSession = session;

        if (session.presentPlayerId == session.player.id) {
            if (session.player.ownedFields > 0) {
                boolean roundEnd;

                do {
                    roundEnd = session.player.Attack(mainSession);

                    if (!roundEnd) {
                        Arrays.fill(tableOut, "");
                        Miscellaneous.tableToStringArray(session.table, tableOut);
                        scores = Miscellaneous.checkScores(session);
                        Miscellaneous.roundOut(mainSession, info, tableOut, scores,
                                sor, tableOut[2].length() + 21);

                        session.addToSave(scores);
                    }
                } while (!roundEnd);

                if (session.player.ownedDices < session.player.ownedFields * 8 && Miscellaneous.existsAvailablePlayer(session) > 1) {
                    Miscellaneous.givePlusDices(mainSession, mainSession.presentPlayerId,
                            Math.min((int) Math.ceil(mainSession.player.ownedFields / 2), (session.player.ownedFields * 8) - session.player.ownedDices));
                } else {
                    Miscellaneous.givePlusDices(mainSession, mainSession.presentPlayerId, 0);
                }
            }
        } else {
            int bot = 0;

            for (int i = 0; i < session.bots.length; i++) {
                if (session.bots[i].id == session.presentPlayerId) {
                    bot = i;
                }
            }

            if (session.bots[bot].ownedFields > 0) {
                boolean roundEnd = false;

                do {
                    roundEnd = session.bots[bot].attack(mainSession);

                    if (!roundEnd) {
                        Arrays.fill(tableOut, "");
                        Miscellaneous.tableToStringArray(session.table, tableOut);
                        scores = Miscellaneous.checkScores(session);
                        Miscellaneous.roundOut(mainSession, info, tableOut, scores,
                                sor, tableOut[2].length() + 21);

                        session.addToSave(scores);
                    }
                } while (!roundEnd);

                if (session.bots[bot].ownedDices < session.bots[bot].ownedFields * 8 && Miscellaneous.existsAvailablePlayer(session) > 1) {
                    Miscellaneous.givePlusDices(mainSession, mainSession.presentPlayerId,
                            Math.min(session.bots[bot].ownedFields / 2, (session.bots[bot].ownedFields * 8) - session.bots[bot].ownedDices));
                } else {
                    Miscellaneous.givePlusDices(mainSession, mainSession.presentPlayerId, 0);
                }
            }
        }

        session.round++;

        mainSession = session;
    }
}
