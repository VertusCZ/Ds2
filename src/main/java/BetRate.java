import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class BetRate {

    private int betRateId;

    private float rateWinTeam1;

    private float rateWinTeam2;

    private float rateDraw;

    private LocalDateTime setTime;

    private int matchId;

    public void printBetRate() {
        System.out.println(betRateId + ", " + rateWinTeam1 + ", " + rateWinTeam2 + ", " + rateDraw + ", " + setTime + ", " + matchId);
    }

}
