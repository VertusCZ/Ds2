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
                System.out.println(bet.toString());
            }
        }
    }


    private static void listMatches() {
       // MatchTable matchTable = new MatchTable();
       // matchTable.listMatches();
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


    private static void deleteBet() {
            System.out.print("Zadejte ID sázky: ");
            int betId = sc.nextInt();
            sc.nextLine();

            Bet bet = new Bet();
            boolean result = BetTable.deleteBet(betId);
            if (result) {
                System.out.println("Sázka s ID " + betId + " byla smazána.");
            } else {
                System.out.println("Sázka s ID " + betId + " nebyla nalezena.");
            }
        }



    private static void listBetAmounts() throws SQLException {
        if (!DatabaseConnection.isConnection()) {
            System.out.println("Chyba při spojení s databází, zkontrolujte internetové připojení.");
            return;
        }

        System.out.print("Zadejte ID zápasu: ");
        int matchId = sc.nextInt();
        sc.nextLine();
        BetTable betTable = new BetTable();
        betTable.listBetAmounts(matchId);
    }


}