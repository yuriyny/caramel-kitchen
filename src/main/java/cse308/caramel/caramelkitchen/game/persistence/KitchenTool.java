package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class KitchenTool extends SubprocedureComponent implements Comparable<KitchenTool> {

    @Id
    private String id;
    private String name;
    private String unitOfMeasure;
    @Override
    public int compareTo(KitchenTool o) {
        return this.name.compareTo(o.getName());
    }
}
