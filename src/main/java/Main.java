import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {

        if (!DatabaseConnection.isConnection()) {
            System.out.println("Chyba při spojení s databází, zkontrolujte internetové připojení.");
            return;
        }

        int choice;
        do {
            System.out.println("1. Nová sázka");
            System.out.println("2. Seznam sázek");
            System.out.println("3. Zápas na který je sázka vypsána");
            System.out.println("4. Detail sázky");
            System.out.println("5. Smazání sázky");
            System.out.println("6. Výpis částek ze sázek u jednotlivých týmů");
            System.out.println("0. Konec programu");
            System.out.print("Vyberte akci: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    newBet();
                    break;
                case 2:
                    listBets();
                    break;
                case 3:
                    listMatches();
                    break;
                case 4:
                    betDetails();
                    break;
                case 5:
                    deleteBet();
                    break;
                case 6:
                    listBetAmounts();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Neplatná volba.");
                    break;
            }
        } while (choice != 0);
    }

    private static void newBet() throws SQLException {
        System.out.print("Zadejte ID zápasu: ");
        int matchId = sc.nextInt();
        System.out.print("Zadejte ID uživatele: ");
        int userId = sc.nextInt();
        System.out.print("Zadejte částku sázky: ");
        float amount = sc.nextFloat();
        System.out.println("1 - Domácí tým vítězí, 2 - Remíza, 3 - Hostující tým vítězí");
        System.out.print("Zadejte tip: ");
        int prediction = sc.nextInt();
        sc.nextLine();

        BetTable betTable = new BetTable();
        betTable.addBet(matchId, userId, amount, prediction);
    }

    private static void listBets() throws SQLException {
        BetTable betTable = new BetTable();
        List<Bet> bets = betTable.getAllBets();

        if (bets.isEmpty()) {
            System.out.println("Nejsou k dispozici žádné sázky.");
        } else {
            System.out.println("Seznam sázek:");
            for (Bet bet : bets) {
                System.out.println("ID sázky: " + bet.getBetId());
                System.out.println("Cas sázky: " + bet.getBetTime());
                System.out.println("Castka: " + bet.getAmount());
                System.out.println("Volba: " + bet.getPrediction());
                System.out.println("ID zápasu: " + bet.getMatchId());
                System.out.println("ID uzivatele ktery sazí: " + bet.getUserId());
                System.out.println("-----------------------\n");
            }
        }
    }


    private static void listMatches() throws SQLException {
        BetTable betTable = new BetTable();
        System.out.println("Zadej ID sázky.");
        int bet_id = sc.nextInt();
        sc.nextLine();
        List<Match> matches = betTable.getMatchesWithBets(bet_id);

        if (matches.isEmpty()) {
            System.out.println("Nejsou k dispozici žádné zápasy se sázkami pro sázku s ID " + bet_id);
        } else {
            System.out.println("Seznam zápasů se sázkami pro sázku s ID " + bet_id + ":");
            for (Match match : matches) {
                System.out.println("ID zápasu: " + match.getMatchId());
                System.out.println("Skore: " + match.getScore());
                System.out.println("Datum zápasu: " + match.getMatchDateTime());
                System.out.println("Stav zápasu: " + match.getStatus());
                System.out.println("Místo konání zápasu: " + match.getPlace());
                System.out.println("ID týmu 1: " + match.getTeam1Id());
                System.out.println("ID týmu 2: " + match.getTeam2Id());
                System.out.println("-----------------------");
            }
        }
    }



    private static void betDetails() throws SQLException {
        System.out.print("Zadejte ID sázky: ");
        int betId = sc.nextInt();
        sc.nextLine();

        BetTable betTable = new BetTable();
        Bet bet = betTable.getBetById(betId);

        if (bet == null) {
            System.out.println("Sázka s ID " + betId + " neexistuje.");
        } else {
            System.out.println("ID sázky: " + bet.getBetId());
            System.out.println("ID zápasu: " + bet.getMatchId());
            System.out.println("ID uživatele: " + bet.getUserId());
            System.out.println("Částka sázky: " + bet.getAmount());
            System.out.println("Tip: " + bet.getPrediction());
        }
    }


    private static void deleteBet() throws SQLException {
        System.out.print("Zadejte ID sázky: ");
        int betId = sc.nextInt();
        sc.nextLine();

        BetTable betTable = new BetTable();
        boolean result = betTable.deleteBet(betId);
        if (result) {
            System.out.println("Sázka s ID " + betId + " byla smazána.");
        } else {
            System.out.println("Sázka s ID " + betId + " nebyla nalezena.");
        }
    }





    private static void listBetAmounts() throws SQLException {
        System.out.print("Zadejte ID týmu: ");
        int matchId = sc.nextInt();
        sc.nextLine();

        BetTable betTable = new BetTable();
        float matchAmount = betTable.listBetAmounts(matchId);

        if (matchAmount == 0) {
            System.out.println("Tyř s ID " + matchId + " neexistuje.");
        } else {
            System.out.println("Celková počet sázek na zápas: " + matchAmount);
        }
    }


}