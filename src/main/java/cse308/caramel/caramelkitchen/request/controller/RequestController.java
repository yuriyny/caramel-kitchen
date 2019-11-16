package cse308.caramel.caramelkitchen.request.controller;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class RequestController{
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg");
    @Autowired
    RequestService requestService;

    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> handleIngredientUpload(@RequestParam("name") String name,
                                                        @RequestParam("file") MultipartFile file,
                                                        // should be able to bind list by sending multiple input values to param 'blacklist'
                                                        // https://stackoverflow.com/questions/4596351/binding-a-list-in-requestparam
                                                        // KitchenTool only required to have 'name' and 'actions'
                                                        @RequestParam("blacklist") List<KitchenTool> blacklistedActions) {
        Map<String, Object> returnJson = new HashMap<>();
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){
            try {
                Ingredient uploadedIngredient = requestService.storeIngredient(name, file);
                requestService.updateIngredientToolActionBlacklist(name, blacklistedActions);
                returnJson.put("status", "ok");
                returnJson.put("result", uploadedIngredient);
            }
            catch (Exception e){
                returnJson.put("status", "error");
                returnJson.put("message", "Server error when uploading image");
            }

        }
        else {
            returnJson.put("status", "error");
            returnJson.put("message", "Input file type is not an image (png, jpeg)");
        }
        return returnJson;
    }

}

