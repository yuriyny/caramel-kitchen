package cse308.caramel.caramelkitchen.request.controller;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.request.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class RequestController{
    private static final List<String> contentTypes = Arrays.asList("image/png", "image/jpeg");
    @Autowired
    RequestService requestService;

    @ResponseBody
    @PostMapping(value = "/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView handleIngredientUpload(@RequestParam("name") String name,
                                                        @RequestParam("file") MultipartFile file,
                                                        // should be able to bind list by sending multiple input values to param 'blacklist'
                                                        // https://stackoverflow.com/questions/4596351/binding-a-list-in-requestparam
                                                        // KitchenTool only required to have 'name' and 'actions'
                                                        @RequestParam("listed actions") List<String> listedActions) {
        ModelAndView modelAndView = new ModelAndView("/userprofile");
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){
            try {
                Ingredient uploadedIngredient = requestService.storeIngredient(name, file);
                requestService.updateIngredientToolActionWhitelist(name, listedActions);
                modelAndView.addObject("message","ingredient uploaded successfully");
            }
            catch (Exception e){
                modelAndView.addObject("message","Server error when uploading image");
            }
        }
        else {
            modelAndView.addObject("message","Input file is not an image (png, jpeg)");
        }
        return modelAndView;
    }
}

