package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "kitchenTool")
public class KitchenTool extends SubprocedureComponent{
    @Id
    private String id;
    private String unitOfMeasure;
}
