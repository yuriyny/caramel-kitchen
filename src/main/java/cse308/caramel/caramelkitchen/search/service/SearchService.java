package cse308.caramel.caramelkitchen.search.service;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.user.persistence.User;
import cse308.caramel.caramelkitchen.user.storage.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class SearchService {
    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    KitchenToolRepository kitchenToolRepository;

    /*TODO: Add query to search for matching user*/
    public List<User> getCreators(String search){
        List<User>rl= new ArrayList<>(userRepository.findAllCreatorsContainingString(search));
        rl.sort(Comparator.comparing(User::getUsername));
        return rl;
    }
    public List<Recipe> getPublishedRecipes(String search){
        List<Recipe>rl= new ArrayList<>(recipeRepository.findAllRecipesContainingString(search));
        rl.sort(Comparator.comparing(Recipe::getRecipeName));
        return rl;
    }
    public List<Ingredient> getIngredients(String search){
        List<Ingredient>rl= new ArrayList<>(ingredientRepository.findAllIngredientsContainingString(search));
//        rl.sort(Comparator.comparing(Ingredient::getName));
        return rl;
    }
    public List<KitchenTool> getKitchenTools(String search){
        List<KitchenTool>rl=  new ArrayList<>(kitchenToolRepository.findAllEquipmentContainingString(search));
//        rl.sort(Comparator.comparing(KitchenTool::getName));
        return rl;
    }
}
