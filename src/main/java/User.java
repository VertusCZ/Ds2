import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class User {

    private int userId;

    private String firstName;

    private String lastName;

    private String login;

    private String password;

    private String email;

    private String address;

    private String bankAccountNumber;

    private float wallet;

    private int roleId;

    public void printUser() {
        System.out.println(userId + ", " + firstName + ", " + lastName + ", " + login + ", " + password + ", " + email + ", " + address + ", " + bankAccountNumber + ", " + wallet + ", " + roleId);
    }

}
