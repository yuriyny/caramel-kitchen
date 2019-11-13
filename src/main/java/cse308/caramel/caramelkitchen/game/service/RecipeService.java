package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.persistence.Subprocedure;
import cse308.caramel.caramelkitchen.game.persistence.SubprocedureComponent;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import cse308.caramel.caramelkitchen.game.repository.IngredientRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository recipeRepository;
    @Autowired
    IngredientRepository ingredientRepository;
    @Autowired
    KitchenToolRepository kitchenToolRepository;
    @Autowired
    UserDomainService userDomainService;
    @Autowired
    S3Services s3Services;

    public List<SubprocedureComponent> findAllEquipmentTool(){
        List<SubprocedureComponent> returnList=new ArrayList<>();
        returnList.addAll(kitchenToolRepository.findAll());
        returnList.addAll(ingredientRepository.findAll());
        returnList=sortList(returnList);
        returnList=findImage(returnList);
        return returnList;
    }

    public List<String> findAllToolActions(String toolId){
        return (List) kitchenToolRepository.findByName(toolId).get().getActions();
    }

    public List<Recipe> findAllRecipe(){ 
        return recipeRepository.findAll();
    }
    public Recipe findRecipe(String id){
        return recipeRepository.findById(id).get();
    }
    public void saveRecipe(Recipe recipe, String userName){
        if(recipe.getCreator()==null){
            recipe.setCreator(userName);
        }
        recipeRepository.save(recipe);
    }
    public List<SubprocedureComponent> sortList(List<SubprocedureComponent> list){
        list.sort(Comparator.comparing(a->a.getName()));
        return list;
    }
    public List<SubprocedureComponent> findImage(List<SubprocedureComponent> list){
        for (SubprocedureComponent subprocedureComponent : list){
            if(subprocedureComponent.getImageName()!=null){
                String URL=s3Services.getImageUrl(subprocedureComponent.getImageName());
                subprocedureComponent.setImageFileUrl(URL);
            }
        }
        return list;
    }
}
