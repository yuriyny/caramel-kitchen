package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.Equipment;
import cse308.caramel.caramelkitchen.game.Ingredient;
import cse308.caramel.caramelkitchen.game.Items;
import cse308.caramel.caramelkitchen.game.Recipe;
import cse308.caramel.caramelkitchen.game.repository.EquipmentRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.user.User;
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
    EquipmentRepository equipmentRepository;

    /*TODO: Add query to search for matching user*/
    public List<User> getUsers(String search){
        return new ArrayList<>();
    }
    public List<Recipe> getRecipes(String search){
        return new ArrayList<>(recipeRepository.findAllRecipesContainingString(search));
    }
    public List<Items> getIngredients(String search){
        return new ArrayList<>(ingredientRepository.findAllIngredientsContainingString(search));
    }
    public List<Items> getKitchenTools(String search){
        return new ArrayList<>(equipmentRepository.findAllEquipmentContainingString(search));
    }
}
