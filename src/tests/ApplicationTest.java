package sample.tests;

import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ApplicationTest {

    static final String SIGNUP_URI = "/api/signup";
    static final String LOGIN_URI = "/api/login";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registerTests() {

        JSONObject json = new JSONObject();
        json.put("login", "test");
        json.put("password", "testypass");
        json.put("email", "testmail@mail.ru");
        assertThat(makeRequest(json, SIGNUP_URI).getStatusCode().value()).isEqualTo(200);

        json.clear();

        json.put("login", "test");
        assertThat(makeRequest(json, SIGNUP_URI).getStatusCode().value()).isEqualTo(400);

        json.clear();

        json.put("login", "   ");
        json.put("password", "testypass");
        json.put("email", "testmail@mail.ru");
        assertThat(makeRequest(json, SIGNUP_URI).getStatusCode().value()).isEqualTo(400);

        json.clear();

        json.put("login", "test-user");
        json.put("password", "test-password");
        json.put("email", "test_email@test.ru");
        assertThat(makeRequest(json, SIGNUP_URI).getStatusCode().value()).isEqualTo(409);
    }

    @Test
    public void loginTests() {
        JSONObject json = new JSONObject();
        json.put("login", "test-user");
        json.put("password", "test-password");
        assertThat(makeRequest(json, LOGIN_URI).getStatusCode().value()).isEqualTo(200);

        json.clear();

        json.put("login", "test");
        json.put("password", "testypass");
        assertThat(makeRequest(json, LOGIN_URI).getStatusCode().value()).isEqualTo(409);

        json.clear();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity request = new HttpEntity<>(json, headers);
        System.out.print(restTemplate.getForObject("/api/logout", String.class).toString());

//        System.out.print(restTemplate.getForObject("/api/logout", String.class).toString());

        json.put("login", "test");
        json.put("password", "testypass");
        assertThat(makeRequest(json, LOGIN_URI).getStatusCode().value()).isEqualTo(200);
    }


    public ResponseEntity<String> makeRequest(JSONObject json, String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity request = new HttpEntity<>(json.toString(), headers);
        return restTemplate.postForEntity(url, request, String.class);
    }
}
