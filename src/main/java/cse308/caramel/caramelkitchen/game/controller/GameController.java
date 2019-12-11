package cse308.caramel.caramelkitchen.game.controller;

import cse308.caramel.caramelkitchen.game.model.BasicJsonResponse;
import cse308.caramel.caramelkitchen.game.model.GameApplication;
import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.persistence.Game;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.service.GameService;
import cse308.caramel.caramelkitchen.game.service.RecipeService;
import cse308.caramel.caramelkitchen.game.service.SubprocedureManager;
import cse308.caramel.caramelkitchen.user.persistence.User;
import cse308.caramel.caramelkitchen.user.service.UserDomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
        Game game;
        User currentUser = userDomainService.getUserByUsername(principal.getName());
        if(gameService.isGameInProgress(recipe, currentUser)) {
            game = gameService.fetchGameInprogress(recipe, currentUser);
        }
        else {
            game = gameService.createGame(recipe);
            userDomainService.saveInProgressGameToUser(principal.getName(),game);
        }
        //return view of recipe
        ModelAndView modelAndView = new ModelAndView("play");
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
    public void saveGameState (@RequestBody GameState gamestate){
        gameService.saveGameProgress(gamestate);
    }

    @ResponseBody
    @PostMapping(path={"/finish-game"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicJsonResponse finishedGame (@RequestBody Game game, Principal principal){
        BasicJsonResponse response = new BasicJsonResponse();
        gameService.saveGame(game);
        userDomainService.saveFinishedGameToUser(principal.getName(), game);
        response.setMessage(game.getScore().toString());
        return response;
    }

    @ResponseBody
    @PostMapping(path="/reset")
    public void resetGame(@RequestBody Game game, Principal principal) {
        Game gameFound = gameService.getGame(game.getId());
        gameFound.setGameState(null);
        gameFound.setScore(null);

        userDomainService.deleteGameFromInProgress(principal.getName(), gameFound);
        userDomainService.deleteGameFromFinished(principal.getName(), gameFound);

    }

}
