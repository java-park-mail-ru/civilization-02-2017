package mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;
import com.hexandria.websocket.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * Created by frozenfoot on 02.05.17.
 */
@RunWith(SpringRunner.class)
public class MapperTests {
    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    public void testMapperPing(){
        Message message;
        TextMessage jsonTextMessage = new TextMessage("{\"event\":\"EVENTS.SERVICE.PING\",\"payload\":\"testytest\"}");
        try {
            message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);
            assertThat(message.getClass()).isEqualTo(Ping.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMapperConnect(){
        Message message;
        TextMessage jsonTextMessage = new TextMessage("{\"event\":\"EVENTS.SERVICE.CONNECT\",\"payload\":\"testytest\"}");
        try {
            message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);
            assertThat(message.getClass()).isEqualTo(Connect.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMapperMove(){

        Message message;
        TextMessage jsonTextMessage = new TextMessage("{\"event\":\"EVENTS.LOGIC.MOVE\",\"payload\":{\"playerIndex\":1,\"squadIndex\":2,\"moveTo\":{\"x\":5,\"y\":5}}}");
        try {
            message = objectMapper.readValue(jsonTextMessage.getPayload(), Message.class);
            assertThat(message.getClass()).isEqualTo(Move.class);
            System.out.println(message.getClass().getMethods());
        } catch (IOException e) {
            e.printStackTrace();
            assertThat(0).isEqualTo(1);
        }
    }
}
