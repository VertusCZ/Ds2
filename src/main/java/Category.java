import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Category {

    private int categoryId;

    private String name;

    private String description;

    public void printCategory() {
        System.out.println(categoryId + ", " + name + ", " + description);
    }
}