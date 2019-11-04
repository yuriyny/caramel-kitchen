package cse308.caramel.caramelkitchen.game.controller;

import cse308.caramel.caramelkitchen.game.Items;
import cse308.caramel.caramelkitchen.game.Recipe;
import cse308.caramel.caramelkitchen.game.repository.EquipmentRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.game.service.SearchService;
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
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    EquipmentRepository equipmentRepository;
    @Autowired
    SearchService searchService;

    /**
     *  (NEEDS TO BE CHANGED BECAUSE THERE ARE TWO RECIPE but the other wasn't implemented yet)
     * Needed when you go on home page
     * @return list of recipes in database
     */
    @ResponseBody
    @GetMapping(path={"/recipe-list"})
    public List<Recipe> getRecipesList(){
         return recipeRepository.findAll();              //USE THIS TO GET A LIST OF RECIPES
    }
    /**
     *  Needed when you click on create lab
     * @return list of ingredient/tool in database
     */
    @ResponseBody
    @GetMapping(path={"/ingredient-tool-list"})
    public List<Items> getIngredientToolList(){
        List<Items>returnList=new ArrayList<>();
        returnList.addAll(equipmentRepository.findAll());
        returnList.addAll(ingredientRepository.findAll());
        equipmentRepository.findAll().forEach(e->System.out.println(e.getName()));
        java.util.Collections.sort(returnList);
        return returnList;
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
    public List<Items> searchIngredientTool(@RequestBody String search){
        List<Items> returnList=new ArrayList<>();
        returnList.addAll(searchService.getIngredients(search));
        returnList.addAll(searchService.getKitchenTools(search));
        return returnList;
    }

    public void updateRecipeHistory(Recipe recipe){

    }
    public void createRecipe(Recipe recipe){

    }
    public void modifyRecipe(Recipe recipe){

    }
    public Recipe getRecipeContent(String s){
        return new Recipe();
    }
}
