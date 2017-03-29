package service;

import com.hexandria.Application;
import com.hexandria.auth.common.user.UserEntity;
import com.hexandria.auth.common.user.UserManager;
import org.junit.Before;
import org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.security.SecureRandom;

import static org.junit.Assert.assertEquals;

/**
 * Created by frozenfoot on 30.03.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Transactional
@WebAppConfiguration
@ComponentScan(basePackages = {"com.hexandria.auth.common.user"})
public class ServiceTest {

    UserManager userManager;
    private static SecureRandom rnd;

    @Before
    public void setup(){
        UserManager userManager = new UserManager();
        rnd = new SecureRandom();
    }

    @Test
    public void addTestUser(){
        String testLogin = getRandomString(rnd, 10);
        String testPassword = getRandomString(rnd, 12);
        String testMail = getRandomString(rnd, 6) + "@mail.ru";
        UserEntity newUser = userManager.createUser(new UserEntity(testLogin, testPassword, testMail));
        assertEquals(testLogin, newUser.getLogin());
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
