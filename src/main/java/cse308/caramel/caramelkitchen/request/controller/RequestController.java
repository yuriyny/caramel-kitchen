package cse308.caramel.caramelkitchen.request.controller;

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
                                      @RequestParam("file") MultipartFile file) {
        Map<String, Object> returnJson = new HashMap<>();
        String fileContentType = file.getContentType();
        if(contentTypes.contains(fileContentType)){
            try {
                requestService.storeIngredient(name, file);
                returnJson.put("status", "ok");
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

