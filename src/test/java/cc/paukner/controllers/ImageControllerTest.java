package cc.paukner.controllers;

import cc.paukner.dtos.RecipeDto;
import cc.paukner.services.ImageService;
import cc.paukner.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTest {

    ImageController imageController;

    @Mock
    ImageService imageService;

    @Mock
    RecipeService recipeService;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        imageController = new ImageController(imageService, recipeService); // doesn't it have to get initialized? - Yes, as mock
        mockMvc = MockMvcBuilders.standaloneSetup(imageController).build();
    }

    @Test
    public void getImageForm() throws Exception {
        when(recipeService.findDtoById(anyLong())).thenReturn(RecipeDto.builder().id(1L).build());

        mockMvc.perform(get("/recipes/1/image"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"));

        verify(recipeService).findDtoById(anyLong());
    }

    @Test
    public void handleImagePost() throws Exception {
        mockMvc.perform(multipart("/recipes/1/image")
                .file(new MockMultipartFile("image", "testing.txt", "text/plain", "whatever".getBytes()))
                ).andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/recipes/1/details"));

        verify(imageService).saveImage(anyLong(), any());
    }
}
