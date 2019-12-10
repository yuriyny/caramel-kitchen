package cse308.caramel.caramelkitchen.request.service;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.persistence.Whitelist;
import cse308.caramel.caramelkitchen.game.repository.WhitelistRepository;
import cse308.caramel.caramelkitchen.game.service.RecipeEditorService;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.request.persistence.Request;
import cse308.caramel.caramelkitchen.request.repository.RequestRepository;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
@Service
public class RequestService {
    @Autowired
    WhitelistRepository WhitelistRepository;
    @Autowired
    S3Services s3Services;
    @Autowired
    RecipeEditorService recipeEditorService;
    @Autowired
    RequestRepository requestRepository;

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

    public void storeRequest(String username,String type, String name, String description){
        Request r=new Request();
        r.setUsername(username);
        r.setType(type);
        r.setName(name);
        r.setDescription(description);
        requestRepository.save(r);
    }
    public List<Request>getAllRequests(){
        List<Request> r=requestRepository.findAll();
        r.sort(Comparator.comparing(Request::getType));
        return r;
    }
    public List<Request>getTypeRequests(String type){ //ingredient/tool/action/feedback
        List<Request> r=(List<Request>)requestRepository.findAllOfType(type);
        try {
            r.sort(Comparator.comparing(Request::getName));
        }catch (NullPointerException e){ }
        return r;
    }
    public List<Request>getUserRequests(String username){
        List<Request> r=(List<Request>)requestRepository.findByUsername(username);
        r.sort(Comparator.comparing(Request::getType));
        return r;
    }
}
