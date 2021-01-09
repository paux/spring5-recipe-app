package cc.paukner.controllers;

import cc.paukner.dtos.RecipeDto;
import cc.paukner.services.ImageService;
import cc.paukner.services.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
        mockMvc = MockMvcBuilders.standaloneSetup(imageController)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
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

    @Test
    public void renderImageFromDb() throws Exception {
        RecipeDto recipeDto = RecipeDto.builder().id(1L).build();

        String fakeImage = "fake image";
        Byte[] bytesBoxed = new Byte[fakeImage.getBytes().length];
        int i = 0;
        for (byte bait : fakeImage.getBytes()) {
            bytesBoxed[i++] = bait;
        }
        recipeDto.setImage(bytesBoxed);
        when(recipeService.findDtoById(anyLong())).thenReturn(recipeDto);

        MockHttpServletResponse response = mockMvc.perform(get("/recipes/1/get-image"))
                .andExpect(status().isOk())
                .andReturn().getResponse();
        assertEquals(fakeImage.getBytes().length, response.getContentAsByteArray().length);
    }

    @Test
    public void getImageForm_IdInvalidNumber() throws Exception {
        mockMvc.perform(get("/recipes/invalid/image"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400"));
    }
}
