package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeEditorService {

    @Autowired
    WhitelistRepository whitelistRepository;
    @Autowired
    KitchenToolRepository kitchenToolRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    S3Services s3Services;

    public List<Ingredient> findAllIngredients(){
        List<Ingredient> returnList=new ArrayList<>();
        returnList.addAll(ingredientRepository.findAll());
        return returnList;
    }
    public List<KitchenTool> findAllTools(){
        List<KitchenTool> returnList=new ArrayList<>();
        returnList.addAll(kitchenToolRepository.findAll());
        return returnList;
    }

    public List<String> findAllToolActions(String toolName){
        return (List) kitchenToolRepository.findByName(toolName).get().getActions();
    }
    public List<String> findAllIngredientActions(String ingredient){
        Ingredient i=ingredientRepository.findByName(ingredient).get();
        return (List)whitelistRepository.findById(i.getName()).get().getActions();
    }
    public List<String> findAllPossibleIngredientActions(){
        List<Ingredient>ingredient=findAllIngredients();
        return ingredient.stream()
                .map(obj->findAllIngredientActions(obj.getName()))
                .flatMap(List::stream).distinct()
                .collect(Collectors.toList());
    }
    public List<String> findAllActions(){
        List<KitchenTool>tools=findAllTools();
        return tools.stream()
                .map(obj->findAllToolActions(obj.getName()))
                .flatMap(List::stream).distinct()
                .collect(Collectors.toList());
    }
    public List<String>findAllTypes(){
        return findAllIngredients().stream().map(obj->obj.getType()).distinct().collect(Collectors.toList());
    }
    public List<SubprocedureComponent> findImage(List<SubprocedureComponent> list){
        for (SubprocedureComponent subprocedureComponent : list){
            if(subprocedureComponent.getImageName()!=null){
                String URL=s3Services.getImageUrl(subprocedureComponent.getImageName());
                subprocedureComponent.setImageFileUrl(URL);
            }
        }
        return list;
    }

    public List<KitchenTool> findImageTool(List<KitchenTool> list){
        for (KitchenTool kitchenTool : list){
            if(kitchenTool.getImageName()!=null){
                String URL=s3Services.getImageUrl(kitchenTool.getImageName());
                kitchenTool.setImageFileUrl(URL);
            }
        }
        return list;
    }
    public List<Ingredient> findImageIngredient(List<Ingredient> list){
        for (Ingredient ingredient : list){
            if(ingredient.getImageName()!=null){
                String URL=s3Services.getImageUrl(ingredient.getImageName());
                ingredient.setImageFileUrl(URL);
            }
        }
        return list;
    }
    public List<IntermediateIngredient> findImageIntermediateIngredient(List<IntermediateIngredient> list){
        for (IntermediateIngredient intermediateIngredient : list){
            if(intermediateIngredient.getImageName()!=null){
                String URL=s3Services.getImageUrl(intermediateIngredient.getImageName());
                intermediateIngredient.setImageFileUrl(URL);
            }
        }
        return list;
    }

    public String findRecipeImageUrl(String recipeImage) {
        return s3Services.getImageUrl(recipeImage);
    }

    public void saveIngredient(Ingredient ingredient){
        ingredientRepository.save(ingredient);
    }

    public List<String> retrieveValidToolActions(List<String> ingredients,List<String> tools,  List<IntermediateIngredient> intermediates) {

        List<Ingredient>ingredientObj=new ArrayList<>();
        ingredients.stream().forEach(obj->ingredientObj.add(ingredientRepository.findByName(obj).get()));
        List<KitchenTool>toolObj=new ArrayList<>();
        tools.stream().forEach(obj->toolObj.add(kitchenToolRepository.findByName(obj).get()));
        List<String>base=new ArrayList<>();
        base.add("spice");
        base.add("liquid");

//        System.out.println("tool size"+toolObj.size());
//        System.out.println("bowl?"+toolObj.stream().anyMatch(obj->obj.getName().equals("bowl")));
//        System.out.println("spoon"+tools.stream().anyMatch("mixing spoon"::contains));

        //if size of ingredients ==1 and size of intermediates==0 and size of tools==1 check whitelist
        if(ingredients.size()==1 && intermediates.size()==0 && tools.size()==1){
                Whitelist ingredientWL=whitelistRepository.findById(ingredients.get(0)).get();                                          // ID is the name, might need to change
                KitchenTool tool=toolObj.get(0);
                List<String> allActions= (List) tool.getActions();
                return allActions.stream().filter(action->ingredientWL.getActions().contains(action)).collect(Collectors.toList());
        }
        //if previously peeled, then can chop/slice or whatever matches with knife
        else if(ingredients.size()==0 && intermediates.size()==1 && toolObj.size()==1 && toolObj.stream().allMatch(obj->obj.getName().equals("knife"))&& intermediates.get(0).getTag().equals("peel")){
            Ingredient i=((List<Ingredient>)intermediates.get(0).getIngredients()).get(0);
            Whitelist w=whitelistRepository.findById(i.getName()).get();
            List<String> s=toolObj.get(0).getActions().stream().filter(action->w.getActions().contains(action)).collect(Collectors.toList());
            s.remove("peel");
            return s;
        }
        //if has liquid or spice ingredient and other ingredient and bowl is available, then marinate
        else if(ingredientObj.size()+intermediates.size()>=1 && toolObj.size()==1 && toolObj.stream().anyMatch(obj->obj.getName().equals("bowl"))){
            //if only liquid/spice, no actions
            if(ingredientObj.size()>=1 && intermediates.size()==0 && ingredientObj.stream().allMatch(obj->base.contains(obj.getType()))){
                return new ArrayList<>();
            }
            //if previous action was mix and liquid/spice was present in that mix, then allow marinate
            if(intermediates.size()>0 && intermediates.stream().anyMatch(obj-> obj.getTag().equals("mix") && (obj.getIngredients().stream().anyMatch(ingredient -> ingredient.getType().equals("liquid"))||obj.getIngredients().stream().anyMatch(ingredient -> ingredient.getType().equals("spice"))))){
                return new ArrayList<>(Collections.singleton("marinate"));
            }
            //if current list has a liquid or spice(has a non liquid/spice item)
            if(ingredientObj.size()+intermediates.size()>1 &&(ingredientObj.stream().anyMatch(obj->obj.getType().equals("liquid"))|| ingredientObj.stream().anyMatch(obj->obj.getType().equals("spice")))){
                return new ArrayList<>(Collections.singleton("marinate"));
            }
        }
        //if list of ingredients+intermediate size >1, and bowl and spoon is there, then  add mix
        else if(ingredients.size()+intermediates.size()>1 && tools.size()==2 && toolObj.stream().anyMatch(obj->obj.getName().equals("bowl"))&& toolObj.stream().anyMatch(obj->obj.getName().equals("mixing spoon"))){
            return new ArrayList<>(Collections.singleton("mix"));
        }
        //if spice is in the ingredient and the other ingredient isn't spice, have option to add spice
        else if(ingredientObj.size()+intermediates.size()==2 && ingredientObj.stream().anyMatch(obj->obj.getType().equals("spice"))&&!ingredientObj.stream().allMatch(obj->obj.getType().equals("spice"))){
            List<Ingredient> i=ingredientObj.stream().filter(obj->obj.getType().equals("spice")).collect(Collectors.toList());
            return new ArrayList<>(Collections.singleton("add"+i.get(0).getName()));
        }
        //if pot and liquid, then boil is an option
        else if(tools.size()==1&& toolObj.stream().anyMatch(tool->tool.getName().equals("pot")) && ingredientObj.stream().anyMatch(obj->obj.getType().equals("liquid"))){
            return new ArrayList<>(Collections.singleton("boil"));
        }
        //if pan and spatula and exists non liquid and non spice ingredient and oil exists, then saute is an option
        else if(toolObj.stream().anyMatch(tool->tool.getName().equals("pan")) && toolObj.stream().anyMatch(tool->tool.getName().equals("spatula")) && ingredientObj.stream().anyMatch(ingredient -> ingredient.getType().equals("oil")) && !ingredientObj.stream().allMatch(obj->base.contains(obj.getType()))){
            return new ArrayList<>(Collections.singleton("sauté"));
        }
        return new ArrayList<>();

        //if you add ingredient to item being cooked, combine in front end (might not handle, just add as comment)
        //if no ingredient, only one intermediate ingredient and action was some cook action, then add stir (might not handle, just add as comment)
    }


}
