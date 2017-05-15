package controller;

import com.hexandria.Application;
import net.minidev.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.transaction.Transactional;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
@Profile("test")
@Transactional
public class ApplicationTest {

    private static SecureRandom rnd = new SecureRandom();
    private final URI SIGNUP_URI = new URI("/api/user/signup");
    private final URI LOGIN_URI = new URI("/api/user/login");
    private final URI LOGOUT_URI = new URI("/api/user/logout");
    private final URI USER_URI = new URI("/api/user/");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public ApplicationTest() throws URISyntaxException {
    }

    @Before
    public void clearDataBaseBefore() throws SQLException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    @After
    public void clearDatabase() throws SQLException {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "users");
    }

    /**
     * Correct User Registration
     */
    @Test
    public void correctRegister() {
        JSONObject json = createRegisterJson(rnd);
        ResponseEntity response = proceedPostRequest(json, SIGNUP_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * Incorrect request
     */
    @Test
    public void incorrectRequest() {
        JSONObject json = new JSONObject();
        json.put("login", "test");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * Empty credentials error
     */
    @Test
    public void emptyCredentials() {
        JSONObject json = createRegisterJson(rnd);
        json.put("login", "   ");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * Trying to register alread registered user
     */
    @Test
    public void alreadyRegisteredUser() {
        JSONObject json = createRegisterJson(rnd);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    /**
     * Wrong email format
     */
    @Test
    public void emailFormatTest() {
        JSONObject json = createRegisterJson(rnd);
        json.put("email", "test_emailt.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * Multiple errors
     */
    @Test
    public void multipleErrors() {
        JSONObject json = createRegisterJson(rnd);
        json.put("password", "");
        json.put("email", "test_emailt.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    /**
     * Register, login and logout user
     */
    @Test
    public void loginTest() throws URISyntaxException {
        correctRegister();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        ResponseEntity response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(logoutUser(response).getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    /**
     * Create user and try to login with incorrect password credentials
     */
    public void incorrectPasswordLogin() throws URISyntaxException {
        loginTest();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        json.put("password", "-----");
        ResponseEntity response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    /**
     * Logout already logged out user
     */
    @Test
    public void logoutTests() throws URISyntaxException {
        loginTest();
        ResponseEntity response = proceedPostRequest(new JSONObject(), LOGOUT_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    /**
     * Correct user GET request
     */
    @Test
    public void userGetTests() throws URISyntaxException {
        loginTest();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        json.remove("email");
        RequestEntity request = createGetRequest(USER_URI, proceedPostRequest(json, LOGIN_URI).
                getHeaders().get(HttpHeaders.SET_COOKIE).toArray(new String[0]));
        ResponseEntity response = restTemplate.exchange(request, String.class);
        json.remove("password");
        assertThat(response.getBody().toString().equals(json.toString()));
        assertThat(restTemplate.exchange(createGetRequest(USER_URI, ""), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }

    /**
     * Correct user POST request
     */
    @Test
    public void userPostTest() throws URISyntaxException {
        loginTest();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        ResponseEntity response = proceedPostRequest(json, LOGIN_URI);
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
    }

    @Test
    public void userPostEmptyLogin() throws URISyntaxException {
        userPostTest();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        String[] cookies = proceedPostRequest(json, LOGIN_URI)
                .getHeaders()
                .get(HttpHeaders.SET_COOKIE)
                .toArray(new String[0]);
        json.put("login", "");
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void userPostIncorrectPassword() throws URISyntaxException {
        userPostTest();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        String[] cookies = proceedPostRequest(json, LOGIN_URI).
                getHeaders().
                get(HttpHeaders.SET_COOKIE).
                toArray(new String[0]);
        json.put("newPassword", getRandomString(rnd, 12));
        json.put("password", "--------");
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);
    }
    @Test
    public void userPostIncorrectLogin() throws URISyntaxException {
        userPostTest();
        JSONObject json = createRegisterJson(rnd);
        proceedPostRequest(json, SIGNUP_URI);
        String[] cookies = proceedPostRequest(json, LOGIN_URI)
                .getHeaders()
                .get(HttpHeaders.SET_COOKIE)
                .toArray(new String[0]);
        json.put("newPassword", getRandomString(rnd, 12));
        json.put("login", "--------");
        assertThat(restTemplate
                .exchange(createPostRequest(json, USER_URI, cookies), String.class)
                .getStatusCode())
                .isEqualTo(HttpStatus.FORBIDDEN);

    }

    public RequestEntity createGetRequest(URI uri, String... cookies) {

        return RequestEntity.get(uri)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .header(HttpHeaders.COOKIE, cookies)
                .build();
    }

    public RequestEntity createPostRequest(JSONObject json, URI uri, String... cookies) throws URISyntaxException {
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

    public String getRandomString(SecureRandom random, int length) {
        final String lettersAndDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            stringBuilder.append(lettersAndDigits.toCharArray()[random.nextInt(lettersAndDigits.length())]);
        }
        return stringBuilder.toString();
    }

    public JSONObject createRegisterJson(SecureRandom rnd) {

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
