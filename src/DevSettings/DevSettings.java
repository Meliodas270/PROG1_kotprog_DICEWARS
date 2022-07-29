package DevSettings;

public class DevSettings {
    private static boolean playerMindigAtadjaAKoret = false;
    private static int kiiratasIdotartamok = 3000;

    public static int getKiiratasIdotartamok() {
        return kiiratasIdotartamok;
    }

    public static void setKiiratasIdotartamok(int kiiratasIdotartamok) {
        DevSettings.kiiratasIdotartamok = kiiratasIdotartamok;
    }

    public static boolean isPlayerMindigAtadjaAKoret() {
        return playerMindigAtadjaAKoret;
    }

    public static void setPlayerMindigAtadjaAKoret(boolean playerMindigAtadjaAKoret) {
        DevSettings.playerMindigAtadjaAKoret = playerMindigAtadjaAKoret;
    }
}
