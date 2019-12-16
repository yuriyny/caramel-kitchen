package cse308.caramel.caramelkitchen.util;

import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.GameRepository;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeEditorService;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.request.service.RequestService;
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
    @Autowired
    RequestService requestService;

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

        User user1 = new User();
        user1.setUsername("Teresa");
        user1.setEnabled(true);
        user1.setPassword("password");
        userDomainService.saveNewUser(user1);

        User user2 = new User();
        user2.setUsername("David");
        user2.setEnabled(true);
        user2.setPassword("password");
        userDomainService.saveNewUser(user2);

        User user3 = new User();
        user3.setUsername("Allen");
        user3.setEnabled(true);
        user3.setPassword("password");
        userDomainService.saveNewUser(user3);

        User user4 = new User();
        user4.setUsername("Yuriy");
        user4.setEnabled(true);
        user4.setPassword("password");
        userDomainService.saveNewUser(user4);
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
        teaspoon.setImageName("teaspoon.png");
        teaspoon.getActions().add("measure");
        teaspoon.setUnitOfMeasure("teaspoon");

        KitchenTool tablespoon = new KitchenTool();
        tablespoon.setName("tablespoon");
        tablespoon.setImageName("tablespoon.png");
        tablespoon.getActions().add("measure");
        tablespoon.setUnitOfMeasure("tablespoon");

        KitchenTool measuringCup = new KitchenTool();
        measuringCup.setName("measuring cup");
        measuringCup.setImageName("measuringcup.png");
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
        mixingSpoon.setImageName("mixingspoon.png");
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
        spatula.setImageName("spatula.png");
        spatula.getActions().add("flip");
        spatula.getActions().add("stir");

        KitchenTool whisk = new KitchenTool();
        whisk.setName("whisk");
        whisk.getActions().add("whisk");
        whisk.setImageName("whisk.png");

        KitchenTool plate = new KitchenTool();
        plate.setName("plate");
        plate.getActions().add("put");
        plate.getActions().add("add");
        plate.getActions().add("dredge");
        plate.setImageName("plate.png");

        KitchenTool potatomasher = new KitchenTool();
        potatomasher.setName("potato masher");
        potatomasher.setImageName("potatomasher.png");
        potatomasher.getActions().add("mash");

        KitchenTool drainer = new KitchenTool();
        drainer.setName("drainer");
        drainer.setImageName("drainer.png");
        drainer.getActions().add("drain");

        KitchenTool butterKnife = new KitchenTool();
        butterKnife.setName("butter knife");
        butterKnife.setImageName("butterKnife.png");
        butterKnife.getActions().add("spread");

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
        this.mongoTemplate.insert(plate);
        this.mongoTemplate.insert(potatomasher);
        this.mongoTemplate.insert(drainer);
        this.mongoTemplate.insert(butterKnife);

        /* ----------------- ADD INGREDIENTS ----------------------*/
        Ingredient apple = new Ingredient();
        apple.setName("apple");
        apple.setImageName("apple.png");
        apple.setType("fruit");
        apple.setUploader(user.getUsername());

        Ingredient carrot = new Ingredient();
        carrot.setName("carrot");
        carrot.setImageName("carrot.png");
        carrot.setType("vegetable");
        carrot.setUploader(user.getUsername());

        Ingredient garlic = new Ingredient();
        garlic.setName("garlic");
        garlic.setImageName("garlic.png");
        garlic.setType("vegetable");
        garlic.setUploader(user.getUsername());

        Ingredient salt = new Ingredient();
        salt.setName("salt");
        salt.getUnitOfMeasure().add("teaspoon");
        salt.getUnitOfMeasure().add("tablespoon");
        salt.setImageName("salt.png");
        salt.setType("spice");
        salt.setUploader(user.getUsername());

        Ingredient pepper = new Ingredient();
        pepper.setName("pepper");
        pepper.getUnitOfMeasure().add("teaspoon");
        pepper.getUnitOfMeasure().add("tablespoon");
        pepper.setImageName("pepper.png");
        pepper.setType("spice");
        pepper.setUploader(user.getUsername());

        Ingredient steak = new Ingredient();
        steak.setName("steak");
        steak.setImageName("steak.png");
        steak.setType("meat");
        steak.setUploader(user.getUsername());

        Ingredient orange = new Ingredient();
        orange.setName("orange");
        orange.setImageName("orange.png");
        orange.setType("fruit");
        orange.setUploader(user.getUsername());

        Ingredient broccoli = new Ingredient();
        broccoli.setName("broccoli");
        broccoli.setImageName("broccoli.png");
        broccoli.setType("vegetable");
        broccoli.setUploader(user.getUsername());

        Ingredient chicken = new Ingredient();
        chicken.setName("chicken");
        chicken.setImageName("chicken.png");
        chicken.setType("meat");
        chicken.setUploader(user.getUsername());

        Ingredient dryMustard = new Ingredient();
        dryMustard.setName("dry mustard");
        dryMustard.setImageName("drymustard.png");
        dryMustard.setType("spice");
        dryMustard.setUploader(user.getUsername());

        Ingredient butter = new Ingredient();
        butter.setName("butter");
        butter.setImageName("butter.png");
        butter.setType("oil");
        butter.setUploader(user.getUsername());;

        Ingredient milk = new Ingredient();
        milk.setName("milk");
        milk.setImageName("milk.png");
        milk.setType("liquid");
        milk.setUploader(user.getUsername());

        Ingredient cinnamon = new Ingredient();
        cinnamon.setName("cinnamon");
        cinnamon.setImageName("cinnamon.png");
        cinnamon.setType("spice");
        cinnamon.setUploader(user.getUsername());

        Ingredient breadSlice = new Ingredient();
        breadSlice.setName("bread slice");
        breadSlice.setImageName("breadslice.png");
        breadSlice.setType("bread");
        breadSlice.setUploader(user.getUsername());

        Ingredient mapleSyrup = new Ingredient();
        mapleSyrup.setName("maple syrup");
        mapleSyrup.setImageName("maplesyrup.png");
        mapleSyrup.setType("other");
        mapleSyrup.setUploader(user.getUsername());

        Ingredient egg = new Ingredient();
        egg.setName("egg");
        egg.setImageName("egg.png");
        egg.setType("other");
        egg.setUploader(user.getUsername());

        Ingredient veggieOil=new Ingredient();
        veggieOil.setName("vegetable oil");
        veggieOil.setImageName("vegetableoil.png");
        veggieOil.setType("oil");
        veggieOil.setUploader(user.getUsername());

        Ingredient whitewine = new Ingredient();
        whitewine.setName("white wine");
        whitewine.setImageName("whitewine.png");
        whitewine.setType("liquid");
        whitewine.setUploader(user.getUsername());

        Ingredient lemonjuice = new Ingredient();
        lemonjuice.setName("lemon juice");
        lemonjuice.setImageName("lemonjuice.png");
        lemonjuice.setType("liquid");
        lemonjuice.setUploader(user.getUsername());

        Ingredient choppedparsley = new Ingredient();
        choppedparsley.setName("chopped parsley");
        choppedparsley.setImageName("choppedparsley.png");
        choppedparsley.setType("spice");
        choppedparsley.setUploader(user.getUsername());

        Ingredient extravirginoil = new Ingredient();
        extravirginoil.setName("extra virgin oil");
        extravirginoil.setImageName("extravirginoil.png");
        extravirginoil.setType("oil");
        extravirginoil.setUploader(user.getUsername());

        Ingredient parmesancheese = new Ingredient();
        parmesancheese.setName("parmesan cheese");
        parmesancheese.setImageName("parmesancheese.png");
        parmesancheese.setType("dairy");
        parmesancheese.setUploader(user.getUsername());

        Ingredient flour = new Ingredient();
        flour.setName("flour");
        flour.setImageName("flour.png");
        flour.setType("grain");
        flour.setUploader(user.getUsername());

        Ingredient chickenbreast = new Ingredient();
        chickenbreast.setName("chicken breast");
        chickenbreast.setImageName("chickenbreast.png");
        chickenbreast.setType("meat");
        chickenbreast.setUploader(user.getUsername());

        Ingredient basil = new Ingredient();
        basil.setName("basil");
        basil.setImageName("basil.png");
        basil.setType("spice");
        basil.setUploader(user.getUsername());

        Ingredient water = new Ingredient();
        water.setName("water");
        water.setImageName("water.png");
        water.setType("liquid");
        water.setUploader(user.getUsername());

        Ingredient garlicclove = new Ingredient();
        garlicclove.setName("garlic clove");
        garlicclove.setImageName("garlicclove.png");
        garlicclove.setType("spice");
        garlicclove.setUploader(user.getUsername());

        Ingredient smallpasta = new Ingredient();
        smallpasta.setName("small pasta");
        smallpasta.setImageName("smallpasta.png");
        smallpasta.setType("grain");
        smallpasta.setUploader(user.getUsername());

        Ingredient mozzarella = new Ingredient();
        mozzarella.setName("mozzarella");
        mozzarella.setImageName("mozzarella.png");
        mozzarella.setType("dairy");
        mozzarella.setUploader(user.getUsername());

        Ingredient cherrytomato = new Ingredient();
        cherrytomato.setName("cherry tomato");
        cherrytomato.setImageName("cherrytomato.png");
        cherrytomato.setType("vegetable");
        cherrytomato.setUploader(user.getUsername());

        Ingredient potato = new Ingredient();
        potato.setName("potato");
        potato.setImageName("potato.png");
        potato.setType("vegetable");
        potato.setUploader(user.getUsername());

        Ingredient creamCheese = new Ingredient();
        creamCheese.setName("cream cheese");
        creamCheese.setImageName("creamCheese.png");
        creamCheese.setType("spread");
        creamCheese.setUploader(user.getUsername());

        Ingredient strawberry = new Ingredient();
        strawberry.setName("strawberry");
        strawberry.setImageName("strawberry.png");
        strawberry.setType("fruit");
        strawberry.setUploader(user.getUsername());

        Ingredient cherry = new Ingredient();
        cherry.setName("cherry");
        cherry.setImageName("cherry.png");
        cherry.setType("fruit");
        cherry.setUploader(user.getUsername());

        Ingredient pear = new Ingredient();
        pear.setName("pear");
        pear.setImageName("pear.png");
        pear.setType("fruit");
        pear.setUploader(user.getUsername());

        Ingredient liver = new Ingredient();
        liver.setName("liver");
        liver.setImageName("liver.png");
        liver.setType("meat");
        liver.setUploader(user.getUsername());

        Ingredient sugar = new Ingredient();
        sugar.setName("sugar");
        sugar.setImageName("sugar.png");
        sugar.setType("other");
        sugar.setUploader(user.getUsername());

        Ingredient peanutbutter = new Ingredient();
        peanutbutter.setName("peanut butter");
        peanutbutter.setImageName("peanutbutter.png");
        peanutbutter.setType("spread");
        peanutbutter.setUploader(user.getUsername());

        Ingredient rice = new Ingredient();
        rice.setName("rice");
        rice.setImageName("rice.png");
        rice.setType("grain");
        rice.setUploader(user.getUsername());

        Ingredient allseasoning = new Ingredient();
        allseasoning.setName("all purpose seasoning");
        allseasoning.setImageName("allpurposeseasoning.png");
        allseasoning.setType("spice");
        allseasoning.setUploader(user.getUsername());

        Ingredient cacao = new Ingredient();
        cacao.setName("cacao");
        cacao.setImageName("cacao.png");
        cacao.setType("other");
        cacao.setUploader(user.getUsername());

        Ingredient mayonnaise = new Ingredient();
        mayonnaise.setName("mayonnaise");
        mayonnaise.setImageName("mayonnaise.png");
        mayonnaise.setType("other");
        mayonnaise.setUploader(user.getUsername());

        Ingredient pineapple = new Ingredient();
        pineapple.setName("pineapple");
        pineapple.setImageName("pineapple.png");
        pineapple.setType("fruit");
        pineapple.setUploader(user.getUsername());

        Ingredient sourcream = new Ingredient();
        sourcream.setName("sour cream");
        sourcream.setImageName("sourcream.png");
        sourcream.setType("other");
        sourcream.setUploader(user.getUsername());

        Ingredient mustard = new Ingredient();
        mustard.setName("mustard");
        mustard.setImageName("mustard.png");
        mustard.setType("other");
        mustard.setUploader(user.getUsername());

        Ingredient cucumber = new Ingredient();
        cucumber.setName("cucumber");
        cucumber.setImageName("cucumber.png");
        cucumber.setType("vegetable");
        cucumber.setUploader(user.getUsername());

        Ingredient avocado = new Ingredient();
        avocado.setName("avocado");
        avocado.setImageName("avocado.png");
        avocado.setType("vegetable");
        avocado.setUploader(user.getUsername());

        Ingredient onion = new Ingredient();
        onion.setName("onion");
        onion.setImageName("onion.png");
        onion.setType("vegetable");
        onion.setUploader(user.getUsername());

        Ingredient redonion = new Ingredient();
        redonion.setName("red onion");
        redonion.setImageName("redonion.png");
        redonion.setType("vegetable");
        redonion.setUploader(user.getUsername());

        Ingredient squash = new Ingredient();
        squash.setName("squash");
        squash.setImageName("squash.png");
        squash.setType("vegetable");
        squash.setUploader(user.getUsername());




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
        this.mongoTemplate.insert(whitewine);
        this.mongoTemplate.insert(choppedparsley);
        this.mongoTemplate.insert(lemonjuice);
        this.mongoTemplate.insert(chickenbreast);
        this.mongoTemplate.insert(parmesancheese);
        this.mongoTemplate.insert(extravirginoil);
        this.mongoTemplate.insert(flour);
        this.mongoTemplate.insert(basil);
        this.mongoTemplate.insert(water);
        this.mongoTemplate.insert(garlicclove);
        this.mongoTemplate.insert(smallpasta);
        this.mongoTemplate.insert(mozzarella);
        this.mongoTemplate.insert(cherrytomato);
        this.mongoTemplate.insert(potato);
        this.mongoTemplate.insert(creamCheese);
        this.mongoTemplate.insert(strawberry);
        this.mongoTemplate.insert(cherry);
        this.mongoTemplate.insert(liver);
        this.mongoTemplate.insert(pear);
        this.mongoTemplate.insert(sugar);
        this.mongoTemplate.insert(peanutbutter);
        this.mongoTemplate.insert(rice);
        this.mongoTemplate.insert(allseasoning);
        this.mongoTemplate.insert(cacao);
        this.mongoTemplate.insert(mayonnaise);
        this.mongoTemplate.insert(pineapple);
        this.mongoTemplate.insert(sourcream);
        this.mongoTemplate.insert(mustard);
        this.mongoTemplate.insert(cucumber);
        this.mongoTemplate.insert(avocado);
        this.mongoTemplate.insert(onion);
        this.mongoTemplate.insert(redonion);
        this.mongoTemplate.insert(squash);

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

        Whitelist w4 = new Whitelist();
        w4.setName(salt.getName());

        Whitelist w5 = new Whitelist();
        w5.setName(pepper.getName());

        Whitelist w6 = new Whitelist();
        w6.setName(steak.getName());
        w6.getActions().add("slice");

        Whitelist w7 = new Whitelist();
        w7.setName(orange.getName());
        w7.getActions().add("slice");

        Whitelist w8 = new Whitelist();
        w8.setName(broccoli.getName());
        w8.getActions().add("chop");

        Whitelist w9 = new Whitelist();
        w9.setName(chicken.getName());
        w9.getActions().add("chop");

        Whitelist w10 = new Whitelist();
        w10.setName(butter.getName());

        Whitelist w11 = new Whitelist();
        w11.setName(dryMustard.getName());

        Whitelist w12 = new Whitelist();
        w12.setName(milk.getName());

        Whitelist w13 = new Whitelist();
        w13.setName(cinnamon.getName());

        Whitelist w14 = new Whitelist();
        w14.setName(egg.getName());

        Whitelist w15=new Whitelist();
        w15.setName(veggieOil.getName());

        Whitelist w16=new Whitelist();
        w16.setName(breadSlice.getName());
        w16.getActions().add("slice");
        w16.getActions().add("flatten");
        w16.getActions().add("flip");

        Whitelist w17=new Whitelist();
        w17.setName(mapleSyrup.getName());

        Whitelist w18 = new Whitelist();
        w18.setName(cherrytomato.getName());
        w18.getActions().add("slice");

        Whitelist w19 = new Whitelist();
        w19.setName(basil.getName());
        w19.getActions().add("chop");

        Whitelist w20 = new Whitelist();
        w20.setName(mozzarella.getName());
        w20.getActions().add("chop");

        Whitelist w21 = new Whitelist();
        w21.setName(water.getName());

        Whitelist w22 = new Whitelist();
        w22.setName(garlicclove.getName());
        w22.getActions().add("chop");

        Whitelist w23= new Whitelist();
        w23.setName(smallpasta.getName());

        Whitelist w24 = new Whitelist();
        w24.setName(whitewine.getName());

        Whitelist w25 = new Whitelist();
        w25.setName(lemonjuice.getName());

        Whitelist w26 = new Whitelist();
        w26.setName(chickenbreast.getName());
        w26.getActions().add("slice");

        Whitelist w27 = new Whitelist();
        w27.setName(choppedparsley.getName());

        Whitelist w28 = new Whitelist();
        w28.setName(extravirginoil.getName());

        Whitelist w29 = new Whitelist();
        w29.setName(flour.getName());

        Whitelist w30 = new Whitelist();
        w30.setName(parmesancheese.getName());

        Whitelist w31 = new Whitelist();
        w31.setName(potato.getName());
        w31.getActions().add("chop");
        w31.getActions().add("peel");

        Whitelist w32 = new Whitelist();
        w32.setName(creamCheese.getName());

        Whitelist w33 = new Whitelist();
        w33.setName(strawberry.getName());
        w33.getActions().add("chop");

        Whitelist w34 = new Whitelist();
        w34.setName(cherry.getName());

        Whitelist w35 = new Whitelist();
        w35.setName(liver.getName());

        Whitelist w36 = new Whitelist();
        w36.setName(pear.getName());
        w36.getActions().add("slice");

        Whitelist w37 = new Whitelist();
        w37.setName(sugar.getName());

        Whitelist w38 = new Whitelist();
        w38.setName(peanutbutter.getName());

        Whitelist w39 = new Whitelist();
        w39.setName(rice.getName());

        Whitelist w40 = new Whitelist();
        w40.setName(allseasoning.getName());

        Whitelist w41 = new Whitelist();
        w41.setName(cacao.getName());

        Whitelist w42 = new Whitelist();
        w42.setName(mayonnaise.getName());

        Whitelist w43 = new Whitelist();
        w43.setName(pineapple.getName());
        w43.getActions().add("peel");
        w43.getActions().add("slice");

        Whitelist w44 = new Whitelist();
        w44.setName(sourcream.getName());

        Whitelist w45 = new Whitelist();
        w45.setName(mustard.getName());

        Whitelist w46 = new Whitelist();
        w46.setName(cucumber.getName());
        w46.getActions().add("peel");
        w46.getActions().add("chop");

        Whitelist w47 = new Whitelist();
        w47.setName(avocado.getName());
        w47.getActions().add("peel");
        w47.getActions().add("chop");

        Whitelist w48 = new Whitelist();
        w48.setName(onion.getName());
        w48.getActions().add("peel");
        w48.getActions().add("chop");

        Whitelist w49 = new Whitelist();
        w49.setName(redonion.getName());
        w49.getActions().add("peel");
        w49.getActions().add("chop");

        Whitelist w50 = new Whitelist();
        w50.setName(squash.getName());
        w50.getActions().add("chop");

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
        this.mongoTemplate.insert(w15);
        this.mongoTemplate.insert(w16);
        this.mongoTemplate.insert(w17);
        this.mongoTemplate.insert(w18);
        this.mongoTemplate.insert(w19);
        this.mongoTemplate.insert(w20);
        this.mongoTemplate.insert(w21);
        this.mongoTemplate.insert(w22);
        this.mongoTemplate.insert(w23);
        this.mongoTemplate.insert(w24);
        this.mongoTemplate.insert(w25);
        this.mongoTemplate.insert(w26);
        this.mongoTemplate.insert(w27);
        this.mongoTemplate.insert(w28);
        this.mongoTemplate.insert(w29);
        this.mongoTemplate.insert(w30);
        this.mongoTemplate.insert(w31);
        this.mongoTemplate.insert(w32);
        this.mongoTemplate.insert(w33);
        this.mongoTemplate.insert(w34);
        this.mongoTemplate.insert(w35);
        this.mongoTemplate.insert(w36);
        this.mongoTemplate.insert(w37);
        this.mongoTemplate.insert(w38);
        this.mongoTemplate.insert(w39);
        this.mongoTemplate.insert(w40);
        this.mongoTemplate.insert(w41);
        this.mongoTemplate.insert(w42);
        this.mongoTemplate.insert(w43);
        this.mongoTemplate.insert(w44);
        this.mongoTemplate.insert(w45);
        this.mongoTemplate.insert(w46);
        this.mongoTemplate.insert(w47);
        this.mongoTemplate.insert(w48);
        this.mongoTemplate.insert(w49);
        this.mongoTemplate.insert(w50);


//        /* ----------------- TEST ACTIONS ----------------------*/
//        List<String> i=new ArrayList<>();//mix
//        i.add(apple.getName());
//        i.add(steak.getName());
//        List<String>t=new ArrayList<>();
//        t.add(mixingBowl.getName());
//        t.add(mixingSpoon.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        i=new ArrayList<>();//marinate
//        i.add(salt.getName());
//        i.add(steak.getName());
//        t=new ArrayList<>();
//        t.add(mixingBowl.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        i=new ArrayList<>();//none
//        i.add(salt.getName());
//        i.add(milk.getName());
//        t=new ArrayList<>();
//        t.add(mixingBowl.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        IntermediateIngredient mixedAppleAndMilk=new IntermediateIngredient();
//        mixedAppleAndMilk.setName("mixed apple and milk");
//        mixedAppleAndMilk.getIngredients().add(apple);
//        mixedAppleAndMilk.getIngredients().add(milk);
//        mixedAppleAndMilk.setTag("mix");
//        i=new ArrayList<>();//marinate
//        t=new ArrayList<>();
//        t.add(mixingBowl.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>(Collections.singleton(mixedAppleAndMilk))));
//
//        i=new ArrayList<>();//add salt
//        i.add(salt.getName());
//        i.add(chicken.getName());
//        t=new ArrayList<>();
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        i=new ArrayList<>();//[]
//        i.add(salt.getName());
//        i.add(pepper.getName());
//        t=new ArrayList<>();
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        i=new ArrayList<>();//boil
//        i.add(milk.getName());
//        i.add(chicken.getName());
//        t=new ArrayList<>();
//        t.add(pot.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        i=new ArrayList<>();//boil
//        i.add(milk.getName());
//        t=new ArrayList<>();
//        t.add(kettle.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));
//
//        i=new ArrayList<>();//sauté
//        i.add(veggieOil.getName());
//        t=new ArrayList<>();
//        t.add(pan.getName());
//        t.add(spatula.getName());
//        System.out.println(recipeEditorService.retrieveValidToolActions(i,t,new ArrayList<>()));

        /* ----------------- SAMPLE RECIPES ----------------------*/

        //########### Chop carrot #############
        carrot.setQuantity(1);
        Subprocedure chopCarrot = new Subprocedure();
        chopCarrot.setProcedureName("Chop");
        chopCarrot.setInstructions("Chop 1 carrot[s]");
        chopCarrot.getTargetIngredients().add(carrot);

        IntermediateIngredient choppedCarrot=new IntermediateIngredient();
        choppedCarrot.setTag("Chop");
        choppedCarrot.getIngredients().add(carrot);
        choppedCarrot.setImageName("choppedCarrot.png");
        choppedCarrot.setName("chopped carrot");
        choppedCarrot.setId(23423423);

        Recipe recipe = new Recipe();
        recipe.setCreator(user.getUsername());
        recipe.setRecipeName("Chopping Carrot Recipe");
        recipe.getSubprocedureList().add(chopCarrot);
        recipe.setIsPublished(true);
        recipe.getIngredients().add(carrot);
        recipe.getKitchenTools().add(knife);
        recipe.getIntermediateIngredients().add(choppedCarrot);
        recipeService.saveRecipe(recipe, user.getUsername());
        user.getRecipesCreated().add(recipe);
        userDomainService.saveUser(user);

        //########### Butter bread #############
        butter.setQuantity(1);
        breadSlice.setQuantity(1);
        Subprocedure butterbread=new Subprocedure();
        butterbread.setProcedureName("Spread");
        butterbread.getTargetIngredients().add(breadSlice);
        butterbread.getTargetIngredients().add(butter);
        butterbread.setInstructions("Spread 1 bread slice[s] and 1 butter[s]");

        IntermediateIngredient butteredBread=new IntermediateIngredient();
        butteredBread.setTag("spread");
        butteredBread.getIngredients().add(butter);
        butteredBread.getIngredients().add(breadSlice);
        butteredBread.setImageName("butteredbread.png");
        butteredBread.setName("spread");

        Recipe recipe1 = new Recipe();
        recipe1.setCreator(user1.getUsername());
        recipe1.setRecipeName("Buttered bread");
        recipe1.getSubprocedureList().add(butterbread);
        recipe1.setIsPublished(true);
        recipe1.getIngredients().add(butter);
        recipe1.getIngredients().add(breadSlice);
        recipe1.getKitchenTools().add(butterKnife);
        recipe1.getIntermediateIngredients().add(butteredBread);
        recipeService.saveRecipe(recipe1, user1.getUsername());
        user1.getRecipesCreated().add(recipe1);
        userDomainService.saveUser(user1);

        //########### Chop Peeled Carrot #############
        carrot.setQuantity(1);
        Subprocedure peelCarrot=new Subprocedure();
        peelCarrot.setProcedureName("Peel");
        peelCarrot.getTargetIngredients().add(carrot);
        peelCarrot.setInstructions("Peel 1 carrot[s]");

        Subprocedure chopPeelCarrot=new Subprocedure();
        chopPeelCarrot.setProcedureName("Chop");
//        chopPeeledCarrot.getTargetIngredients().add(carrot); //MAY NEED TO BE CHANGED SO THAT THE INTERMEDIATE INGREDIENTS ARE STORED
        chopPeelCarrot.setInstructions("Chop 1 peeled carrot[s]");

        IntermediateIngredient peeledCarrot=new IntermediateIngredient();
        peeledCarrot.setTag("peel");
        peeledCarrot.getIngredients().add(carrot);
        peeledCarrot.setImageName("peeledcarrot.png");
        peeledCarrot.setName("peel carrot");
        peeledCarrot.setId(234234);

        IntermediateIngredient choppedPeeledCarrot=new IntermediateIngredient();
        choppedPeeledCarrot.setTag("chop");
        choppedPeeledCarrot.getIntermediateIngredients().add(choppedCarrot);
        choppedPeeledCarrot.setImageName("choppedpeeledcarrot.png");
        choppedPeeledCarrot.setName("chopped peeled carrot");
        choppedPeeledCarrot.setId(56745);

        Recipe recipe2 = new Recipe();
        recipe2.setCreator(user2.getUsername());
        recipe2.setRecipeName("Chop peeled carrot");
        recipe2.getSubprocedureList().add(peelCarrot);
        recipe2.getSubprocedureList().add(chopPeelCarrot);
        recipe2.setIsPublished(true);
        recipe2.getIngredients().add(carrot);
        recipe2.getKitchenTools().add(knife);
        recipe2.getIntermediateIngredients().add(peeledCarrot);
        recipe2.getIntermediateIngredients().add(choppedPeeledCarrot);
        recipeService.saveRecipe(recipe2, user2.getUsername());
        user2.getRecipesCreated().add(recipe2);
        userDomainService.saveUser(user2);

        //########### Mix Chopped Apple with Chop Peeled Carrot #############
        apple.setQuantity(1);
        Subprocedure sliceApple=new Subprocedure();
        sliceApple.setProcedureName("slice");
        sliceApple.getTargetIngredients().add(apple);
        sliceApple.setInstructions("Slice 1 apple[s]");

        Subprocedure mixChoppedAppleAndChoppedPeeledCarrot=new Subprocedure();
        mixChoppedAppleAndChoppedPeeledCarrot.setProcedureName("Mix");
//        mixChoppedAppleAndChoppedPeeledCarrot.getTargetIngredients().add(chopApple); //MAY NEED TO BE CHANGED SO THAT THE INTERMEDIATE INGREDIENTS ARE STORED
//        mixChoppedAppleAndChoppedPeeledCarrot.getTargetIngredients().add(choppedPeeledCarrot); //MAY NEED TO BE CHANGED SO THAT THE INTERMEDIATE INGREDIENTS ARE STORED
        mixChoppedAppleAndChoppedPeeledCarrot.setInstructions("Mix 1 peeled carrot[s] and 1 sliced apple[s]");

        IntermediateIngredient slicedApple=new IntermediateIngredient();
        slicedApple.setTag("slice");
        slicedApple.getIngredients().add(apple);
        slicedApple.setImageName("choppedapple.png");
        slicedApple.setName("sliced apple");
        slicedApple.setId(567563);

        IntermediateIngredient mixCarrotSlicedApple=new IntermediateIngredient();
        mixCarrotSlicedApple.setTag("mix");
        mixCarrotSlicedApple.getIntermediateIngredients().add(slicedApple);
        mixCarrotSlicedApple.getIntermediateIngredients().add(choppedPeeledCarrot);
        mixCarrotSlicedApple.setImageName("applecarrotmix.png");
        mixCarrotSlicedApple.setName("mixed apple and carrot");
        mixCarrotSlicedApple.setId(9879);

        Recipe recipe3 = new Recipe();
        recipe3.setCreator(user3.getUsername());
        recipe3.setRecipeName("Mix chopped apple and chop peeled carrot");
        recipe3.getSubprocedureList().add(peelCarrot);
        recipe3.getSubprocedureList().add(chopPeelCarrot);
        recipe3.getSubprocedureList().add(sliceApple);
        recipe3.getSubprocedureList().add(mixChoppedAppleAndChoppedPeeledCarrot);
        recipe3.setIsPublished(true);
        recipe3.getIngredients().add(carrot);
        recipe3.getIngredients().add(apple);
        recipe3.getKitchenTools().add(knife);
        recipe3.getKitchenTools().add(mixingBowl);
        recipe3.getKitchenTools().add(mixingSpoon);
        recipe3.getIntermediateIngredients().add(peeledCarrot);
        recipe3.getIntermediateIngredients().add(choppedPeeledCarrot);
        recipe3.getIntermediateIngredients().add(slicedApple);
        recipe3.getIntermediateIngredients().add(mixCarrotSlicedApple);
        recipeService.saveRecipe(recipe3, user3.getUsername());
        user3.getRecipesCreated().add(recipe3);
        userDomainService.saveUser(user3);


//        g



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


//        //==================NEW RECIPE: FRIED FLANK STEAK ===================================
//        //INGREDIENTS:
//        //1.STEAK
//        //2.SALT
//        //3.PEPPER
//        //4.DRY MUSTARD
//        //5.BUTTER
//        //TOOLS:
//        //1.KNIFE
//        //2.PAN
//        //INSTRUCTIONS:
//        //1.CUT STEAK
//        //2.APPLY SALT
//        //3.APPLY PEPPER
//        //4.APPLY DRY MUSTARD
//        //5.APPLY BUTTER
//        Subprocedure cutSteak = new Subprocedure();
//        cutSteak.setProcedureName("slice");
//        cutSteak.setInstructions("Slice Steak");
//        Subprocedure applySalt = new Subprocedure();
//        applySalt.setProcedureName("apply");
//        applySalt.setInstructions("Apply Salt");
//        Subprocedure applyPepper = new Subprocedure();
//        applyPepper.setProcedureName("apply");
//        applyPepper.setInstructions("Apply Pepper");
//        Subprocedure applyDryMustard = new Subprocedure();
//        applyDryMustard.setProcedureName("apply");
//        applyDryMustard.setInstructions("Apply Dry Mustard");
//        Subprocedure applyButter = new Subprocedure();
//        applyButter.setProcedureName("spread");
//        applyButter.setInstructions("Spread Butter");
//        Subprocedure frySteak = new Subprocedure();
//        frySteak.setProcedureName("fry");
//        frySteak.setInstructions("Fry Steak");
////        chopCarrot.setGame(new GameApplication());
//
//        /* ----------------- Fried Flank Steak RECIPE ----------------------*/
//        Recipe steakrecipe = new Recipe();
//        steakrecipe.setCreator(user.getUsername());
//        steakrecipe.setRating(2.0);
//        steakrecipe.setRecipeName("Fried Flank Steak");
//        steakrecipe.getSubprocedureList().add(cutSteak);
//        steakrecipe.getSubprocedureList().add(applySalt);
//        steakrecipe.getSubprocedureList().add(applyPepper);
//        steakrecipe.getSubprocedureList().add(applyDryMustard);
//        steakrecipe.getSubprocedureList().add(applyButter);
//        steakrecipe.getSubprocedureList().add(frySteak);
//        steakrecipe.setIsPublished(true);
//        steakrecipe.getIngredients().add(steak);
//        steakrecipe.getIngredients().add(salt);
//        steakrecipe.getIngredients().add(pepper);
//        steakrecipe.getIngredients().add(dryMustard);
//        steakrecipe.getIngredients().add(butter);
//        steakrecipe.getKitchenTools().add(knife);
//        steakrecipe.getKitchenTools().add(pan);
//
//        recipeService.saveRecipe(steakrecipe, user.getUsername());
//        user.getRecipesCreated().add(steakrecipe);
//        //userDomainService.saveUser(user);
//
//        //French Toast Recipe
//        //INGREDIENTS:
//        //1.4 EGGS
//        //2.2/3 CUP MILK
//        //3.2 TEASPOONS CINNAMON
//        //4.8 BREAD SLICES
//        //5.BUTTER
//        //6. MAPLE SYRUP
//        //TOOLS:
//        //1.BOWL
//        //2.WHISK
//        //3.PAN
//        //INSTRUCTIONS:
//        //1.POUR MILK INTO A BOWL
//        //2.ADD 4 EGGS TO THE BOWL
//        //3.ADD 2 TEASPOONS OF CINNAMON
//        //4.WHISK TOGETHER THE EGGS, MILK, AND CINNAMON
//        //5.SOAK EACH BREAD SLICE INTO THE MIXTURE
//        //6.ADD BUTTER TO A PAN
//        //7.FRY THE SOAKED SLICES On THE PAN
//        Subprocedure pourMilk = new Subprocedure();
//        pourMilk.setProcedureName("pour");
//        pourMilk.setInstructions("Pour 2/3 cup of milk into a bowl");
//        Subprocedure addEggs = new Subprocedure();
//        addEggs.setProcedureName("add");
//        addEggs.setInstructions("Add 4 eggs");
//        Subprocedure addCinnamon = new Subprocedure();
//        addCinnamon.setProcedureName("add");
//        addCinnamon.setInstructions("Add 2 teaspoons of cinnamon");
//        Subprocedure whiskMilk = new Subprocedure();
//        whiskMilk.setProcedureName("whisk");
//        whiskMilk.setInstructions("Whisk together the eggs, milk, and cinnamon");
//        Subprocedure addButter = new Subprocedure();
//        addButter.setProcedureName("add");
//        addButter.setInstructions("Add butter to a pan");
//        Subprocedure frySlices = new Subprocedure();
//        frySlices.setProcedureName("fry");
//        frySlices.setInstructions("Fry the slices");
////        chopCarrot.setGame(new GameApplication());
//
//        /* ----------------- Fried Flank Steak RECIPE ----------------------*/
//        Recipe frenchToastrecipe = new Recipe();
//        frenchToastrecipe.setCreator(user.getUsername());
//        frenchToastrecipe.setRecipeName("French Toast Recipe");
//        frenchToastrecipe.getSubprocedureList().add(pourMilk);
//        frenchToastrecipe.getSubprocedureList().add(addEggs);
//        frenchToastrecipe.getSubprocedureList().add(addCinnamon);
//        frenchToastrecipe.getSubprocedureList().add(whiskMilk);
//        frenchToastrecipe.getSubprocedureList().add(addButter);
//        frenchToastrecipe.getSubprocedureList().add(frySlices);
//
//        frenchToastrecipe.setIsPublished(true);
//        frenchToastrecipe.getIngredients().add(egg);
//        frenchToastrecipe.getIngredients().add(milk);
//        frenchToastrecipe.getIngredients().add(cinnamon);
//        frenchToastrecipe.getIngredients().add(breadSlice);
//        frenchToastrecipe.getIngredients().add(butter);
//        frenchToastrecipe.getIngredients().add(mapleSyrup);
//
//        frenchToastrecipe.getKitchenTools().add(mixingBowl);
//        frenchToastrecipe.getKitchenTools().add(pan);
//        System.out.println(user.getUsername());
//        recipeService.saveRecipe(frenchToastrecipe, user.getUsername());
//        user.getRecipesCreated().add(frenchToastrecipe);
//        //userDomainService.saveUser(user);
//
//        //CHICKEN PICCATA
//        //INGREDIENTS
//        //1. 2 CHICKEN BREASTS
//        //2. 1/2 CUP FLOUR
//        //3. 1/4 TEASPOON SALT
//        //4. 1 PINCH PEPPER
//        //5. 3 tablespoons grated Parmesan cheese
//        //6. 4 tablespoons extra virgin olive oil
//        //7. 4 tablespoons butter
//        //8. 1/2 CUP WHITE WINE
//        //9. 2 tablespoons lemon juice
//        //10. 1/4 cup brined capers
//        //11. 2 tablespoons chopped parsley
//        //TOOLS
//        //1. PAN
//        //2. KNIFE
//        //3. plate
//
//        Subprocedure sliceBreast = new Subprocedure();
//        sliceBreast.setProcedureName("slice");
//        sliceBreast.setInstructions("Slice 2 chicken breasts");
//        Subprocedure addFlour = new Subprocedure();
//        addFlour.setProcedureName("add");
//        addFlour.setInstructions("Add 1/2 cup of flour to a plate");
//        Subprocedure addCheese = new Subprocedure();
//        addCheese.setProcedureName("add");
//        addCheese.setInstructions("Add 3 tablespoons of Parmesan cheese to the plate");
//        Subprocedure add_salt = new Subprocedure();
//        add_salt.setProcedureName("add");
//        add_salt.setInstructions("Add 1/4 teaspoon of salt to the plate");
//        Subprocedure add_pepper = new Subprocedure();
//        add_pepper.setProcedureName("add");
//        add_pepper.setInstructions("Add 1 pinch of pepper to the plate");
//        Subprocedure mix_all = new Subprocedure();
//        mix_all.setProcedureName("mix");
//        mix_all.setInstructions("Mix together the flour, Parmesan cheese, salt and pepper");
//        Subprocedure dredge = new Subprocedure();
//        dredge.setProcedureName("dredge");
//        dredge.setInstructions("Dredge the breasts thoroughly in the flour mixture");
//        Subprocedure add_evoil = new Subprocedure();
//        add_evoil.setProcedureName("add");
//        add_evoil.setInstructions("Add 4 tablespoons of extra virgin olive oil to a pan");
//        Subprocedure add_butter = new Subprocedure();
//        add_butter.setProcedureName("add");
//        add_butter.setInstructions("Add 2 tablespoons of butter to the pan");
//        Subprocedure add_seasoned_breast = new Subprocedure();
//        add_seasoned_breast.setProcedureName("add");
//        add_seasoned_breast.setInstructions("Add the dredged breasts to the pan");
//        Subprocedure fry_breasts = new Subprocedure();
//        fry_breasts.setProcedureName("fry");
//        fry_breasts.setInstructions("Fry the breasts");
//        Subprocedure add_wine = new Subprocedure();
//        add_wine.setProcedureName("add");
//        add_wine.setInstructions("Add 1/2 cup of wine to the pan");
//        Subprocedure add_juice = new Subprocedure();
//        add_juice.setProcedureName("add");
//        add_juice.setInstructions("Add 2 tablespoons of lemon juice to the pan");
//        Subprocedure fry_breasts2 = new Subprocedure();
//        fry_breasts2.setProcedureName("fry");
//        fry_breasts2.setInstructions("Finish frying the breasts");
//        Subprocedure add_parsley = new Subprocedure();
//        add_parsley.setProcedureName("add");
//        add_parsley.setInstructions("Sprinkle with 2 tablespoons of chopped parsley");
//
//        Recipe chickenbreastrecipe =new Recipe();
//        chickenbreastrecipe.setCreator(user.getUsername());
//        chickenbreastrecipe.setRating(5.0);
//        chickenbreastrecipe.setRecipeName("Chicken Piccata");
//        chickenbreastrecipe.getSubprocedureList().add(sliceBreast);
//        chickenbreastrecipe.getSubprocedureList().add(addFlour);
//        chickenbreastrecipe.getSubprocedureList().add(addCheese);
//        chickenbreastrecipe.getSubprocedureList().add(add_salt);
//        chickenbreastrecipe.getSubprocedureList().add(add_pepper);
//        chickenbreastrecipe.getSubprocedureList().add(mix_all);
//        chickenbreastrecipe.getSubprocedureList().add(dredge);
//        chickenbreastrecipe.getSubprocedureList().add(add_evoil);
//        chickenbreastrecipe.getSubprocedureList().add(add_butter);
//        chickenbreastrecipe.getSubprocedureList().add(add_seasoned_breast);
//        chickenbreastrecipe.getSubprocedureList().add(fry_breasts);
//        chickenbreastrecipe.getSubprocedureList().add(add_wine);
//        chickenbreastrecipe.getSubprocedureList().add(add_juice);
//        chickenbreastrecipe.getSubprocedureList().add(fry_breasts2);
//        chickenbreastrecipe.getSubprocedureList().add(add_parsley);
//        chickenbreastrecipe.setIsPublished(true);
//        chickenbreastrecipe.getIngredients().add(chickenbreast);
//        chickenbreastrecipe.getIngredients().add(pepper);
//        chickenbreastrecipe.getIngredients().add(salt);
//        chickenbreastrecipe.getIngredients().add(extravirginoil);
//        chickenbreastrecipe.getIngredients().add(flour);
//        chickenbreastrecipe.getIngredients().add(lemonjuice);
//        chickenbreastrecipe.getIngredients().add(whitewine);
//        chickenbreastrecipe.getIngredients().add(parmesancheese);
//        chickenbreastrecipe.getIngredients().add(butter);
//        chickenbreastrecipe.getIngredients().add(choppedparsley);
//        chickenbreastrecipe.getKitchenTools().add(knife);
//        chickenbreastrecipe.getKitchenTools().add(plate);
//        chickenbreastrecipe.getKitchenTools().add(pan);
//
//        recipeService.saveRecipe(chickenbreastrecipe,user.getUsername());
//        user.getRecipesCreated().add(chickenbreastrecipe);
//        //userDomainService.saveUser(user);
//
//        //Caprese Pasta Salad Recipe
//        //INGREDIENTS
//        //1. 1/2 pound small shaped pasta
//        //2. 1 tablespoon salt
//        //3. 1/3 cup olive oil
//        //4. 3 garlic cloves
//        //5. 1 teaspoon pepper
//        //6. 3 cups cherry tomatoes
//        //7. 8 ounces torn mozzarella
//        //8. 10 large fresh basil leaves
//        //9. water
//
//        //TOOLS
//        //1. pot
//        //2. KNIFE
//        //3. bowl
//        //4. spatula ??? for stirring
//
//        Subprocedure addwater = new Subprocedure();
//        addwater.setProcedureName("add");
//        addwater.setInstructions("Add water to a large pot");
//        Subprocedure boilwater = new Subprocedure();
//        boilwater.setProcedureName("boil");
//        boilwater.setInstructions("Boil the water");
//        Subprocedure addpastasalt = new Subprocedure();
//        addpastasalt.setProcedureName("add");
//        addpastasalt.setInstructions("Add 1 table spoon of salt to the pot");
//        Subprocedure addpasta = new Subprocedure();
//        addpasta.setProcedureName("add");
//        addpasta.setInstructions("Add pasta to the pot");
//        Subprocedure boilpasta = new Subprocedure();
//        boilpasta.setProcedureName("boil");
//        boilpasta.setInstructions("Boil the pasta");
//        Subprocedure chopgarlic = new Subprocedure();
//        chopgarlic.setProcedureName("chop");
//        chopgarlic.setInstructions("Chop 3 garlic cloves");
//        Subprocedure sddtobowl = new Subprocedure();
//        sddtobowl.setProcedureName("add");
//        sddtobowl.setInstructions("Add chopped garlic cloves to a bowl");
//        Subprocedure sddtobowl11 = new Subprocedure();
//        sddtobowl11.setProcedureName("add");
//        sddtobowl11.setInstructions("Add  1 teaspoon of pepper to the same bowl");
//        Subprocedure sddtobowl12 = new Subprocedure();
//        sddtobowl12.setProcedureName("add");
//        sddtobowl12.setInstructions("Add  1/3 cup of olive oil to the same bowl");
//        Subprocedure mixall = new Subprocedure();
//        mixall.setProcedureName("mix");
//        mixall.setInstructions("Mix together the oil, minced garlic, pepper, and salt.");
//        Subprocedure sddtobowl122 = new Subprocedure();
//        sddtobowl122.setProcedureName("add");
//        sddtobowl122.setInstructions("Add  the boiled pasta to the bowl the same bowl");
//        Subprocedure mixall2 = new Subprocedure();
//        mixall2.setProcedureName("mix");
//        mixall2.setInstructions("Mix together the pasta, oil, minced garlic, pepper, and salt.");
//        Subprocedure chopmoz = new Subprocedure();
//        chopmoz.setProcedureName("chop");
//        chopmoz.setInstructions("Chop 8 ounces of mozzarella");
//        Subprocedure addmoz = new Subprocedure();
//        addmoz.setProcedureName("add");
//        addmoz.setInstructions("Add the chopped mozzarella to the pasta");
//        Subprocedure slicetom = new Subprocedure();
//        slicetom.setProcedureName("slice");
//        slicetom.setInstructions("Slice 3 cups of cherry tomatoes");
//        Subprocedure addtom = new Subprocedure();
//        addtom.setProcedureName("add");
//        addtom.setInstructions("Add sliced tomatoes to the pasta");
//        Subprocedure chopb = new Subprocedure();
//        chopb.setProcedureName("chop");
//        chopb.setInstructions("Chop 10 basil leaves");
//        Subprocedure addbas = new Subprocedure();
//        addbas.setProcedureName("add");
//        addbas.setInstructions("Add chopped basil leaves to the pasta");
//
//        Recipe capreserecipe =new Recipe();
//        capreserecipe.setCreator(user.getUsername());
//        capreserecipe.setRecipeName("Caprese Pasta Salad");
//        capreserecipe.getSubprocedureList().add(addwater);
//        capreserecipe.getSubprocedureList().add(addpastasalt);
//        capreserecipe.getSubprocedureList().add(addpasta);
//        capreserecipe.getSubprocedureList().add(boilpasta);
//        capreserecipe.getSubprocedureList().add(chopgarlic);
//        capreserecipe.getSubprocedureList().add(sddtobowl);
//        capreserecipe.getSubprocedureList().add(sddtobowl11);
//        capreserecipe.getSubprocedureList().add(sddtobowl12);
//        capreserecipe.getSubprocedureList().add(mixall);
//        capreserecipe.getSubprocedureList().add(sddtobowl122);
//        capreserecipe.getSubprocedureList().add(mixall2);
//        capreserecipe.getSubprocedureList().add(chopmoz);
//        capreserecipe.getSubprocedureList().add(addmoz);
//        capreserecipe.getSubprocedureList().add(slicetom);
//        capreserecipe.getSubprocedureList().add(addtom);
//        capreserecipe.getSubprocedureList().add(chopb);
//        capreserecipe.getSubprocedureList().add(addbas);
//
//        capreserecipe.setIsPublished(true);
//        capreserecipe.getIngredients().add(smallpasta);
//        capreserecipe.getIngredients().add(salt);
//        capreserecipe.getIngredients().add(extravirginoil);
//        capreserecipe.getIngredients().add(garlicclove);
//        capreserecipe.getIngredients().add(pepper);
//        capreserecipe.getIngredients().add(cherrytomato);
//        capreserecipe.getIngredients().add(mozzarella);
//        capreserecipe.getIngredients().add(basil);
//        capreserecipe.getIngredients().add(water);
//
//
//        capreserecipe.getKitchenTools().add(mixingBowl);
//        capreserecipe.getKitchenTools().add(knife);
//        capreserecipe.getKitchenTools().add(pot);
//
//
//
//        recipeService.saveRecipe(capreserecipe,user.getUsername());
//        user.getRecipesCreated().add(capreserecipe);
//        userDomainService.saveUser(user);
//
//
//        Subprocedure addwater2 = new Subprocedure();
//        addwater2.setProcedureName("add");
//        addwater2.setInstructions("Add water to a large pot");
//        Subprocedure boilwater2 = new Subprocedure();
//        boilwater2.setProcedureName("boil");
//        boilwater2.setInstructions("Boil the water");
//        Subprocedure addpsalt2 = new Subprocedure();
//        addpsalt2.setProcedureName("add");
//        addpsalt2.setInstructions("Add 1 table spoon of salt to the pot");
//        Subprocedure peelpotato = new Subprocedure();
//        peelpotato.setProcedureName("peel");
//        peelpotato.setInstructions("Peel 2 pounds of potatoes");
//        Subprocedure cutpotato = new Subprocedure();
//        cutpotato.setProcedureName("chop");
//        cutpotato.setInstructions("Chop the peeled potatoes");
//        Subprocedure addpotato = new Subprocedure();
//        addpotato.setProcedureName("boil");
//        addpotato.setInstructions("Boil chopped potatoes");
//        Subprocedure drainpotato = new Subprocedure();
//        drainpotato.setProcedureName("drain");
//        drainpotato.setInstructions("Drain boiled potatoes");
//        Subprocedure addpotato2 = new Subprocedure();
//        addpotato2.setProcedureName("add");
//        addpotato2.setInstructions("Put drained potatoes to a bowl");
//        Subprocedure addbtr = new Subprocedure();
//        addbtr.setProcedureName("add");
//        addbtr.setInstructions("Add 2 table spoons of butter to the bowl");
//        Subprocedure addmlk = new Subprocedure();
//        addmlk.setProcedureName("add");
//        addmlk.setInstructions("Add 1 cup of milk to the bowl");
//        Subprocedure mashp = new Subprocedure();
//        mashp.setProcedureName("mash");
//        mashp.setInstructions("Mash the potatoes");
//        Subprocedure addppp = new Subprocedure();
//        addppp.setProcedureName("add");
//        addppp.setInstructions("Add pepper to taste");
//
//        Recipe mashedpotatoes = new Recipe();
//        mashedpotatoes.setCreator(user.getUsername());
//        mashedpotatoes.setRecipeName("Mashed Potatoes");
//        mashedpotatoes.getSubprocedureList().add(addwater2);
//        mashedpotatoes.getSubprocedureList().add(boilwater2);
//        mashedpotatoes.getSubprocedureList().add(addpsalt2);
//        mashedpotatoes.getSubprocedureList().add(peelpotato);
//        mashedpotatoes.getSubprocedureList().add(cutpotato);
//        mashedpotatoes.getSubprocedureList().add(addpotato);
//        mashedpotatoes.getSubprocedureList().add(drainpotato);
//        mashedpotatoes.getSubprocedureList().add(addpotato2);
//        mashedpotatoes.getSubprocedureList().add(addbtr);
//        mashedpotatoes.getSubprocedureList().add(addmlk);
//        mashedpotatoes.getSubprocedureList().add(mashp);
//
//        mashedpotatoes.setIsPublished(true);
//        mashedpotatoes.getIngredients().add(potato);
//        mashedpotatoes.getIngredients().add(salt);
//        mashedpotatoes.getIngredients().add(water);
//        mashedpotatoes.getIngredients().add(butter);
//        mashedpotatoes.getIngredients().add(pepper);
//        mashedpotatoes.getIngredients().add(milk);
//
//
//        mashedpotatoes.getKitchenTools().add(mixingBowl);
//        mashedpotatoes.getKitchenTools().add(knife);
//        mashedpotatoes.getKitchenTools().add(pot);
//        mashedpotatoes.getKitchenTools().add(potatomasher);
//
//        recipeService.saveRecipe(mashedpotatoes,user.getUsername());
//        user.getRecipesCreated().add(mashedpotatoes);
//        userDomainService.saveUser(user);
//
//
//
//
//
//
//
//
//
//        /////////////////////////////////////////////////////////////////////////////
//        //Adding Game
//        Game game1 = new Game();
//        game1.setRecipe(steakrecipe);
//        game1.setGameState(new GameState());
//        gameRepository.save(game1);
//        //System.out.println(game1.getId());
//        game1.getGameState().setRecipeId(steakrecipe.getId());
//        game1.getGameState().setGameId(game1.getId());
//        //After each subprocedure do we create a new intermediateIngredient?
//        IntermediateIngredient ii1 = new IntermediateIngredient();
//        //we cut steak, so we add steak to the list of ingredients?
//        ii1.getIngredients().add(steak);
//        IntermediateIngredient ii2 = new IntermediateIngredient();
//        ii2.getIngredients().add(salt);
//        //adding previous intermediateIngredient?
//        ii2.getIntermediateIngredients().add(ii1);
//
//        game1.getGameState().getIntermediateIngredients().add(ii1);
//        game1.getGameState().getIntermediateIngredients().add(ii2);
//        game1.setScore(250.0);
//        game1.setUserRating(3.0);
//        //saving uncomplited game with two finished steps
//        gameRepository.save(game1);
//
//        //Adding Game
//        Game game2 = new Game();
//        game2.setRecipe(steakrecipe);
//        game2.setGameState(new GameState());
//        gameRepository.save(game2);
//        System.out.println(game2.getId());
//        game2.getGameState().setRecipeId(steakrecipe.getId());
//        game2.getGameState().setGameId(game2.getId());
//        //After each subprocedure do we create a new intermediateIngredient?
//        //cut steak
//        IntermediateIngredient ii12 = new IntermediateIngredient();
//        //we cut steak, so we add steak to the list of ingredients?
//        ii12.getIngredients().add(steak);
//        //apply salt
//        IntermediateIngredient ii22 = new IntermediateIngredient();
//        ii22.getIngredients().add(salt);
//        //adding previous intermediateIngredient?
//        ii22.getIntermediateIngredients().add(ii12);
//        //apply pepper
//        //ii22 = salt + pepper
//        //ii32 = ii22 + pepper
//        IntermediateIngredient ii32 = new IntermediateIngredient();
//        ii32.getIntermediateIngredients().add(ii22);
//        ii32.getIngredients().add(pepper);
//        //apply dry mustard
//        IntermediateIngredient ii42 = new IntermediateIngredient();
//        ii42.getIntermediateIngredients().add(ii32);
//        ii42.getIngredients().add(dryMustard);
//        //spread butter
//        IntermediateIngredient ii52 = new IntermediateIngredient();
//        ii52.getIntermediateIngredients().add(ii42);
//        ii52.getIngredients().add(butter);
//        //fry steak
//        //seasoned steak = ii52
//        //no ingredients needed for this step
//        IntermediateIngredient ii62 = new IntermediateIngredient();
//        ii62.getIntermediateIngredients().add(ii52);
//
//        game2.getGameState().getIntermediateIngredients().add(ii12);
//        game2.getGameState().getIntermediateIngredients().add(ii22);
//        game2.getGameState().getIntermediateIngredients().add(ii32);
//        game2.getGameState().getIntermediateIngredients().add(ii42);
//        game2.getGameState().getIntermediateIngredients().add(ii52);
//        game2.getGameState().getIntermediateIngredients().add(ii62);
//        game2.setScore(1050.0);
//        game2.setUserRating(1.0);
//        //saving completed game with All finished steps
//        gameRepository.save(game2);
//
//        Game game3 = new Game();
//        game3.setRecipe(chickenbreastrecipe);
//        game3.setUserRating(5.0);
//        gameRepository.save(game3);
        /* ----------------- TEST REQUEST ----------------------*/
        requestService.storeRequest(user.getUsername(),"feedback",null,"add more actions");
        requestService.storeRequest(user.getUsername(),"feedback",null,"better games");
        requestService.storeRequest(user.getUsername(),"action","mince","use a knife to mince a garlic");
        requestService.storeRequest(user.getUsername(),"action","blend","use blender to blend");
        requestService.storeRequest(user.getUsername(),"ingredient","sugar",null);
        requestService.storeRequest(user.getUsername(),"ingredient","thyme",null);
        requestService.storeRequest(user.getUsername(),"tool","blender","to blend food");
        requestService.storeRequest(user.getUsername(),"tool","oven","to bake");
    }
}