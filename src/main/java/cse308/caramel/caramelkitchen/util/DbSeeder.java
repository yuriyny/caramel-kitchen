package cse308.caramel.caramelkitchen.util;

import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.GameRepository;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeEditorService;
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
import java.util.Collections;
import java.util.List;


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
    @Autowired
    GameRepository gameRepository;
    @Autowired
    RecipeEditorService recipeEditorService;

    //we can use mongotemplate class or repository interface for managing data in mongodb
    private MongoTemplate mongoTemplate;

    public DbSeeder(MongoTemplate mongoTemplate) {
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
        this.mongoTemplate.dropCollection(Game.class);
        /* ----------------- ADD USER ----------------------*/
        User user = new User();
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
        mixingBowl.getActions().add("pour");
        mixingBowl.getActions().add("marinate");
        mixingBowl.setImageName("bowl.png");

        KitchenTool mixingSpoon = new KitchenTool();
        mixingSpoon.setName("mixing spoon");
        mixingBowl.getActions().add("mix");

        KitchenTool pan = new KitchenTool();
        pan.setName("pan");
        pan.getActions().add("sauté");
        pan.getActions().add("fry");
        pan.setImageName("pan.png");

        KitchenTool pot = new KitchenTool();
        pot.setName("pot");
        pot.getActions().add("boil");
        pot.setImageName("pot.png");

        KitchenTool spatula = new KitchenTool();
        spatula.setName("spatula");
        spatula.getActions().add("flip");
        spatula.getActions().add("stir");

        KitchenTool whisk = new KitchenTool();
        whisk.setName("whisk");
        whisk.getActions().add("whisk");
        whisk.setImageName("whisk.png");

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
        this.mongoTemplate.insert(whisk);

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
        butter.setType("oil");

        Ingredient milk = new Ingredient();
        milk.setName("milk");
        milk.setImageName("milk.png");
        milk.setType("liquid");

        Ingredient cinnamon = new Ingredient();
        cinnamon.setName("cinnamon");
        cinnamon.setImageName("cinnamon.png");
        cinnamon.setType("spice");

        Ingredient breadSlice = new Ingredient();
        breadSlice.setName("bread slice");
        breadSlice.setImageName("breadslice.png");
        breadSlice.setType("other");

        Ingredient mapleSyrup = new Ingredient();
        mapleSyrup.setName("maple syrup");
        mapleSyrup.setImageName("maplesyrup.png");
        mapleSyrup.setType("other");

        Ingredient egg = new Ingredient();
        egg.setName("egg");
        egg.setImageName("egg.png");
        egg.setType("other");

        Ingredient veggieOil=new Ingredient();
        veggieOil.setType("vegetable oil");
        veggieOil.setType("oil");

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
        this.mongoTemplate.insert(milk);
        this.mongoTemplate.insert(cinnamon);
        this.mongoTemplate.insert(breadSlice);
        this.mongoTemplate.insert(mapleSyrup);
        this.mongoTemplate.insert(egg);
        this.mongoTemplate.insert(veggieOil);

        /* ----------------- ADD TO WHITELIST ----------------------*/
        //[chop,peel,slice,boil,flatten]

        Whitelist w1 = new Whitelist();
        w1.setName(apple.getName());
        w1.getActions().add("slice");
        w1.getActions().add("peel");

        Whitelist w2 = new Whitelist();
        w2.setName(carrot.getName());
        w2.getActions().add("chop");
        w2.getActions().add("peel");

        Whitelist w3 = new Whitelist();
        w3.setName(garlic.getName());
//        w3.getActions().add("flatten");

        Whitelist w4 = new Whitelist();
        w4.setName(salt.getName());
        w4.getActions().add("season");

        Whitelist w5 = new Whitelist();
        w5.setName(pepper.getName());
        w5.getActions().add("season");

        Whitelist w6 = new Whitelist();
        w6.setName(steak.getName());
        w6.getActions().add("season");
        w6.getActions().add("slice");
        w6.getActions().add("fry");

        Whitelist w7 = new Whitelist();
        w7.setName(orange.getName());
        w7.getActions().add("slice");

        Whitelist w8 = new Whitelist();
        w8.setName(broccoli.getName());
        w8.getActions().add("chop");

        Whitelist w9 = new Whitelist();
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

        Whitelist w12 = new Whitelist();
        w12.setName(milk.getName());
        w12.getActions().add("pour");
        w12.getActions().add("whisk");
        w12.getActions().add("boil");

        Whitelist w13 = new Whitelist();
        w13.setName(cinnamon.getName());
        w13.getActions().add("season");

        Whitelist w14 = new Whitelist();
        w14.setName(egg.getName());
        w14.getActions().add("whisk");



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
        this.mongoTemplate.insert(w11);
        this.mongoTemplate.insert(w12);
        this.mongoTemplate.insert(w13);
        this.mongoTemplate.insert(w14);

        /* ----------------- TEST ACTIONS ----------------------*/
        List<String> i=new ArrayList<>();//mix
        i.add(apple.getName());
        i.add(steak.getName());
        List<String>t=new ArrayList<>();
        t.add(mixingBowl.getName());
        t.add(mixingSpoon.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        i=new ArrayList<>();//marinate
        i.add(salt.getName());
        i.add(steak.getName());
        t=new ArrayList<>();
        t.add(mixingBowl.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        i=new ArrayList<>();//none
        i.add(salt.getName());
        i.add(milk.getName());
        t=new ArrayList<>();
        t.add(mixingBowl.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        IntermediateIngredient mixedAppleAndMilk=new IntermediateIngredient();
        mixedAppleAndMilk.setName("mixed apple and milk");
        mixedAppleAndMilk.getIngredients().add(apple);
        mixedAppleAndMilk.getIngredients().add(milk);
        mixedAppleAndMilk.setTag("mix");
        i=new ArrayList<>();//marinate
        t=new ArrayList<>();
        t.add(mixingBowl.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>(Collections.singleton(mixedAppleAndMilk))));

        i=new ArrayList<>();//add salt
        i.add(salt.getName());
        i.add(chicken.getName());
        t=new ArrayList<>();
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        i=new ArrayList<>();//[]
        i.add(salt.getName());
        i.add(pepper.getName());
        t=new ArrayList<>();
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        i=new ArrayList<>();//boil
        i.add(milk.getName());
        i.add(chicken.getName());
        t=new ArrayList<>();
        t.add(pot.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        i=new ArrayList<>();//boil
        i.add(milk.getName());
        t=new ArrayList<>();
        t.add(kettle.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        i=new ArrayList<>();//sauté
        i.add(veggieOil.getName());
        t=new ArrayList<>();
        t.add(pan.getName());
        t.add(spatula.getName());
        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
        /* ----------------- SUBPROCEDURE ----------------------*/

        Subprocedure chopCarrot = new Subprocedure();
        chopCarrot.setProcedureName("chop");
        chopCarrot.setInstructions("Chop 1 carrot");
//        chopCarrot.setGame(new GameApplication());

        /* ----------------- SAMPLE RECIPE ----------------------*/
        Recipe recipe = new Recipe();
        recipe.setCreator(user.getUsername());
        recipe.setRecipeName("Chopping Carrot Recipe");
        recipe.getSubprocedureList().add(chopCarrot);
        recipe.setIsPublished(true);
        recipe.getIngredients().add(carrot);
        recipe.getKitchenTools().add(knife);
        recipeService.saveRecipe(recipe, user.getUsername());
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
        Subprocedure frySteak = new Subprocedure();
        frySteak.setProcedureName("fry");
        frySteak.setInstructions("Fry Steak");
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
        steakrecipe.getSubprocedureList().add(frySteak);
        steakrecipe.setIsPublished(true);
        steakrecipe.getIngredients().add(steak);
        steakrecipe.getIngredients().add(salt);
        steakrecipe.getIngredients().add(pepper);
        steakrecipe.getIngredients().add(dryMustard);
        steakrecipe.getIngredients().add(butter);
        steakrecipe.getKitchenTools().add(knife);
        steakrecipe.getKitchenTools().add(pan);

        recipeService.saveRecipe(steakrecipe, user.getUsername());
        user.getRecipesCreated().add(steakrecipe);
        //userDomainService.saveUser(user);

        //French Toast Recipe
        //INGREDIENTS:
        //1.4 EGGS
        //2.2/3 CUP MILK
        //3.2 TEASPOONS CINNAMON
        //4.8 BREAD SLICES
        //5.BUTTER
        //6. MAPLE SYRUP
        //TOOLS:
        //1.BOWL
        //2.WHISK
        //3.PAN
        //INSTRUCTIONS:
        //1.POUR MILK INTO A BOWL
        //2.ADD 4 EGGS TO THE BOWL
        //3.ADD 2 TEASPOONS OF CINNAMON
        //4.WHISK TOGETHER THE EGGS, MILK, AND CINNAMON
        //5.SOAK EACH BREAD SLICE INTO THE MIXTURE
        //6.ADD BUTTER TO A PAN
        //7.FRY THE SOAKED SLICES On THE PAN
        Subprocedure pourMilk = new Subprocedure();
        pourMilk.setProcedureName("pour");
        pourMilk.setInstructions("Pour 2/3 cup of milk into a bowl");
        Subprocedure addEggs = new Subprocedure();
        addEggs.setProcedureName("add");
        addEggs.setInstructions("Add 4 eggs");
        Subprocedure addCinnamon = new Subprocedure();
        addCinnamon.setProcedureName("add");
        addCinnamon.setInstructions("Add 2 teaspoons of cinnamon");
        Subprocedure whiskMilk = new Subprocedure();
        whiskMilk.setProcedureName("whisk");
        whiskMilk.setInstructions("Whisk together the eggs, milk, and cinnamon");
        Subprocedure addButter = new Subprocedure();
        addButter.setProcedureName("add");
        addButter.setInstructions("Add butter to a pan");
        Subprocedure frySlices = new Subprocedure();
        frySlices.setProcedureName("fry");
        frySlices.setInstructions("Fry the slices");
//        chopCarrot.setGame(new GameApplication());

        /* ----------------- Fried Flank Steak RECIPE ----------------------*/
        Recipe frenchToastrecipe = new Recipe();
        frenchToastrecipe.setCreator(user.getUsername());
        frenchToastrecipe.setRecipeName("French Toast Recipe");
        frenchToastrecipe.getSubprocedureList().add(pourMilk);
        frenchToastrecipe.getSubprocedureList().add(addEggs);
        frenchToastrecipe.getSubprocedureList().add(addCinnamon);
        frenchToastrecipe.getSubprocedureList().add(whiskMilk);
        frenchToastrecipe.getSubprocedureList().add(addButter);
        frenchToastrecipe.getSubprocedureList().add(frySlices);

        frenchToastrecipe.setIsPublished(true);
        frenchToastrecipe.getIngredients().add(egg);
        frenchToastrecipe.getIngredients().add(milk);
        frenchToastrecipe.getIngredients().add(cinnamon);
        frenchToastrecipe.getIngredients().add(breadSlice);
        frenchToastrecipe.getIngredients().add(butter);
        frenchToastrecipe.getIngredients().add(mapleSyrup);

        frenchToastrecipe.getKitchenTools().add(mixingBowl);
        frenchToastrecipe.getKitchenTools().add(pan);
        System.out.println(user.getUsername());
        recipeService.saveRecipe(frenchToastrecipe, user.getUsername());
        user.getRecipesCreated().add(frenchToastrecipe);
        userDomainService.saveUser(user);



        /////////////////////////////////////////////////////////////////////////////
        //Adding Game
        Game game1 = new Game();
        game1.setRecipe(steakrecipe);
        game1.setGameState(new GameState());
        gameRepository.save(game1);
        //System.out.println(game1.getId());
        game1.getGameState().setRecipeId(steakrecipe.getId());
        game1.getGameState().setGameId(game1.getId());
        //After each subprocedure do we create a new intermediateIngredient?
        IntermediateIngredient ii1 = new IntermediateIngredient();
        //we cut steak, so we add steak to the list of ingredients?
        ii1.getIngredients().add(steak);
        IntermediateIngredient ii2 = new IntermediateIngredient();
        ii2.getIngredients().add(salt);
        //adding previous intermediateIngredient?
        ii2.getIntermediateIngredients().add(ii1);

        game1.getGameState().getIntermediateIngredients().add(ii1);
        game1.getGameState().getIntermediateIngredients().add(ii2);
        game1.setScore(250.0);
        //saving uncomplited game with two finished steps
        gameRepository.save(game1);

        //Adding Game
        Game game2 = new Game();
        game2.setRecipe(steakrecipe);
        game2.setGameState(new GameState());
        gameRepository.save(game2);
        System.out.println(game2.getId());
        game2.getGameState().setRecipeId(steakrecipe.getId());
        game2.getGameState().setGameId(game2.getId());
        //After each subprocedure do we create a new intermediateIngredient?
        //cut steak
        IntermediateIngredient ii12 = new IntermediateIngredient();
        //we cut steak, so we add steak to the list of ingredients?
        ii12.getIngredients().add(steak);
        //apply salt
        IntermediateIngredient ii22 = new IntermediateIngredient();
        ii22.getIngredients().add(salt);
        //adding previous intermediateIngredient?
        ii22.getIntermediateIngredients().add(ii12);
        //apply pepper
        //ii22 = salt + pepper
        //ii32 = ii22 + pepper
        IntermediateIngredient ii32 = new IntermediateIngredient();
        ii32.getIntermediateIngredients().add(ii22);
        ii32.getIngredients().add(pepper);
        //apply dry mustard
        IntermediateIngredient ii42 = new IntermediateIngredient();
        ii42.getIntermediateIngredients().add(ii32);
        ii42.getIngredients().add(dryMustard);
        //spread butter
        IntermediateIngredient ii52 = new IntermediateIngredient();
        ii52.getIntermediateIngredients().add(ii42);
        ii52.getIngredients().add(butter);
        //fry steak
        //seasoned steak = ii52
        //no ingredients needed for this step
        IntermediateIngredient ii62 = new IntermediateIngredient();
        ii62.getIntermediateIngredients().add(ii52);

        game2.getGameState().getIntermediateIngredients().add(ii12);
        game2.getGameState().getIntermediateIngredients().add(ii22);
        game2.getGameState().getIntermediateIngredients().add(ii32);
        game2.getGameState().getIntermediateIngredients().add(ii42);
        game2.getGameState().getIntermediateIngredients().add(ii52);
        game2.getGameState().getIntermediateIngredients().add(ii62);
        game2.setScore(1050.0);
        //saving completed game with All finished steps
        gameRepository.save(game2);
    }
}