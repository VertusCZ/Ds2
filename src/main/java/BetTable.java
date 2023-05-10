import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BetTable implements AutoCloseable {
    private static Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;


    public BetTable() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }


    /* SQL COMMANDS */

    //2.1 Nova sazka
    private final String SQL_INSERT_BET = "INSERT INTO Bet (bet_time, amount, prediction, match_id, user_id)" +
            "VALUES (?, ?, ?, ?, ?)";

    //2.2 Seznam sazek
    private final String SQL_SELECT_BETS = "SELECT Bet.bet_id as a, Bet.match_id as b , Match.team1_id as c, Match.team2_id as d, Match.match_date_time as e, Bet.amount as f, Bet.bet_time as g, Bet.prediction as h,Bet.user_id as i " +
            "FROM Bet " +
            "JOIN Match ON Bet.match_id = Match.match_id ";


    //2.3 Zapas, na ktery je sazka vypsana
    private final String SQL_SELECT_BET_MATCH =" SELECT m.match_id as a, m.score as b, m.match_date_time as c, m.status as d , m.place as f, m.team1_id as g, m.team2_id as h "+
   " FROM match m "+
    "JOIN bet b ON m.match_id = b.match_id "+
    "WHERE b.bet_id = ? ";


    //2.4 Detail sazky
    private final String SQL_SELECT_BET =  "SELECT Bet.bet_id as a, Bet.match_id as b , Match.team1_id as c, Match.team2_id as d, Match.match_date_time as e, Bet.amount as f, Bet.bet_time as g, Bet.prediction as h,Bet.user_id as i " +
            "FROM Bet " +
            "JOIN Match ON Bet.match_id = Match.match_id " +
            "WHERE Bet.bet_id = ?";

    //2.5 Smazani sazky
    private static final String SQL_DELETE_BET = "DELETE FROM Bet WHERE bet_id = ?";

    //2.6 Vypis castek ze sazek u jednotlivych tymu
    private final String SQL_SELECT_TEAM_AMOUNTS = "SELECT Team.name as a, Team.team_id as b, SUM(COALESCE(Bet.amount,0)) AS total_amount " +
            "FROM Match " +
            "JOIN Team ON (Team.team_id=Match.team1_id OR Team.team_id=Match.team2_id)" +
            "JOIN Bet ON Match.match_id = Bet.match_id " +
            "WHERE Team.team_id = ?  AND  ((Match.team1_id!=Match.team2_id AND (Bet.prediction=1)) OR (Match.team1_id!=Match.team2_id AND (Bet.prediction=2))) " +
            "GROUP BY Team.name, Team.team_id " +
            "ORDER BY total_amount DESC";



    public void addBet(int matchId, int userId, float amount, int prediction) throws SQLException {
        if (matchId <= 0 || userId <= 0 || amount <= 0 || prediction < 0) {
            throw new IllegalArgumentException("All fields are required.");
        }

        try (CallableStatement cstmt = conn.prepareCall(SQL_INSERT_BET)) {
            cstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // set current timestamp as bet_time
            cstmt.setFloat(2, amount);
            cstmt.setInt(3, prediction);
            cstmt.setInt(4, matchId);
            cstmt.setInt(5, userId);
            cstmt.execute();
            System.out.println("Bet successfully added");
        } catch (SQLException e) {
            System.out.println("Error adding bet: " + e.getMessage());
            throw e;
        }
    }

    public List<Bet> getAllBets() throws SQLException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BETS)) {
            rs = stmt.executeQuery();
            while (rs.next()) {
                int betId = rs.getInt("a");
                Timestamp betTime = rs.getTimestamp("g");
                float amount = rs.getFloat("f");
                int prediction = rs.getInt("h");
                int matchId = rs.getInt("b");
                int userId = rs.getInt("i");
                Bet bet = new Bet(betId, betTime, amount, prediction, matchId, userId);
                bets.add(bet);
            }
        } catch (SQLException e) {
            System.out.println("Error getting bets: " + e.getMessage());
            throw e;
        }
        // print the list of bets
        for (Bet bet : bets) {
            bet.printBet();
        }
        return bets;
    }



    public List<Match> getMatchesWithBets(int bet_id) throws SQLException {
        try (PreparedStatement statement = conn.prepareStatement(SQL_SELECT_BET_MATCH)) {
            statement.setInt(1, bet_id);
            ResultSet resultSet = statement.executeQuery();
            List<Match> matches = new ArrayList<>();

            while (resultSet.next()) {
                Match match = new Match();
                match.setMatchId(resultSet.getInt("a"));
                match.setScore(resultSet.getString("b"));
                match.setMatchDateTime(resultSet.getDate("c"));
                match.setStatus(resultSet.getString("d"));
                match.setPlace(resultSet.getString("f"));
                match.setTeam1Id(resultSet.getInt("g"));
                match.setTeam2Id(resultSet.getInt("h"));
                matches.add(match);
            }

            return matches;
        }
    }




    public Bet getBetById(int betId) {
        try (PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BET)) {
            pstmt.setInt(1, betId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int betId_2 = rs.getInt("a");
                Timestamp betTime = rs.getTimestamp("g");
                float amount = rs.getFloat("f");
                int prediction = rs.getInt("h");
                int matchId = rs.getInt("b");
                int userId = rs.getInt("i");
                return new Bet(betId_2, betTime, amount, prediction, matchId, userId);
            } else {
                throw new SQLException("Bet not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error getting bet by ID: " + e.getMessage());
            return null;
        }
    }

    public void updateBet(Bet bet) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE Bet SET match_id = ?, user_id = ?, amount = ?, prediction = ? WHERE bet_id = ?");
            pstmt.setInt(1, bet.getMatchId());
            pstmt.setInt(2, bet.getUserId());
            pstmt.setFloat(3, bet.getAmount());
            pstmt.setInt(4, bet.getPrediction());
            pstmt.setInt(5, bet.getBetId());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Bet successfully updated");
            } else {
                throw new SQLException("Failed to update bet");
            }
        } catch (SQLException e) {
            System.out.println("Error updating bet: " + e.getMessage());
        }
    }

    public boolean deleteBet(int betId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_BET)) {
            stmt.setInt(1, betId);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Bet with ID " + betId + " deleted successfully");
                return true;
            } else {
                System.out.println("No bet found with ID " + betId);
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error deleting bet: " + e.getMessage());
            throw e;
        }
    }



    public float listBetAmounts(int match_id) {
        // retrieve and print the total amounts for bets placed on each team
        try (PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_TEAM_AMOUNTS)) {
            stmt.setInt(1, match_id);
            rs = stmt.executeQuery();
            System.out.println("Pruměrna částka sázek tymu:");
            System.out.println("Team name\tTotal amount");
            while (rs.next()) {
                String teamName = rs.getString("a");
                float totalAmount = rs.getFloat("total_amount");
                System.out.println(teamName + "\t\t" + totalAmount);
            }
        } catch (SQLException e) {
            System.out.println("Error getting team amounts: " + e.getMessage());
            try {
                throw e;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        return match_id;
    }

    @Override
    public void close() throws Exception {
        
    }
}