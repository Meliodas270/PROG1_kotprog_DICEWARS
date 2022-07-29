package Game;

import Bots.Bot;
import Player.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Ebben a classban tudjuk eltárolni egy játék session-jét és annak minden adatát.
 */
public class GameSession{
    public Player player;
    public Bot[] bots;
    public Field[][] table;
    public int round = 1;
    public int kor;
    public char presentPlayerId = 'P';
    public String creationTime;
    public List<String> saveList = new ArrayList<>();

    /**
     * Az egész session elmentése egy text file-ba.
     */
    public void save() {
        String relativePath = "Saves" + File.separator + this.creationTime + ".txt";
        String saves = "Saves" + File.separator + "saves.txt";

        try {
            Files.createDirectories(Paths.get(relativePath).getParent());
            File file = new File(relativePath);
            FileWriter fw = new FileWriter(file, true);

            for (String s : this.saveList) {
                fw.write(s);
                fw.write("\n");
            }

            fw.close();

            file = new File(saves);
            fw = new FileWriter(file, true);

            fw.write(this.creationTime + ".txt");
            fw.write("\n");

            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * A játék jelenlegi tábláját menti el a session "saveList" nevű adattagába.
     */
    public void addToSave() {
        StringBuilder table = new StringBuilder();
        for (Field[] fields : this.table) {
            for (Field field : fields) {
                table.append(field.toString()).append(";");
            }
        }

        table.deleteCharAt(table.length() - 1);

        this.saveList.add("T::" + table.toString());
    }

    /**
     * A játék jelenlegi körét menti el a session "saveList" nevű adattagába.
     *
     * @param round Játék jelenlegi köre
     */
    public void addToSave(int round) {
        if (this.presentPlayerId == this.player.id) {
            this.saveList.add("R::" + round + "," + this.player.name);
        } else {
            for (Bot bot : this.bots) {
                if (this.presentPlayerId == bot.id) {
                    this.saveList.add("R::" + round + "," + bot.name);
                }
            }
        }
    }

    /**
     * A játék egyik köre alatt törént támadását menti el a session "saveList" nevű adattagába.
     *
     * @param tamadosor A támadó mező sora
     * @param tamadooszlop A támadó mező oszlopa
     * @param vedekezosor A megtámadott mező sora
     * @param vedekezooszlop A megtámadott mezó oszlopa
     * @param tamadodobasok A támadó mezőjén lévő kockákkal dobott értékek összege
     * @param vedekezodobasok A megtámadott mezőjén lévő kockákkal dobott értékek összege
     * @param winner A támadás győztese
     */
    public void addToSave(int tamadosor, int tamadooszlop,
                          int vedekezosor, int vedekezooszlop,
                          String tamadodobasok, String vedekezodobasok,
                          String winner)
    {
        String tamadomezo = (char) (tamadooszlop + 65) + "" + (tamadosor + 1);
        String vedekezomezo = (char) (vedekezooszlop + 65) + "" + (vedekezosor + 1);

        this.saveList.add("A::" + tamadomezo + ">" + vedekezomezo + ";" + tamadodobasok + " || " + vedekezodobasok + ";" + winner);
    }

    /**
     * A jelenlegi állást menti el a session "saveList" nevű adattagába.
     *
     * @param scores A jelenlegi állást tartalmazó tömb
     */
    public void addToSave(String[] scores) {
        StringBuilder scoresinline = new StringBuilder();
        for (String score : scores) {
            scoresinline.append(score).append(";");
        }
        scoresinline.deleteCharAt(scoresinline.length() - 1);
        this.saveList.add("S::" + scoresinline.toString());
    }
}