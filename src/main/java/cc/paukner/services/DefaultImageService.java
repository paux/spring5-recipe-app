package cc.paukner.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class DefaultImageService implements ImageService {

    @Override
    public void saveImage(Long recipeId, MultipartFile image) {
        // TODO
        log.debug("Received a file");
    }
}
