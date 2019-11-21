package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.SubprocedureComponent;
import cse308.caramel.caramelkitchen.game.persistence.Whitelist;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;

import java.util.ArrayList;
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
    public void saveIngredient(Ingredient ingredient){
        ingredientRepository.save(ingredient);
    }

    public List<String> retrieveValidToolActions(String ingredientName, String toolName) {                                  //does not handle if ingredient doesn't exist in whitelist.
        Whitelist ingredientWL=whitelistRepository.findById(ingredientName).get();                                          // ID is the name, might need to change
        KitchenTool tool = kitchenToolRepository.findByName(toolName).get();
        List<String> allActions= (List) tool.getActions();
        List<String>list=allActions.stream().filter(action->ingredientWL.getActions().contains(action)).collect(Collectors.toList());
        return list;
    }
}
