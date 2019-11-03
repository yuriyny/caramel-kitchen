package cse308.caramel.caramelkitchen.persistence;

import cse308.caramel.caramelkitchen.game.Equipment;
import cse308.caramel.caramelkitchen.game.Ingredient;
import cse308.caramel.caramelkitchen.game.Procedure;
import cse308.caramel.caramelkitchen.game.Recipe;
import cse308.caramel.caramelkitchen.game.repository.EquipmentRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.user.Request;
import cse308.caramel.caramelkitchen.user.Role;
import cse308.caramel.caramelkitchen.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
public class DbSeeder implements CommandLineRunner {
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    //we can use mongotemplate class or repository interface for managing data in mongodb
    private MongoTemplate mongoTemplate;
    public DbSeeder(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void run(String... args) {
        this.mongoTemplate.dropCollection(User.class);
        this.mongoTemplate.dropCollection(Request.class);
        //this.mongoTemplate.dropCollection(Role.class);
        this.mongoTemplate.dropCollection(Ingredient.class);
        this.mongoTemplate.dropCollection(Equipment.class);
        this.mongoTemplate.dropCollection(Recipe.class);

        Ingredient i1 = new Ingredient();
        i1.setName("salt");
        i1.setUnitOfMeasure("teaspoon");
        i1.setQuantity(1);

        Ingredient i2 = new Ingredient();
        i2.setName("pepper");
        i2.setUnitOfMeasure("teaspoon");
        i2.setQuantity(1);


        Ingredient i3 = new Ingredient();
        i3.setName("olive oil");
        i3.setUnitOfMeasure("tablespoon");
        i3.setQuantity(3);

        this.mongoTemplate.insert(i1);
        this.mongoTemplate.insert(i2);
        this.mongoTemplate.insert(i3);

        Equipment e1 = new Equipment();
        e1.setName("knife");
        Procedure p1 = new Procedure();
        p1.setProcedureName("chopping");
        e1.getProcedures().add(p1);

        Equipment e2 = new Equipment();
        e2.setName("oven");
        Procedure p2 = new Procedure();
        p2.setProcedureName("baking");
        e2.getProcedures().add(p2);

        Equipment e3 = new Equipment();
        e3.setName("bowl");
        Procedure p3 = new Procedure();
        p3.setProcedureName("mixing");
        e3.getProcedures().add(p3);

        mongoTemplate.insert(e1);
        mongoTemplate.insert(e2);
        mongoTemplate.insert(e3);
        //query test
        /*Collection<Equipment> equipment = new ArrayList<>();
        equipment = equipmentRepository.findAllEquipmentContainingString("oW");
        for( Equipment ii1 : equipment){
            System.out.println("Ingredient -- " +ii1.getEquipmentName());
        }*/





    }
}