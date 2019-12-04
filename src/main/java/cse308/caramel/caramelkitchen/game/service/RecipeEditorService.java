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
    public List<String> findAllActions(){
        List<KitchenTool>tools=findAllTools();
        return tools.stream()
                .map(obj->findAllToolActions(obj.getName()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
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

    public void saveIngredient(Ingredient ingredient){
        ingredientRepository.save(ingredient);
    }

    public List<String> retrieveValidToolActions(List<String> ingredientIds,List<String> toolIds,  List<IntermediateIngredient> intermediates) {

        List<Ingredient>ingredients=new ArrayList<>();
        ingredientIds.stream().forEach(obj->{
            ingredients.add(ingredientRepository.findByName(obj).get());
        });

        List<KitchenTool>tools=new ArrayList<>();
        toolIds.stream().forEach(obj->tools.add(kitchenToolRepository.findByName(obj).get()));

        //if size of ingredients ==1 and size of intermediates==0 and size of tools==1 check whitelist
        if(ingredients.size()==1 && intermediates.size()==0 && tools.size()==1){
                Whitelist ingredientWL=whitelistRepository.findById(ingredients.get(0).getName()).get();                                          // ID is the name, might need to change
                KitchenTool tool = kitchenToolRepository.findByName(tools.get(0).getName()).get();
                List<String> allActions= (List) tool.getActions();
                return allActions.stream().filter(action->ingredientWL.getActions().contains(action)).collect(Collectors.toList());
        }
        //if has liquid or spice ingredient somewhere and bowl is available, then marinate
        else if(ingredients.size()+intermediates.size()>1 && tools.size()==1 && tools.stream().allMatch(obj->obj.getName().equals("bowl"))){
            //if previous action was mix and liquid/spice was present in that mix, then allow marinate
            if(intermediates.size()>0 && intermediates.stream().anyMatch(obj-> obj.getTag().equals("mix") && (obj.getIngredients().stream().anyMatch(ingredient -> ingredient.getType().equals("liquid"))||obj.getIngredients().stream().anyMatch(ingredient -> ingredient.getType().equals("spice"))))){
                return new ArrayList<>(Collections.singleton("mix"));
            }
            //if current list has a liquid or spice
            if(ingredients.stream().anyMatch(obj->obj.getType().equals("liquid"))|| ingredients.stream().anyMatch(obj->obj.getType().equals("spice"))){
                return new ArrayList<>(Collections.singleton("mix"));
            }
        }
        //if list of ingredients+intermediate size >1, and bowl and spoon is there, then  add mix
        else if(ingredients.size()+intermediates.size()>1 && tools.size()==2 && tools.stream().anyMatch(obj->obj.getName().equals("bowl")) && tools.stream().anyMatch(obj->obj.getName().equals("spoon"))){
            return new ArrayList<>(Collections.singleton("mix"));
        }
        //if spice is in the ingredient and the other ingredient isn't spice, have option to add spice
        else if(ingredients.size()+intermediates.size()==2 && ingredients.stream().anyMatch(obj->obj.getType().equals("spice"))&&!ingredients.stream().allMatch(obj->obj.getType().equals("spice"))){
            List<Ingredient> i=ingredients.stream().filter(obj->obj.getType().equals("spice")).collect(Collectors.toList());
            return new ArrayList<>(Collections.singleton("add"+i.get(0)));
        }
        //if pan or pot and exists liquid, then boil is an option
        else if((tools.stream().anyMatch(tool->tool.getName().equals("pan")) || tools.stream().anyMatch(tool->tool.getName().equals("pot"))) && ingredients.stream().anyMatch(obj->obj.getType().equals("liquid"))){
            return new ArrayList<>(Collections.singleton("boil"));
        }
        //if pan and spatula and exists non liquid and non spice ingredient and oil exists, then saute is an option (NOT FINISHED)
        else if(tools.stream().anyMatch(tool->tool.getName().equals("pan")) && tools.stream().anyMatch(tool->tool.getName().equals("spatula")) && ingredients.stream().anyMatch(ingredient -> ingredient.getType().equals("oil")) ){
            return new ArrayList<>(Collections.singleton("boil"));
        }
        return new ArrayList<>();

        //if you add ingredient to item being cooked, combine in front end (might not handle, just add as comment)
        //if no ingredient, only one intermediate ingredient and action was some cook action, then add stir (might not handle, just add as comment)
    }
}
