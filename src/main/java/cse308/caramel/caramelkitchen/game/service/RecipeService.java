package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    KitchenToolRepository kitchenToolRepository;
    @Autowired
    UserDomainService userDomainService;
    @Autowired
    S3Services s3Services;

//    public List<SubprocedureComponent> findAllEquipmentTool(){
//        List<SubprocedureComponent> returnList=new ArrayList<>();
//        returnList.addAll(kitchenToolRepository.findAll());
//        returnList.addAll(ingredientRepository.findAll());
//        returnList=sortList(returnList);
//        returnList=findImage(returnList);
//        return returnList;
//    }
    public void deleteRecipe(Recipe recipe){
        recipeRepository.delete(recipe);
    }
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

    public List<String> findAllToolActions(String toolId){
        return (List) kitchenToolRepository.findByName(toolId).get().getActions();
    }

    public List<Recipe> findAllPublishedRecipe(){
        return (List<Recipe>)recipeRepository.findAllPublishedRecipes();
    }
    public Recipe findRecipe(String id){
        return recipeRepository.findById(id).get();
    }
    public Recipe saveRecipe(Recipe recipe, String userName){
        if(recipe.getCreator()==null){
            recipe.setCreator(userName);
        }
        recipeRepository.save(recipe);
        return recipe;
    }
    public List<SubprocedureComponent> sortList(List<SubprocedureComponent> list){
        list.sort(Comparator.comparing(a->a.getName()));
        return list;
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
}
