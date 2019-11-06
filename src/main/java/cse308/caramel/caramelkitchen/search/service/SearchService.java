package cse308.caramel.caramelkitchen.search.service;

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
    public List<User> getUsers(String search){
        return new ArrayList<>();
    }
    public List<Recipe> getRecipes(String search){
        return new ArrayList<>(recipeRepository.findAllRecipesContainingString(search));
    }
    public List<KitchenTool> getIngredients(String search){
        return new ArrayList<>(ingredientRepository.findAllIngredientsContainingString(search));
    }
    public List<KitchenTool> getKitchenTools(String search){
        return new ArrayList<>(kitchenToolRepository.findAllEquipmentContainingString(search));
    }
}
