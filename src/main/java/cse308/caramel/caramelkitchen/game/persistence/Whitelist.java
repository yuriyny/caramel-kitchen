package cse308.caramel.caramelkitchen.game.persistence;

import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Document(collection = "whitelist")
public class Whitelist {
    @Id
//    @DBRef
    private String name;//ingredient name
    private Collection<String> actions;
    public Whitelist(){
        actions=new ArrayList();
    }
}


