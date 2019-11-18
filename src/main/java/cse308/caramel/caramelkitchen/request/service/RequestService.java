package cse308.caramel.caramelkitchen.request.service;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.repository.BlacklistRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
@Service
public class RequestService {
    @Autowired
    RecipeService recipeService;
    @Autowired
    BlacklistRepository blacklistRepository;
    @Autowired
    S3Services s3Services;

    public Ingredient storeIngredient(String name, MultipartFile file) throws IOException {
        Ingredient ingredient = new Ingredient();
        ingredient.setImageName(file.getName());
        s3Services.uploadMultipartFileObject(file.getName(), file);
        // upload to S3, get S3 url
        //sets images url (generated using getImageUrl method)
        ingredient.setImageFileUrl(s3Services.getImageUrl(file.getName()));
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
