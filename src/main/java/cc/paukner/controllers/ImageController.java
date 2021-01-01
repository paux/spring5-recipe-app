package cc.paukner.controllers;

import cc.paukner.dtos.RecipeDto;
import cc.paukner.services.ImageService;
import cc.paukner.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {

    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipes/{id}/image")
    public String showUploadForm(@PathVariable String id, Model model) {
        model.addAttribute("recipe", recipeService.findDtoById(Long.valueOf(id)));
        return "recipes/image-upload";

    }

    @PostMapping("/recipes/{id}/image")
    public String handleImagePost(@PathVariable String id, @RequestParam("image") MultipartFile file) {
        imageService.saveImage(Long.valueOf(id), file);
        return "redirect:/recipes/" + id + "/details";
    }

    @GetMapping("/recipes/{id}/get-image")
    public void renderImageFromDb(@PathVariable String id, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        RecipeDto recipeDto = recipeService.findDtoById(Long.valueOf(id));
        if (null == recipeDto.getImage()) {
            log.debug("No image persisted");
            return;
        }
        byte[] byteArray = new byte[recipeDto.getImage().length];
        int i = 0;
        for (Byte wrappedByte : recipeDto.getImage()) {
            byteArray[i++] = wrappedByte; // auto-unboxing
        }
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        IOUtils.copy(inputStream, response.getOutputStream());
    }
}
