package codenemy.api.Auth.controller;

import codenemy.api.Auth.model.Role;
import codenemy.api.Auth.model.User;
import codenemy.api.Auth.repository.RoleRepo;
import codenemy.api.Auth.repository.UserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Chaudhary Samir Zafar
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class UserControllerIntegrationTest {
    private MockMvc mvc;
    @Autowired
    private WebApplicationContext context;

    @Autowired
    RoleRepo roleRepo;
    @Autowired
    UserRepo userRepo;

    @BeforeEach
    public void setUp() {

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void tearDown() {

        userRepo.deleteAll();
        roleRepo.deleteAll();;
    }

    @Test
    void getAllUserWithNoPermissions() throws Exception {

        this.mvc.perform(
                        get("/api/getAllUsers"))
                .andExpect(status().isForbidden());
    }

    @Test
    void logInWithWrongDetails() throws Exception {

        this.mvc.perform(
                get("http://localhost:8085/api/user/login")
                        .param("username","samirzafar")
                        .param("password", "Samir786!")
                        .servletPath("/api/user/login"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void registerAUser() throws Exception {


        Role role = new Role(0, "ROLE_USER");
        roleRepo.save(role);

        User user =
                new User(0, "samirzafar", "Samir786!", 0, 0, null, List.of(role));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(user);

        this.mvc.perform(
                post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
    }

    @Test
    void registerAUserAndLogIn() throws Exception {

        Role role = new Role(0, "ROLE_USER");
        roleRepo.save(role);

        User user =
                new User(0, "samirzafar", "Samir786!", 0, 0, null, List.of(role));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(user);

        this.mvc.perform(
                        post("/api/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        this.mvc.perform(
                        get("/api/user/login")
                                .param("username","samirzafar")
                                .param("password", "Samir786!")
                                .servletPath("/api/user/login"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void getAllUsersWithIncorrectPermissions() throws Exception {

        Role role = new Role(0, "ROLE_USER");
        roleRepo.save(role);

        User user =
                new User(0, "samirzafar", "Samir786!", 0, 0, null, List.of(role));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(user);

        this.mvc.perform(
                        post("/api/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        MvcResult result = this.mvc.perform(
                        get("/api/user/login")
                                .param("username","samirzafar")
                                .param("password", "Samir786!")
                                .servletPath("/api/user/login"))
                .andExpect(status().is3xxRedirection()).andReturn();

        String accessToken =
                JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");

        this.mvc.perform(
                        get("/api/getAllUsers")
                        .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void getAllUsersWithCorrectPermissions() throws Exception {

        Role role = new Role(0, "ROLE_ADMIN");
        roleRepo.saveAll(List.of(new Role(0, "ROLE_USER"), role));


        User user =
                new User(0, "samirzafar", "Samir786!", 0, 0, null, List.of(role));

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(user);

        this.mvc.perform(
                        post("/api/user/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonContent)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());

        MvcResult result = this.mvc.perform(
                        get("/api/user/login")
                                .param("username","samirzafar")
                                .param("password", "Samir786!")
                                .servletPath("/api/user/login"))
                .andExpect(status().is3xxRedirection()).andReturn();

        String accessToken =
                JsonPath.read(result.getResponse().getContentAsString(), "$.access_token");

        this.mvc.perform(
                        get("/api/getAllUsers")
                                .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk());
    }
}
