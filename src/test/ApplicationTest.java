
// TODO Make request, createrequest wrapper for exchange and request entity
// TODO Code inspection
import net.minidev.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import com.hexandria.Application;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ApplicationTest {

    final URI SIGNUP_URI = new URI("/api/signup");
    final URI LOGIN_URI = new URI("/api/login");
    final URI LOGOUT_URI = new URI("/api/logout");
    final URI USER_URI = new URI("/api/user");

    @Autowired
    private TestRestTemplate restTemplate;

    public ApplicationTest() throws URISyntaxException {
    }

    @Test
    public void registerTests() {

        final JSONObject json = new JSONObject();
        json.put("login", "test");
        json.put("password", "testypass");
        json.put("email", "testmail@mail.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);

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
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);

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

        json.clear();

        json.put("login", "test");
        json.put("password", "testypass");
        json.put("email", "testmail@mail.ru");
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

        ResponseEntity<String> response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = logoutUser(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        response = proceedPostRequest(new JSONObject(), LOGOUT_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    public void userGetTests(){

        JSONObject json = new JSONObject();
        json.put("login", "test");
        json.put("password", "testypass");
        json.put("email", "testmail@mail.ru");
        assertThat(proceedPostRequest(json, SIGNUP_URI).getStatusCode()).isEqualTo(HttpStatus.OK);
        json.remove("email");
        ResponseEntity<String> response = proceedPostRequest(json, LOGIN_URI);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        RequestEntity request = createGetRequest(USER_URI, response
                .getHeaders()
                .get(HttpHeaders.SET_COOKIE)
                .toArray(new String[0]));
//        response = restTemplate.getForObject(USER_URI, );
        request = RequestEntity.get(USER_URI).accept(MediaType.APPLICATION_JSON_UTF8).build();
        response = restTemplate.exchange(request, String.class);
        System.out.println(response.getBody());
    }

    @Test
    public void userPostTests(){

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
}
