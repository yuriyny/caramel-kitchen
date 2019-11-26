package cse308.caramel.caramelkitchen.game.controller;

import cse308.caramel.caramelkitchen.game.model.GameApplication;
import cse308.caramel.caramelkitchen.game.persistence.Game;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.service.GameService;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.game.service.SubprocedureManager;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;

@Controller
public class GameController {

    @Autowired
    SubprocedureManager subprocedureManager;
    @Autowired
    RecipeService recipeService;
    @Autowired
    UserDomainService userDomainService;
    @Autowired
    GameService gameService;

    /**
     * This method will direct user to the playlab page with the recipe of their choice
     * @param id    recipe id that is selected by the user
     * @return
     */
    @ResponseBody
    @GetMapping(path="/play/{id}")
    public ModelAndView playGame(@PathVariable String id, Principal principal){
        Recipe recipe=recipeService.findRecipe(id);
        //create new game, set all necessary game info, store game in user
        Game game=gameService.createGame(recipe);
        userDomainService.saveInProgressGameToUser(principal.getName(),game);

        //return view of recipe
        ModelAndView modelAndView = new ModelAndView("/playlab");
        modelAndView.addObject("game",game);
        return modelAndView;
    }

    /*
    Method to read GameState associated with the current recipe and user, redirect to get GameApplication
     */

    @ResponseBody
    @GetMapping(path={"/game-app/{id}"})
    public GameApplication getMinigame(@PathVariable(value = "id") String id) {
        return subprocedureManager.loadGame(id);
    }

    @ResponseBody
    @PostMapping(path={"/save-game"})
    public void saveGameState (@RequestBody String gameId, @RequestBody String recipeId,  @RequestBody List<String>stepsCompleted,@RequestBody List<Double> scores){
        gameService.saveGameProgress(gameId, recipeId,stepsCompleted,scores);
    }

    @ResponseBody
    @PostMapping(path={"/finish-game"})
    public void finishedGame (@RequestBody Game game,Principal principal){
        userDomainService.saveFinishedGameToUser(principal.getName(),game);
    }
}
