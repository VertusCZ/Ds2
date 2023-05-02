import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BetTable implements AutoCloseable {
    private static Connection conn;
    private PreparedStatement stmt;
    private ResultSet rs;

    public BetTable() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public static void getBetID() {
    }


    public void addBet(int matchId, int userId, float amount, int prediction) throws SQLException {
        if (matchId <= 0 || userId <= 0 || amount <= 0 || prediction < 0) {
            throw new IllegalArgumentException("All fields are required.");
        }

        try (CallableStatement cstmt = conn.prepareCall("{CALL add_bet(?,?,?,?,?)}")) {
            cstmt.setInt(1, matchId);
            cstmt.setInt(2, userId);
            cstmt.setFloat(3, amount);
            cstmt.setInt(4, prediction);
            cstmt.execute();
            System.out.println("Bet successfully added");
        } catch (SQLException e) {
            System.out.println("Error adding bet: " + e.getMessage());
            throw e;
        }
    }

    public List<Bet> getAllBets() throws SQLException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Bet")) {
            rs = stmt.executeQuery();
            while (rs.next()) {
                Bet bet = new Bet(
                );
                bets.add(bet);
            }
        }
        return bets;
    }

    public List<Bet> getBetsByMatchId(int matchId) throws SQLException {
        List<Bet> bets = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Bet WHERE match_id = ?")) {
            stmt.setInt(1, matchId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Bet bet = new Bet(
                );
                bets.add(bet);
            }
        }
        return bets;
    }

    public int getBetID(int matchId, int userId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("SELECT bet_id FROM Bet WHERE match_id = ? AND user_id = ?")) {
            stmt.setInt(1, matchId);
            stmt.setInt(2, userId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("bet_id");
            } else {
                throw new SQLException("Bet not found.");
            }
        }
    }

    public Bet getBetById(int betId) {
        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Bet WHERE bet_id = ?")) {
            pstmt.setInt(1, betId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Bet(
                );
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

    public static boolean deleteBet(int betId) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Bet WHERE bet_id = ?");
            pstmt.setInt(1, betId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 1) {
                System.out.println("Bet successfully deleted");
            } else {
                throw new SQLException("Failed to delete bet");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting bet: " + e.getMessage());
        }
        return false;
    }


    public void listBetAmounts(int matchId) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT amount FROM Bet WHERE match_id = ?");
            pstmt.setInt(1, matchId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                float amount = rs.getFloat("amount");
                System.out.println(amount);
            }
        } catch (SQLException e) {
            System.out.println("Error listing bet amounts: " + e.getMessage());
        }
    }

    @Override
    public void close() throws Exception {

    }
}