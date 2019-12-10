package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.model.Rating;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;


@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserDomainService userDomainService;
    @Autowired
    RecipeEditorService recipeEditorService;

    public void deleteRecipe(Recipe recipe){
        recipeRepository.delete(recipe);
    }

    public List<Recipe> findAllPublishedRecipe(){
        return (List<Recipe>)recipeRepository.findAllPublishedRecipes();
    }
    public Recipe findRecipe(String id){//add images along with retrieving recipe
        Recipe recipe = recipeRepository.findById(id).orElse(null);
        recipe.setIngredients(recipeEditorService.findImageIngredient((List<Ingredient>)recipe.getIngredients()));
        recipe.setKitchenTools(recipeEditorService.findImageTool((List<KitchenTool>)recipe.getKitchenTools()));
        recipe.setIntermediateIngredients(recipeEditorService.findImageIntermediateIngredient((List< IntermediateIngredient >)recipe.getIntermediateIngredients()));
        return recipe;
    }
    public Recipe saveRecipe(Recipe recipe, String username){
        if(recipe.getCreator()==null){//for testing with dbseeder
            recipe.setCreator(username);
        }
        recipeRepository.save(recipe);
        return recipe;
    }
    public List<SubprocedureComponent> sortList(List<SubprocedureComponent> list){
        list.sort(Comparator.comparing(a->a.getName()));
        return list;
    }

}
