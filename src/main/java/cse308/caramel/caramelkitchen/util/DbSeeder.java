package cse308.caramel.caramelkitchen.util;

import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.user.persistence.Request;
import cse308.caramel.caramelkitchen.user.persistence.User;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
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
    KitchenToolRepository kitchenToolRepository;
    @Autowired
    RecipeService recipeService;
    @Autowired
    WhitelistRepository whitelistRepository;
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
        /* ----------------- DROP ALL TABLES ----------------------*/
        this.mongoTemplate.dropCollection(User.class);
        this.mongoTemplate.dropCollection(Request.class);
        //this.mongoTemplate.dropCollection(Role.class);
        this.mongoTemplate.dropCollection(Ingredient.class);
        this.mongoTemplate.dropCollection(KitchenTool.class);
        this.mongoTemplate.dropCollection(Recipe.class);
        this.mongoTemplate.dropCollection(Whitelist.class);
        /* ----------------- ADD USER ----------------------*/
        User user=new User();
        user.setUsername("user");
        user.setEnabled(true);
        user.setPassword("password");
        userDomainService.saveNewUser(user);

        /* ----------------- ADD TOOLS ----------------------*/ //current list of actions [chop,peel,slice,boil,flatten]
        KitchenTool knife = new KitchenTool();
        knife.setName("knife");
        knife.getActions().add("chop");
        knife.getActions().add("peel");
        knife.getActions().add("slice");
        knife.getActions().add("spread");
        knife.setImageName("knife.png");

        KitchenTool kettle = new KitchenTool();
        kettle.setName("kettle");
        kettle.getActions().add("boil");
        kettle.setImageName("kettle.png");

        KitchenTool rollingPin = new KitchenTool();
        rollingPin.setName("rolling pin");
        rollingPin.getActions().add("flatten");
        rollingPin.setImageName("rollingpin.png");

        KitchenTool teaspoon = new KitchenTool();
        teaspoon.setName("teaspoon");
        teaspoon.getActions().add("measure");
        teaspoon.setUnitOfMeasure("teaspoon");

        KitchenTool tablespoon = new KitchenTool();
        tablespoon.setName("tablespoon");
        tablespoon.getActions().add("measure");
        tablespoon.setUnitOfMeasure("tablespoon");

        KitchenTool measuringCup = new KitchenTool();
        measuringCup.setName("measuring cup");
        measuringCup.getActions().add("measure");
        measuringCup.setUnitOfMeasure("cup");

        KitchenTool mixingBowl = new KitchenTool();
        mixingBowl.setName("bowl");
        mixingBowl.getActions().add("mix");
        mixingBowl.getActions().add("marinate");

        KitchenTool mixingSpoon = new KitchenTool();
        mixingSpoon.setName("mixing spoon");
        mixingBowl.getActions().add("mix");

        KitchenTool pan = new KitchenTool();
        pan.setName("pan");
        pan.getActions().add("sauté");
        pan.setImageName("pan.png");

        KitchenTool pot = new KitchenTool();
        pot.setName("pot");
        pot.getActions().add("boil");
        pot.setImageName("pot.png");

        KitchenTool spatula = new KitchenTool();
        spatula.setName("spatula");
        spatula.getActions().add("flip");
        spatula.getActions().add("stir");

        this.mongoTemplate.insert(knife);
        this.mongoTemplate.insert(kettle);
        this.mongoTemplate.insert(rollingPin);
        this.mongoTemplate.insert(teaspoon);
        this.mongoTemplate.insert(tablespoon);
        this.mongoTemplate.insert(measuringCup);
        this.mongoTemplate.insert(mixingBowl);
        this.mongoTemplate.insert(mixingSpoon);
        this.mongoTemplate.insert(pan);
        this.mongoTemplate.insert(pot);
        this.mongoTemplate.insert(spatula);

        /* ----------------- ADD INGREDIENTS ----------------------*/
        Ingredient apple = new Ingredient();
        apple.setName("apple");
        apple.setImageName("apple.png");
        apple.setType("fruit");

        Ingredient carrot = new Ingredient();
        carrot.setName("carrot");
        carrot.setImageName("carrot.png");
        carrot.setType("vegetable");

        Ingredient garlic = new Ingredient();
        garlic.setName("garlic");
        garlic.setImageName("garlic.png");
        garlic.setType("vegetable");

        Ingredient salt = new Ingredient();
        salt.setName("salt");
        salt.getUnitOfMeasure().add("teaspoon");
        salt.getUnitOfMeasure().add("tablespoon");
        salt.setImageName("salt.png");
        salt.setType("spice");

        Ingredient pepper = new Ingredient();
        pepper.setName("pepper");
        pepper.getUnitOfMeasure().add("teaspoon");
        pepper.getUnitOfMeasure().add("tablespoon");
        pepper.setImageName("pepper.png");
        pepper.setType("spice");

        Ingredient steak = new Ingredient();
        steak.setName("steak");
        steak.setImageName("steak.png");
        steak.setType("meat");

        Ingredient orange = new Ingredient();
        orange.setName("orange");
        orange.setImageName("orange.png");
        orange.setType("fruit");

        Ingredient broccoli = new Ingredient();
        broccoli.setName("broccoli");
        broccoli.setImageName("broccoli.png");
        broccoli.setType("vegetable");

        Ingredient chicken = new Ingredient();
        chicken.setName("chicken");
        chicken.setImageName("chicken.png");
        chicken.setType("meat");

        Ingredient dryMustard = new Ingredient();
        dryMustard.setName("dry mustard");
        dryMustard.setImageName("drymustard.png");
        dryMustard.setType("spice");

        Ingredient butter = new Ingredient();
        butter.setName("butter");
        butter.setImageName("butter.png");
        butter.setType("dairy");

        this.mongoTemplate.insert(apple);
        this.mongoTemplate.insert(carrot);
        this.mongoTemplate.insert(garlic);
        this.mongoTemplate.insert(salt);
        this.mongoTemplate.insert(pepper);
        this.mongoTemplate.insert(steak);
        this.mongoTemplate.insert(orange);
        this.mongoTemplate.insert(broccoli);
        this.mongoTemplate.insert(chicken);
        this.mongoTemplate.insert(butter);
        this.mongoTemplate.insert(dryMustard);

        /* ----------------- ADD TO WHITELIST ----------------------*/
        //[chop,peel,slice,boil,flatten]

        Whitelist w1=new Whitelist();
        w1.setName(apple.getName());
        w1.getActions().add("slice");
        w1.getActions().add("peel");

        Whitelist w2=new Whitelist();
        w2.setName(carrot.getName());
        w2.getActions().add("chop");
        w2.getActions().add("peel");

        Whitelist w3=new Whitelist();
        w3.setName(garlic.getName());
//        w3.getActions().add("flatten");

        Whitelist w4=new Whitelist();
        w4.setName(salt.getName());
        w4.getActions().add("season");

        Whitelist w5=new Whitelist();
        w5.setName(pepper.getName());
        w5.getActions().add("season");

        Whitelist w6=new Whitelist();
        w6.setName(steak.getName());
        w6.getActions().add("season");
        w6.getActions().add("slice");

        Whitelist w7=new Whitelist();
        w7.setName(orange.getName());
        w7.getActions().add("slice");

        Whitelist w8=new Whitelist();
        w8.setName(broccoli.getName());
        w8.getActions().add("chop");

        Whitelist w9=new Whitelist();
        w9.setName(chicken.getName());
        w9.getActions().add("season");
        w9.getActions().add("boil");
        w9.getActions().add("chop");

        Whitelist w10 = new Whitelist();
        w10.setName(butter.getName());
        w10.getActions().add("spread");

        Whitelist w11 = new Whitelist();
        w11.setName(dryMustard.getName());
        w11.getActions().add("spice");

        this.mongoTemplate.insert(w1);
        this.mongoTemplate.insert(w2);
        this.mongoTemplate.insert(w3);
        this.mongoTemplate.insert(w4);
        this.mongoTemplate.insert(w5);
        this.mongoTemplate.insert(w6);
        this.mongoTemplate.insert(w7);
        this.mongoTemplate.insert(w8);
        this.mongoTemplate.insert(w9);
        this.mongoTemplate.insert(w10);

        /* ----------------- SUBPROCEDURE ----------------------*/

        Subprocedure chopCarrot=new Subprocedure();
        chopCarrot.setProcedureName("chop");
        chopCarrot.setInstructions("Chop 1 carrot");
//        chopCarrot.setGame(new GameApplication());

        /* ----------------- SAMPLE RECIPE ----------------------*/
        Recipe recipe=new Recipe();
        recipe.setCreator(user.getUsername());
        recipe.setRecipeName("Chopping Carrot Recipe");
        recipe.getSubprocedureList().add(chopCarrot);
        recipe.setIsPublished(true);
        recipe.getIngredients().add(carrot);
        recipe.getKitchenTools().add(knife);
        recipeService.saveRecipe(recipe,user.getUsername());
        user.getRecipesCreated().add(recipe);
        //userDomainService.saveUser(user);


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

        //System.out.println("Pre-Signed URL: " + s3Services.getImageUrl("apple.png"));


        //==================NEW RECIPE: FRIED FLANK STEAK ===================================
        //INGREDIENTS:
        //1.STEAK
        //2.SALT
        //3.PEPPER
        //4.DRY MUSTARD
        //5.BUTTER
        //TOOLS:
        //1.KNIFE
        //2.PAN
        //INSTRUCTIONS:
        //1.CUT STEAK
        //2.APPLY SALT
        //3.APPLY PEPPER
        //4.APPLY DRY MUSTARD
        //5.APPLY BUTTER
        Subprocedure cutSteak = new Subprocedure();
        cutSteak.setProcedureName("slice");
        cutSteak.setInstructions("Slice Steak");
        Subprocedure applySalt = new Subprocedure();
        applySalt.setProcedureName("apply");
        applySalt.setInstructions("Apply Salt");
        Subprocedure applyPepper = new Subprocedure();
        applyPepper.setProcedureName("apply");
        applyPepper.setInstructions("Apply Pepper");
        Subprocedure applyDryMustard = new Subprocedure();
        applyDryMustard.setProcedureName("apply");
        applyDryMustard.setInstructions("Apply Dry Mustard");
        Subprocedure applyButter = new Subprocedure();
        applyButter.setProcedureName("spread");
        applyButter.setInstructions("Spread Butter");
//        chopCarrot.setGame(new GameApplication());

        /* ----------------- Fried Flank Steak RECIPE ----------------------*/
        Recipe steakrecipe = new Recipe();
        steakrecipe.setCreator(user.getUsername());
        steakrecipe.setRecipeName("Fried Flank Steak");
        steakrecipe.getSubprocedureList().add(cutSteak);
        steakrecipe.getSubprocedureList().add(applySalt);
        steakrecipe.getSubprocedureList().add(applyPepper);
        steakrecipe.getSubprocedureList().add(applyDryMustard);
        steakrecipe.getSubprocedureList().add(applyButter);
        steakrecipe.setIsPublished(true);
        steakrecipe.getIngredients().add(steak);
        steakrecipe.getIngredients().add(salt);
        steakrecipe.getIngredients().add(pepper);
        steakrecipe.getIngredients().add(dryMustard);
        steakrecipe.getIngredients().add(butter);
        steakrecipe.getKitchenTools().add(knife);
        steakrecipe.getKitchenTools().add(pan);
        recipeService.saveRecipe(steakrecipe,user.getUsername());
        user.getRecipesCreated().add(steakrecipe);
        userDomainService.saveUser(user);






    }
}