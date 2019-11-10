package cse308.caramel.caramelkitchen.util;

import cse308.caramel.caramelkitchen.game.controller.RecipeController;
import cse308.caramel.caramelkitchen.game.model.GameApplication;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;
import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.Subprocedure;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.user.persistence.Request;
import cse308.caramel.caramelkitchen.user.persistence.User;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    KitchenToolRepository kitchenToolRepository;
    @Autowired
    RecipeController recipeController;
    @Autowired
    UserDomainService userDomainService;
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

        User user=new User();
        user.setUsername("user");
        user.setEnabled(true);
        user.setPassword("password");
        userDomainService.saveUser(user);

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

        Ingredient i4 = new Ingredient();
        i4.setName("apple");
        i4.setImageName("apple.png");
        i4.setQuantity(1);

        this.mongoTemplate.insert(i1);
        this.mongoTemplate.insert(i2);
        this.mongoTemplate.insert(i3);
        this.mongoTemplate.insert(i4);

        KitchenTool e1 = new KitchenTool();
        e1.setName("knife");
        e1.getActions().add("Chopping");
        e1.getActions().add("Peeling");
        Subprocedure p1 = new Subprocedure();
        p1.setProcedureName("chopping");

        KitchenTool e2 = new KitchenTool();
        e2.setName("oven");
        Subprocedure p2 = new Subprocedure();
        p2.setProcedureName("baking");

        KitchenTool e3 = new KitchenTool();
        e3.setName("bowl");
        e3.getActions().add("Mixing");
        Subprocedure p3 = new Subprocedure();
        p3.setProcedureName("mixing");

        mongoTemplate.insert(e1);
        mongoTemplate.insert(e2);
        mongoTemplate.insert(e3);

        //get all actions from specific tool
        Collection<String> toolActions = kitchenToolRepository.findById(e1.getId()).get().getActions();
        //print the result
        for ( String s : toolActions){
            System.out.println(s);
        }


        Subprocedure chopApple=new Subprocedure();
        chopApple.setProcedureName("Chop Apple");
        chopApple.setInstructions("Chop the apple");
        chopApple.setGame(new GameApplication());

        Recipe recipe=new Recipe();
        recipe.setCreator(user.getUsername());
        recipe.setRecipeName("Chopping Apple Recipe");
        recipe.setSubprocedureList(new ArrayList<>());
        recipe.getSubprocedureList().add(chopApple);
        recipe.setIsInProgress(true);
        recipe.setIngredients(new ArrayList<>());
        recipe.setKitchenTools(new ArrayList<>());
        recipe.getIngredients().add(i4);
        recipe.getKitchenTools().add(e1);
        recipeController.createRecipe(recipe);              //Test create recipe
//        mongoTemplate.insert(recipe);

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