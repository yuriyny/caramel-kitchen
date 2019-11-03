package cse308.caramel.caramelkitchen.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
@Getter
@Setter
@Document(collection = "equipment")
public class Equipment extends Items{
//    @Id
//    private String id;
//    private String equipmentName;
//    private String unitOfMeasure;
    //embedded document for equipment, no need for referencing
    private Collection<Procedure> procedures = new ArrayList<>();


}
