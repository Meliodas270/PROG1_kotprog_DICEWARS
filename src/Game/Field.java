package Game;

import java.util.Random;

/**
 * Ez a class a tábla egy mezőjének adattagjait és metódusait tartalmazza.
 */
public class Field {
    public char ownerID;
    public int numberOfDices;

    @Override
    public String toString() {
        return ownerID + "" + numberOfDices;
    }

    public Field(char ownerID, int numberOfDices) {
        this.ownerID = ownerID;
        this.numberOfDices = numberOfDices;
    }

    /**
     * Ez a metódus készít egy Field típusú 2D tömböt, egy játéktáblát, amin random helyekre
     * egyenlő számú mezőt oszt minden játékosnak.
     * Minden játékos háromszor annyi kockát kap mint ahány területe van,
     * ezek a kockák a területek között véletlenszerűen vannak kioszva, úgy,
     * hogy egy mezőn van legalább 1, maximum pedig 8 dobókocka.
     *
     * @param session Egy játék session, aminek egy új, random generált pályát szeretnénk adni.
     * @return Egy random értékekkel telegenerált pálya, amin a játékosok tudnak játszani.
     */
    public static Field[][] NewTable(GameSession session){
        Random rnd = new Random();
        int done = 0;

        //minden mező létrehozása 0ás tagokkal
        for (int i = 0; i < session.table.length; i++) {
            for (int j = 0; j < session.table[i].length; j++) {
                session.table[i][j] = new Field('0', 0);
            }
        }

        //mezők kiosztása a játékosok között
        do {
            for (int i = 0; i < session.table.length; i++) {
                for (int j = 0; j < session.table[i].length; j++) {
                    if (session.table[i][j].ownerID == '0') {
                        if (rnd.nextInt(4) == 0) {
                            if (session.player.ownedFields != (session.table.length * session.table.length) / (1 + session.bots.length)) {
                                session.table[i][j].ownerID = session.player.id;
                                session.table[i][j].numberOfDices = 1;
                                session.player.ownedDices++;
                                session.player.ownedFields++;
                            }
                        } else {
                            int randombot = rnd.nextInt(session.bots.length);
                            if (session.bots[randombot].ownedFields != (session.table.length * session.table.length) / (1 + session.bots.length)) {
                                session.table[i][j].ownerID = session.bots[randombot].id;
                                session.table[i][j].numberOfDices = 1;
                                session.bots[randombot].ownedDices++;
                                session.bots[randombot].ownedFields++;
                            }
                        }
                    }
                }
            }

            done = session.player.ownedFields;

            for (int i = 0; i < session.bots.length; i++) {
                done += session.bots[i].ownedDices;
            }
        } while (done != session.table.length * session.table.length);

        done = 0;

        //dobókockák kiosztása a játékosok között
        do {
            for (int i = 0; i < session.table.length; i++) {
                for (int j = 0; j < session.table[i].length; j++) {
                    if (session.table[i][j].numberOfDices < 8) {
                        int add = rnd.nextInt(9 - session.table[i][j].numberOfDices);

                        if (session.table[i][j].ownerID == session.player.id) {
                            if (session.player.ownedDices + add <= session.player.ownedFields * 3) {
                                session.table[i][j].numberOfDices += add;
                                session.player.ownedDices += add;
                            }
                        } else {
                            for (int k = 0; k < session.bots.length; k++) {
                                if (session.bots[k].id == session.table[i][j].ownerID) {
                                    if (session.bots[k].ownedDices + add <= session.bots[k].ownedFields * 3) {
                                        session.table[i][j].numberOfDices += add;
                                        session.bots[k].ownedDices += add;
                                    }
                                }
                            }
                        }
                    }
                }
            }

            done = session.player.ownedDices;

            for (int i = 0; i < session.bots.length; i++) {
                done += session.bots[i].ownedDices;
            }
        } while (done != (session.player.ownedFields * 3) * (1 + session.bots.length));

//        for (int i = 0; i < session.table.length; i++) {
//            for (int j = 0; j < session.table[i].length; j++) {
//                if (session.table[i][j].ownerID == session.player.id) {
//                    session.table[i][j].numberOfDices = 1;
//                }
//            }
//        }

        return session.table;
    }
}
