import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Role {

    private int roleId;

    private String role;

    private String description;
    public void printRole() {
        System.out.println(roleId + ", " + role + ", " + description);
    }

}
