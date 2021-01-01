package cc.paukner.services;

import cc.paukner.domain.Recipe;
import cc.paukner.repositories.RecipeRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DefaultImageServiceTest {

    ImageService imageService;

    @Mock
    RecipeRepository recipeRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageService = new DefaultImageService(recipeRepository);
    }

    @Test
    public void saveImage() throws Exception {
        MultipartFile multipartFile = new MockMultipartFile("image", "testing.txt", "text/plain", "whatever".getBytes());
        Long id = 1L;
        when(recipeRepository.findById(anyLong())).thenReturn(Optional.of(Recipe.builder().id(id).build()));
        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        imageService.saveImage(id, multipartFile);

        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, savedRecipe.getImage().length);
    }
}
