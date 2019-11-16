package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.game.repository.KitchenToolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecipeEditorService {

    @Autowired
    BlacklistRepository blacklistRepository;
    @Autowired
    KitchenToolRepository kitchenToolRepository;

    public List<String> retrieveValidToolActions(String ingredientName, String toolName) {
        // ex.
        // salt : { knife : { chop, peel, slice } }
        List<String> blacklistedActions = blacklistRepository.findBlacklistedActions(ingredientName, toolName);
        // blacklistedActions should contain [chop, peel, slice]
        KitchenTool tool = kitchenToolRepository.findByName(toolName).get();
        List<String> allActions = null;
        if (tool != null) {
            allActions = (List) tool.getActions();
        }
        allActions.stream().filter(action -> !blacklistedActions.contains(blacklistedActions));
        return allActions;
    }
}
