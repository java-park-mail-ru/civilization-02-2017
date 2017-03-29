import com.hexandria.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Created by frozenfoot on 30.03.17.
 */
@SpringBootApplication
@Import(Application.class)
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
