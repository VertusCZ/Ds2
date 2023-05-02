import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Match {

    private int matchId;

    private String score;

    private LocalDateTime matchDateTime;

    private String status;

    private String place;

    private int team1Id;

    private int team2Id;

    public void printMatch() {
        System.out.println(matchId + ", " + score + ", " + matchDateTime + ", " + status + ", " + place + ", " + team1Id + ", " + team2Id);
    }

}
