package cse308.caramel.caramelkitchen.request.controller;

import cse308.caramel.caramelkitchen.game.model.BasicJsonResponse;
import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.service.RecipeEditorService;
import cse308.caramel.caramelkitchen.request.persistence.Request;
import cse308.caramel.caramelkitchen.request.service.RequestService;
import cse308.caramel.caramelkitchen.s3client.services.S3Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class RequestController{
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg");
    @Autowired
    RequestService requestService;
    @Autowired
    S3Services s3Services;
    @Autowired
    RecipeEditorService recipeEditorService;

    @ResponseBody
    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView handleIngredientUpload(@RequestParam("id") String id, @RequestParam("name") String name,
                                                        @RequestParam("file") MultipartFile file,
                                                        // should be able to bind list by sending multiple input values to param 'blacklist'
                                                        // https://stackoverflow.com/questions/4596351/binding-a-list-in-requestparam
                                                        // KitchenTool only required to have 'name' and 'actions'
                                                        @RequestParam("listed actions") List<String> listedActions,
                                                        @RequestParam("type") String type,
                                                        Principal principal) {
        ModelAndView modelAndView = new ModelAndView("userprofile");
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){

            List<Ingredient> ingredients=recipeEditorService.findAllIngredients();
            if (!ingredients.stream().filter(obj->obj.getId().equals(id)).collect(Collectors.toList()).isEmpty()){
                try {
                    requestService.saveModifiedIngredient(id, name, file,type);
                } catch (Exception e) {
                    modelAndView.addObject("message", "Server error when uploading image");
                }
                requestService.updateIngredientToolActionWhitelist(name, listedActions);
                modelAndView.addObject("message","Ingredient has been updated");
            }
            else {
                try {
                    Ingredient uploadedIngredient = requestService.storeIngredient(name, file,type, principal.getName());
                    requestService.updateIngredientToolActionWhitelist(name, listedActions);
                    modelAndView.addObject("message", "ingredient uploaded successfully");
                } catch (Exception e) {
                    modelAndView.addObject("message", "Server error when uploading image");
                }
            }
        }
        else {
            modelAndView.addObject("message","Input file is not an image (png, jpeg)");
        }
        return modelAndView;
    }
    @ResponseBody
    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicJsonResponse handleUpload(@RequestParam("file") MultipartFile file, Principal principal) {
        BasicJsonResponse response = new BasicJsonResponse();
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){
            try {
                long unixTime = System.currentTimeMillis() / 1000L;
                final String imageKey = principal.getName() + "_" + unixTime + file.getOriginalFilename();
                requestService.storeImage(imageKey, file);
                response.setMessage(imageKey);
            }
            catch (Exception e){
                response.setMessage("Server error when uploading image");
            }
        }
        else {
            response.setMessage("Input file is not an image (png, jpeg)");
        }
        return response;
    }

    @ResponseBody
    @PostMapping(value = "/requests")
    public String handleRequests(@RequestBody String type, @RequestBody String name, @RequestBody String description, Principal principal){//if feedback, leave name null
        requestService.storeRequest(principal.getName(),type,name,description);
        return "/about";
    }

    @ResponseBody
    @GetMapping(value = "/get-requests")
    public List<Request> getRequests(){
        return requestService.getAllRequests();
    }

    @ResponseBody
    @GetMapping(value = "/get-requests/{type}")//ingredient/tool/action/feedback
    public List<Request> getRequestsUsingTypes(@PathVariable String type){
        return requestService.getTypeRequests(type);
    }


    @ResponseBody
    @GetMapping(value = "/get-user-requests")
    public List<Request> getUserRequests(Principal principal){
        return requestService.getUserRequests(principal.getName());
    }
}

