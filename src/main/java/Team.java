import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Team {

    private int teamId;

    private String name;

    private String league;

    private String country;

    private int ranking;

    private int categoryId;

    public void printTeam() {
        System.out.println(teamId + ", " + name + ", " + league + ", " + country + ", " + ranking + ", " + categoryId);
    }

}
