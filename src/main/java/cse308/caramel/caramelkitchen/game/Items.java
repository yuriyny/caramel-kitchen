package cse308.caramel.caramelkitchen.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "items")
public class Items implements Comparable<Items> {
    @Id
    private String id;
    private String name;
    private String unitOfMeasure;
    @Override
    public int compareTo(Items o) {
        return this.name.compareTo(o.getName());
    }
}
