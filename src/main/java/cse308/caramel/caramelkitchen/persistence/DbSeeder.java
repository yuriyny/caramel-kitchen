/*
package cse308.caramel.caramelkitchen.persistence;

import cse308.caramel.caramelkitchen.game.Equipment;
import cse308.caramel.caramelkitchen.game.Ingredient;
import cse308.caramel.caramelkitchen.game.Recipe;
import cse308.caramel.caramelkitchen.user.Request;
import cse308.caramel.caramelkitchen.user.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

//this class will be used to drop all collection and populate database with initial collections(data)
@Service
public class DbSeeder implements CommandLineRunner {

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

        User u1 = new User();
        u1.setUsername("user1");
        u1.setPassword("user1");
        this.mongoTemplate.insert(u1);

    }
}*/
