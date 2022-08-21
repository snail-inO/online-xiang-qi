package me.portfolio.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:user_test.properties")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void postTest() throws Exception {
        this.mockMvc.perform(request(HttpMethod.POST, URI.create("/users")).content("{\"name\": \"abc\"}")
                .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andExpect(content().string(containsString("ONLINE")));
    }
}
