// TODO Code inspection
import com.hexandria.Application;
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ApplicationTest {

    private static SecureRandom rnd = new SecureRandom();
    private final URI SIGNUP_URI = new URI("/api/signup");
    private final URI LOGIN_URI = new URI("/api/login");
    private final URI LOGOUT_URI = new URI("/api/logout");
    private final URI USER_URI = new URI("/api/user");

    @Autowired
    private TestRestTemplate restTemplate;

    public ApplicationTest() throws URISyntaxException {
    }

    @Test
    public void registerTests() {

        JSONObject json = createRegisterJson(rnd);
        ResponseEntity response = proceedPostRequest(json, SIGNUP_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        json.clear();

        json.put("login", "test");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        json.clear();

        json.put("login", "   ");
        json.put("password", "testypass");
        json.put("email", "testmail@mail.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        json.clear();

        json.put("login", "test-user");
        json.put("password", "test-password");
        json.put("email", "test_email@test.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        json.clear();

        json.put("login", "mailtest");
        json.put("password", "test-password");
        json.put("email", "test_emailt.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        json.clear();

        json.put("login", "mailtest");
        json.put("password", "");
        json.put("email", "test_emailt.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void loginTests() throws URISyntaxException {

        JSONObject json = new JSONObject();
        json.put("login", "test-user");
        json.put("password", "test-password");

        ResponseEntity<String> response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = logoutUser(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        json.remove("email");

        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        logoutUser(response);

        json.put("password", "");
        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        json.remove("password");
        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

    }

    @Test
    public void logoutTests() throws URISyntaxException {

        JSONObject json = new JSONObject();
        json.put("login", "test-user");
        json.put("password", "test-password");
        ResponseEntity response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        response = logoutUser(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = proceedPostRequest(new JSONObject(), LOGOUT_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);

        json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        json.remove("email");
        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        response = logoutUser(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }

    @Test
    public void userGetTests() throws URISyntaxException {

        JSONObject json = createRegisterJson(rnd);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        JSONObject loginJson = new JSONObject(json);
        loginJson.remove("email");
        ResponseEntity response = proceedPostRequest(json, LOGIN_URI);
        String[] cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        RequestEntity request = createGetRequest(USER_URI, cookies);
        response = restTemplate.exchange(request, String.class);
        json.remove("password");
        assertThat(response.getBody().toString().equals(json.toString()));

        assertThat(restTemplate.exchange(createGetRequest(USER_URI, ""), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);

    }
    @AutoConfigureMockMvc
    @Test
    public void userPostTests() throws URISyntaxException {
        JSONObject json = createRegisterJson(rnd);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        ResponseEntity<String> response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String[] cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]);
        String oldPassword = json.getAsString("password");
        String newPassword = getRandomString(rnd, 12);
        json.put("newPassword", newPassword);
        assertThat(proceedPostRequest(json, USER_URI).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.OK);
        logoutUser(response);
        json.remove("newPassword");
        json.put("password", newPassword);
        assertThat(proceedPostRequest(json, LOGIN_URI).getStatusCode()).isEqualTo(HttpStatus.OK);

        json = createRegisterJson(rnd);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]);
        json.put("login", "");
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);

        json = createRegisterJson(rnd);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]);
        json.put("newPassword", getRandomString(rnd, 12));
        json.put("password", "--------");
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);

        json = createRegisterJson(rnd);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]);
        json.put("newPassword", getRandomString(rnd, 12));
        json.put("login", "--------");
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);

    }

    public RequestEntity createGetRequest(URI uri, String ... cookies){

        return RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(HttpHeaders.COOKIE, cookies)
                .build();
    }

    public RequestEntity createPostRequest(JSONObject json, URI uri, String ... cookies) throws URISyntaxException {
        return RequestEntity.post(uri)
                .header(HttpHeaders.COOKIE, cookies)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(json);
    }

    public ResponseEntity<String> proceedPostRequest(JSONObject json, URI url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity request = new HttpEntity<>(json.toString(), headers);
        return restTemplate.postForEntity(url, request, String.class);
    }

    public ResponseEntity<String> logoutUser(ResponseEntity<String> response) throws URISyntaxException {
        String[] cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]);
        RequestEntity<JSONObject> request = createPostRequest(new JSONObject(), LOGOUT_URI, cookies);
        return restTemplate.exchange(request, String.class);
    }

    public String getRandomString(SecureRandom random, int length){
        final String lettersAndDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; ++i){
            stringBuilder.append(random.nextInt(lettersAndDigits.length()));
        }
        return stringBuilder.toString();
    }

    public JSONObject createRegisterJson(SecureRandom rnd){

        JSONObject json = new JSONObject();
        String login = getRandomString(rnd, 12);
        String password = getRandomString(rnd, 12);
        String email = getRandomString(rnd, 8) + "@mail.ru";
        json.put("login", login);
        json.put("password", password);
        json.put("email", email);
        return json;
    }
}
