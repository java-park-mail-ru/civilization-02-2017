package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;
import com.hexandria.websocket.Message;

import java.util.Arrays;

/**
 * Created by frozenfoot on 02.05.17.
 */
public class MapperTests {
	ObjectMapper objectMapper = new ObjectMapper();

	@Test
	public void testPing() throws Exception {
		Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.PING\",\"payload\":\"testytest\"}",
				Message.class);
		assertThat(message.getClass()).isEqualTo(Ping.class);

	}

	@Test
	public void testConnect() throws Exception {
		Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.CONNECT\",\"payload\":\"testytest\"}",
				Message.class);
		assertThat(message.getClass()).isEqualTo(Connect.class);
	}
}
