package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Document(collection = "kitchenTool")
public class KitchenTool extends SubprocedureComponent{
    @Id
    private String id;
    private String unitOfMeasure;
    private Collection<String> actions;

    public KitchenTool(){
        actions=new ArrayList<>();
    }
}
