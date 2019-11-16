package cse308.caramel.caramelkitchen.request.service;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class RequestService {
    @Autowired
    RecipeService recipeService;
    @Autowired
    BlacklistRepository blacklistRepository;

    public Ingredient storeIngredient(String name, MultipartFile file) {
        Ingredient ingredient = new Ingredient();
        ingredient.setImageName(file.getName());
        // upload to S3, get S3 url
        ingredient.setImageFileUrl();
        ingredient.setName(name);

        recipeService.saveIngredient(ingredient);
        return ingredient;

    }

    public void updateIngredientToolActionBlacklist(String ingredientName,
                                                    List<KitchenTool> blacklistedToolActions) {
        // parameters give access to ingredient, tool, and tool actions to ban
        // make call to blacklistRepository to update this tool action blacklist for the ingredient-tool pair
    }
}
