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

    public List<SubprocedureComponent> findAllEquipmentTool(){
        List<SubprocedureComponent> returnList=new ArrayList<>();
        returnList.addAll(kitchenToolRepository.findAll());
        returnList.addAll(ingredientRepository.findAll());
        returnList=sortList(returnList);
        return returnList;
    }
    public List<Recipe> findAllRecipe(){ 
        return recipeRepository.findAll();
    }
    public void saveRecipe(Recipe recipe){
        recipe.setCreator(userDomainService.getCurrentUser().getUsername());
        recipeRepository.save(recipe);
    }
    public List<SubprocedureComponent> sortList(List<SubprocedureComponent> list){
        list.sort(Comparator.comparing(a->a.getName()));
        return list;
    }
}
