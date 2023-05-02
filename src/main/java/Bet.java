import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Bet {

    private int betId;

    private LocalDateTime betTime;

    private float amount;

    private int prediction;

    private int matchId;

    private int userId;

    public void printBet() {
        System.out.println(betId + ", " + betTime + ", " + amount + ", " + prediction + ", " + matchId + ", " + userId);
    }

}
