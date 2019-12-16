package cse308.caramel.caramelkitchen.game.controller;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import cse308.caramel.caramelkitchen.game.model.IngredientToolIntermediate;
import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.model.Rating;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.service.GameService;
import cse308.caramel.caramelkitchen.game.service.RecipeEditorService;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.search.service.SearchService;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class RecipeController {

    @Autowired
    SearchService searchService;
    @Autowired
    RecipeService recipeService;
    @Autowired
    RecipeEditorService recipeEditorService;
    @Autowired
    UserDomainService userDomainService;
    @Autowired
    GameService gameService;

    /**
     * Search recipe
     * @return list of recipe matched in database
     */
    @ResponseBody
    @PostMapping(path={"/search-recipe-list"})
    public List<Recipe> searchRecipe(@RequestBody String search){
        return searchService.getPublishedRecipes(search);
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
        ingredients=recipeEditorService.findImage(ingredients);

        List<SubprocedureComponent> tools=new ArrayList<>();
        tools.addAll(searchService.getKitchenTools(search));
        tools=recipeEditorService.findImage(tools);

        Map<String, List<SubprocedureComponent>> returnObj = new HashMap<>();
        returnObj.put("ingredients", ingredients);
        returnObj.put("tools", tools);
        return returnObj;
    }
    @ResponseBody
    @GetMapping(path={"/ingredient-tool-list"})
    public Map<String, List<SubprocedureComponent>> ingredientToolList(){
        List<SubprocedureComponent> ingredients=new ArrayList<>();
        ingredients.addAll(recipeEditorService.findAllIngredients());
        ingredients=recipeService.sortList(recipeEditorService.findImage(ingredients));

        List<SubprocedureComponent> tools=new ArrayList<>();
        tools.addAll(recipeEditorService.findAllTools());
        tools=recipeService.sortList(recipeEditorService.findImage(tools));

        Map<String, List<SubprocedureComponent>> returnObj = new HashMap<>();
        returnObj.put("ingredients", ingredients);
        returnObj.put("tools", tools);
        return returnObj;
    }
    @ResponseBody
    @GetMapping(path={"/recipe-list"})
    public List<Recipe> recipeList(){
        return recipeService.findAllPublishedRecipe();
    }

    public void updateRecipeHistory(Recipe recipe){

    }

    @ResponseBody
    @GetMapping(value={"/create","/edit/{recipeId}"})
    public ModelAndView getRecipeCreationPage(@PathVariable Optional<String> recipeId) {
        ModelAndView modelAndView;
        if(!recipeId.isPresent()){
            modelAndView = new ModelAndView("createlab");
        }
        else if( recipeService.findRecipe(recipeId.get())!=null && recipeService.findRecipe(recipeId.get()).getIsPublished()==false) {
            Recipe recipe = recipeService.findRecipe(recipeId.get());
            modelAndView = new ModelAndView("createlab");
            modelAndView.addObject("recipe", recipe);
        }else if (recipeService.findRecipe(recipeId.get())!=null && recipeService.findRecipe(recipeId.get()).getIsPublished()==true){
            modelAndView = new ModelAndView("userprofile");
            modelAndView.addObject("message","You cannot modify published recipes");
        }else{
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Recipe not found"
            );
        }
        return modelAndView;
    }

    /**
     * This method takes in a recipe and saves it. Works for save and publish.
     * @param recipe
     */
    @ResponseBody
    @PostMapping(path={"/save-recipe","/create-recipe"})
    public void createRecipe(@RequestBody Recipe recipe,Principal principal) {
        boolean isNewRecipe = recipe.getId() == null;
        if (recipe.getRecipeImage() != null)
            recipe.setRecipeImageUrl(recipeEditorService.findRecipeImageUrl(recipe.getRecipeImage()));
        Recipe savedRecipe = recipeService.saveRecipe(recipe,principal.getName());
        if (isNewRecipe) {
            userDomainService.addRecipeToUser(principal.getName(), savedRecipe);
        }
    }
    @ResponseBody
    @PostMapping(path={"/delete-recipe"})
    public void deleteRecipe(@RequestBody Recipe recipe){
        recipeService.deleteRecipe(recipe);
        userDomainService.deleteRecipeFromUser(recipe);
    }

//    @ResponseBody
//    @PostMapping(path={"/extend-recipe"})
//    public void extendRecipe(Recipe recipe){
////        RecipeRevisionHistory recipeRevisionHistory=new RecipeRevisionHistory();
//
//    }
    @ResponseBody
    @PostMapping(path={"/get-recipe-history"})
    public List<Recipe> getRecipeHistory(@RequestBody String recipeId){
        Recipe recipe = recipeService.findRecipe(recipeId);
        if(recipe.getParentId()==null){
            List<Recipe>returnList=new ArrayList<>();
            returnList.add(recipe);
            return returnList;
        }else{
            List<Recipe> parentRecipeList=getRecipeHistory(recipe.getParentId());
            parentRecipeList.add(recipe);
            return parentRecipeList;
        }
    }

    @ResponseBody
    @PostMapping(path={"/get-recipe-children"})
    public List<Recipe> getRecipeChildren(@RequestBody String recipeId){
        List<Recipe>returnRecipe=new ArrayList<>();
        List<Recipe>recipes=recipeService.findAllPublishedRecipe();
        for(Recipe recipe:recipes){
            if(recipe.getParentId()!=null && recipe.getParentId().equals(recipeId)){
                returnRecipe.add(recipe);
            }
        }
        return returnRecipe;
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
        return recipeEditorService.findAllToolActions(id);
    }

    @ResponseBody
    @GetMapping(path={"/get-all-user-recipes/{id}"})
    public List<Recipe> getAllUserRecipes(@PathVariable String id){
        return userDomainService.getUserByUsername(id).getRecipesCreated().stream().filter(obj->!obj.getIsPublished()).collect(Collectors.toList());
    }
    @ResponseBody
    @GetMapping(path={"/get-all-user-recipes"})
    public List<Recipe> getAllCreatedUserRecipes(Principal principal){
        return (List<Recipe>)userDomainService.getUserByUsername(principal.getName()).getRecipesCreated();
    }

    @ResponseBody
    @GetMapping(path={"/get-user-completed-recipes"})
    public List<Game> getAllCompletedUserRecipes(Principal principal){
        return (List<Game>)userDomainService.getUserByUsername(principal.getName()).getGamesPlayed();
    }

    @ResponseBody
    @GetMapping(path={"/get-all-user-in-progress-recipes"})
    public List<Game> getAllInProgressUserRecipes(Principal principal){
        return (List<Game>)userDomainService.getUserByUsername(principal.getName()).getGamesInProgress();
    }

    @ResponseBody
    @GetMapping(value = "/actions")
    public List<String> getAllActions() {
        return recipeEditorService.findAllPossibleIngredientActions();
//        return recipeEditorService.findAllActions();
    }


    /**
     * Return to home after publish
     */
    @ResponseBody
    @GetMapping(value = "/home/{name}")
    public ModelAndView publishRedirect(@PathVariable String name){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("message", "You have successfully published a lab: " + name);
        return modelAndView;
    }
    /**
     * Return to home after publish
     */
    @ResponseBody
    @GetMapping(value = "/home/save/{name}")
    public ModelAndView saveRedirect(@PathVariable String name){
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("message", "You have successfully saved a lab: " + name);
        return modelAndView;
    }

    @ResponseBody
    @PostMapping(value= "/valid-actions")
    public List<String> getValidToolActionsForIngredient(@RequestBody IngredientToolIntermediate ingredientToolIntermediate) {
        return recipeEditorService.retrieveValidToolActions(ingredientToolIntermediate.getIngredient(),ingredientToolIntermediate.getTool(),ingredientToolIntermediate.getIntermediateIngredient());
    }

    @ResponseBody
    @GetMapping(path= "/get-all-types")
    public List<String> getTypes() {
        return recipeEditorService.findAllTypes();
    }

    // Rating routes

    @ResponseBody
    @GetMapping(value= "/user-rating/{gameId}")
    public Rating fetchSingleUserRating(@PathVariable String gameId) {
        Rating r = new Rating();
        r.setScore(gameService.fetchSingleUserGameRating(gameId));
        return r;
    }

    @ResponseBody
    @GetMapping(value= "/rating/{recipeId}")
    public Rating fetchAggregateRating(@PathVariable String recipeId) {
        Rating r = new Rating();
        r.setScore(gameService.fetchRecipeRating(recipeId));
        return r;
    }

    @ResponseBody
    @PostMapping(value= "/update-recipe-rating")
    public void saveRating(@RequestBody Rating rating) {
        gameService.updateUserRecipeRating(rating);
    }

    @ResponseBody
    @GetMapping(value = "/get-top-recipes")
    public List<Recipe> handleRequestForTopRecipes() {
        return recipeService.retrieveListOfTopFiveRecipes();
    }

    @ResponseBody
    @GetMapping(value = "/get-modifiable-ingredients")
    public List<Ingredient> getUserModifiableIngredients(Principal principal) {
        return recipeEditorService.findAllUserCreatedModifiableIngredients(principal.getName());
    }

    @ResponseBody
    @GetMapping(value = "/get-ingredient-whitelist/{id}")
    public Whitelist getUserModifiableIngredients(@PathVariable String id) {
        return recipeEditorService.findIngredientWhitelist(id);
    }

}


