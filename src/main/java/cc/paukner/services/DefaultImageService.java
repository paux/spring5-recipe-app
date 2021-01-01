package cc.paukner.services;

import cc.paukner.domain.Recipe;
import cc.paukner.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class DefaultImageService implements ImageService {

    RecipeRepository recipeRepository;

    public DefaultImageService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public void saveImage(Long recipeId, MultipartFile image) {
        try {
            Recipe recipe = recipeRepository.findById(recipeId).get();
            // we'll get primitive byte[], but we need the wrapper class Byte[]; sadly, no auto-boxing
            Byte[] bytes = new Byte[image.getBytes().length];
            int i = 0;
            for (byte b : image.getBytes()) {
                bytes[i++] = b;
            }
            recipe.setImage(bytes);
            recipeRepository.save(recipe);
        } catch (IOException e) {
            // TODO: better error handling
            log.error("Error occurred", e);
            e.printStackTrace();
        }
    }
}
