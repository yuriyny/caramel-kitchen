package cse308.caramel.caramelkitchen.game.controller;

import com.amazonaws.services.s3.model.JSONOutput;
import com.fasterxml.jackson.databind.util.JSONPObject;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.persistence.SubprocedureComponent;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RecipeController {

    @Autowired
    SearchService searchService;
    @Autowired
    RecipeService recipeService;

    /**
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
    public Map<String, List<SubprocedureComponent>> searchIngredientTool(@RequestBody String search){
        List<SubprocedureComponent> ingredients=new ArrayList<>();
        ingredients.addAll(searchService.getIngredients(search));
        ingredients=recipeService.findImage(ingredients);
        List<SubprocedureComponent> tools=new ArrayList<>();
        tools.addAll(searchService.getKitchenTools(search));
        tools=recipeService.findImage(tools);

        Map<String, List<SubprocedureComponent>> returnObj = new HashMap<String, List<SubprocedureComponent>>();
        returnObj.put("ingredients", ingredients);
        returnObj.put("tools", tools);
        return returnObj;
    }

    public void updateRecipeHistory(Recipe recipe){

    }

    /**
     * This method takes in a recipe and saves it. Works for save and publish.
     * @param recipe
     */
    @ResponseBody
    @PostMapping(path={"/create-recipe", "/update-recipe"})
    public void createRecipe(Recipe recipe){
        recipeService.saveRecipe(recipe);
    }
    public void modifyRecipe(Recipe recipe){

    }
    public Recipe getRecipeContent(String s){
        return new Recipe();
    }

    /**
     * Returns all actions associated with a tool
     * GET
     */
    @ResponseBody
    @GetMapping(value = "/tool/{id}")
    public List<String> getToolActions(@PathVariable String id) {
        return recipeService.findAllToolActions(id);
    }

}

