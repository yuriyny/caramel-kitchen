package cse308.caramel.caramelkitchen.request.service;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.Whitelist;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeEditorService;
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
    WhitelistRepository WhitelistRepository;
    @Autowired
    S3Services s3Services;
    @Autowired
    RecipeEditorService recipeEditorService;

    public Ingredient storeIngredient(String name, MultipartFile file) throws IOException {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setImageName(file.getOriginalFilename());
        s3Services.uploadMultipartFileObject(file.getOriginalFilename(), file);

//        ingredient.setImageFileUrl(s3Services.getImageUrl(file.getOriginalFilename()));//image will be fetched at recipe creation page
        recipeEditorService.saveIngredient(ingredient);
        return ingredient;

    }

    public void updateIngredientToolActionWhitelist(String ingredientName,
                                                    List<String> whitelistedToolActions) {
        Whitelist ingredient=new Whitelist();
        ingredient.setName(ingredientName);
        ingredient.getActions().addAll(whitelistedToolActions);
        WhitelistRepository.save(ingredient);
        // parameters give access to ingredient, tool, and tool actions to ban
        // make call to blacklistRepository to update this tool action blacklist for the ingredient-tool pair
    }
}
