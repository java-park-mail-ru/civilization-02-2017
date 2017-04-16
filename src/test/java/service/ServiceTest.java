package service;

import com.hexandria.Application;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.security.SecureRandom;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by frozenfoot on 30.03.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
public class ServiceTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private static SecureRandom rnd = new SecureRandom();

    @SuppressWarnings("Duplicates")
    @Test
    public void correctRegister() throws Exception {
        JSONObject json = new JSONObject();
        json.put("login", getRandomString(rnd, 10));
        json.put("password", getRandomString(rnd, 10));
        json.put("email", getRandomString(rnd, 10) + "@yandex.ru");
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isOk());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void userAlreadyExists() throws Exception {
        JSONObject json = new JSONObject();
        json.put("login", getRandomString(rnd, 10));
        json.put("password", getRandomString(rnd, 10));
        json.put("email", getRandomString(rnd, 10) + "@yandex.ru");
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isOk());
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void emailFormatError() throws Exception {
        JSONObject json = new JSONObject();
        json.put("login", getRandomString(rnd, 10));
        json.put("password", getRandomString(rnd, 10));
        json.put("email", getRandomString(rnd, 10));
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void emptyCredentialsError() throws Exception {
        JSONObject json = new JSONObject();
        json.put("login", getRandomString(rnd, 10));
        json.put("password", getRandomString(rnd, 10));
        json.put("email", "");
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void incorrectRequest() throws Exception {
        JSONObject json = new JSONObject();
        json.put("something", getRandomString(rnd, 10));
        json.put("password", getRandomString(rnd, 10));
        json.put("email", getRandomString(rnd, 10) + "@yandex.ru");
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void incorrectNickname() throws Exception {
        JSONObject json = new JSONObject();
        json.put("login", "&&&&%^^&*^%^&");
        json.put("password", getRandomString(rnd, 10));
        json.put("email", getRandomString(rnd, 10) + "@yandex.ru");
        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    public String getRandomString(SecureRandom random, int length){
        final String lettersAndDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; ++i){
            stringBuilder.append(lettersAndDigits.toCharArray()[random.nextInt(lettersAndDigits.length())]);
        }
        return stringBuilder.toString();
    }
}
