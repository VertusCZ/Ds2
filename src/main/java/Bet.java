import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
public class Bet {

    private int betId;

    private Timestamp betTime;

    private float amount;

    private int prediction;

    private int matchId;

    private int userId;

    public Bet(int betId, Timestamp betTime, float amount, int prediction, int matchId, int userId) {
        this.betId = betId;
        this.betTime = betTime;
        this.amount = amount;
        this.prediction = prediction;
        this.matchId = matchId;
        this.userId = userId;
    }


    public void printBet() {
        System.out.println(betId + ", " + betTime + ", " + amount + ", " + prediction + ", " + matchId + ", " + userId);
    }

}
