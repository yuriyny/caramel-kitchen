package cse308.caramel.caramelkitchen.util;

import cse308.caramel.caramelkitchen.s3client.services.S3Services;
import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.Subprocedure;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.user.persistence.Request;
import cse308.caramel.caramelkitchen.user.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


@Service
public class DbSeeder implements CommandLineRunner {
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    KitchenToolRepository kitchenToolRepository;
    //we can use mongotemplate class or repository interface for managing data in mongodb
    private MongoTemplate mongoTemplate;
    public DbSeeder(MongoTemplate mongoTemplate){
        this.mongoTemplate = mongoTemplate;
    }

    @Autowired
    S3Services s3Services;
    //private String uploadFilePath = "C:\\planet.png";
    //assign path to the file you wish to upload to s3 bucked and use it in s3 service upload method
    private String uploadFilePath;


    @Override
    public void run(String... args) {
        this.mongoTemplate.dropCollection(User.class);
        this.mongoTemplate.dropCollection(Request.class);
        //this.mongoTemplate.dropCollection(Role.class);
        this.mongoTemplate.dropCollection(Ingredient.class);
        this.mongoTemplate.dropCollection(KitchenTool.class);
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

        KitchenTool e1 = new KitchenTool();
        e1.setName("knife");
        Subprocedure p1 = new Subprocedure();
        p1.setProcedureName("chopping");

        KitchenTool e2 = new KitchenTool();
        e2.setName("oven");
        Subprocedure p2 = new Subprocedure();
        p2.setProcedureName("baking");

        KitchenTool e3 = new KitchenTool();
        e3.setName("bowl");
        Subprocedure p3 = new Subprocedure();
        p3.setProcedureName("mixing");

        mongoTemplate.insert(e1);
        mongoTemplate.insert(e2);
        mongoTemplate.insert(e3);
        //query test
        /*Collection<Equipment> equipment = new ArrayList<>();
        equipment = equipmentRepository.findAllEquipmentContainingString("oW");
        for( Equipment ii1 : equipment){
            System.out.println("Ingredient -- " +ii1.getEquipmentName());
        }*/

        //S3 Client Operations
        //this command upload file: C:\\s3\\filrtoupload.png
        //System.out.println("---------------- START UPLOAD FILE ----------------");
        //s3Services.uploadFile("planet.png", uploadFilePath);
        /*
        System.out.println("---------------- START DOWNLOAD FILE ----------------");
        //this command download the previously uploaded file
        s3Services.downloadFile(downloadKey);*/

        System.out.println("Pre-Signed URL: " + s3Services.getImageUrl("apple.png"));




    }
}