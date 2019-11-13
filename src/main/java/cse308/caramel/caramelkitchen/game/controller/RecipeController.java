package cse308.caramel.caramelkitchen.game.controller;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.persistence.SubprocedureComponent;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.search.service.SearchService;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @Autowired
    UserDomainService userDomainService;

    /**
     *  Needed when you click on create lab
     * @return list of ingredient/tool in database
     */
    public  Map<String, List<SubprocedureComponent>> getIngredientToolList(){
        List<SubprocedureComponent> ingredients=new ArrayList<>();
        ingredients.addAll(recipeService.findAllIngredients());
        ingredients=recipeService.findImage(ingredients);
        List<SubprocedureComponent> tools=new ArrayList<>();
        tools.addAll(recipeService.findAllTools());
        tools=recipeService.findImage(tools);

        Map<String, List<SubprocedureComponent>> returnObj = new HashMap<>();
        returnObj.put("ingredients", ingredients);
        returnObj.put("tools", tools);
        return returnObj;
    }
    /**
     * Search recipe
     * @return list of recipe matched in database
     */
    @ResponseBody
    @PostMapping(path={"/search-recipe-list"})
    public List<Recipe> searchRecipe(@RequestBody String search){
        if(search.isEmpty()){
            return recipeService.findAllRecipe();
        }
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
        List<SubprocedureComponent> tools=new ArrayList<>();

        if(search.isEmpty()){
            ingredients.addAll(recipeService.findAllIngredients());
            tools.addAll(recipeService.findAllTools());
        }else {
            ingredients.addAll(searchService.getIngredients(search));
            tools.addAll(searchService.getKitchenTools(search));
        }
        ingredients=recipeService.findImage(ingredients);
        tools=recipeService.findImage(tools);

        Map<String, List<SubprocedureComponent>> returnObj = new HashMap<>();
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
    public void createRecipe(@RequestBody Recipe recipe,Principal principal){
        recipeService.saveRecipe(recipe,principal.getName());
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

