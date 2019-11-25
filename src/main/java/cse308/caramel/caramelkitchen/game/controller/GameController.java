package cse308.caramel.caramelkitchen.game.controller;

import cse308.caramel.caramelkitchen.game.model.GameApplication;
import cse308.caramel.caramelkitchen.game.service.SubprocedureManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GameController {

    @Autowired
    SubprocedureManager subprocedureManager;

    /*
    Method to read GameState associated with the current recipe and user, redirect to get GameApplication
     */

    @ResponseBody
    @GetMapping(path={"/game-app/{id}"})
    public GameApplication getMinigame(@PathVariable(value = "id") String id) {
        return subprocedureManager.loadGame(id);
    }
}
