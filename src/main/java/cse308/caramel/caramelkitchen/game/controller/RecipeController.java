package cse308.caramel.caramelkitchen.game.controller;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.persistence.SubprocedureComponent;
import cse308.caramel.caramelkitchen.game.repository.EquipmentRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RecipeController {

    @Autowired
    SearchService searchService;
    @Autowired
    RecipeService recipeService;

    /**
     *  (NEEDS TO BE CHANGED BECAUSE THERE ARE TWO RECIPE but the other wasn't implemented yet)
     * Needed when you go on home page
     * @return list of recipes in database
     */
    @ResponseBody
    @GetMapping(path={"/recipe-list"})
    public List<Recipe> getRecipesList(){
        return recipeService.findAllRecipe();
    }
    /**
     *  Needed when you click on create lab
     * @return list of ingredient/tool in database
     */
    @ResponseBody
    @GetMapping(path={"/ingredient-tool-list"})
    public List<SubprocedureComponent> getIngredientToolList(){
        return recipeService.findAllEquipmentTool();
    }
    /**
     * Search recipe
     * @return list of recipe matched in database
     */
    @ResponseBody
    @PostMapping(path={"/search-recipe-list"})
    public List<Recipe> searchRecipe(@RequestBody String search){
        return searchService.getRecipes(search);
    }

    /**
     * Search ingredient/tool
     * @return list of ingredient/tool matched in database
     */
    @ResponseBody
    @PostMapping(path={"/search-ingredient-tool-list"})
    public List<SubprocedureComponent> searchIngredientTool(@RequestBody String search){
        List<SubprocedureComponent> returnList=new ArrayList<>();
        returnList.addAll(searchService.getIngredients(search));
        returnList.addAll(searchService.getKitchenTools(search));
        return returnList;
    }

    public void updateRecipeHistory(Recipe recipe){

    }

    /**
     * This method takes in a recipe and saves it. Works for save and publish.
     * @param recipe
     */
    public void createRecipe(Recipe recipe){
        recipeService.saveRecipe(recipe);

    }
    public void modifyRecipe(Recipe recipe){

    }
    public Recipe getRecipeContent(String s){
        return new Recipe();
    }
}
