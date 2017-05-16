package mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.avatar.UserAvatar;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.base.Town;
import com.hexandria.mechanics.events.game.Start;
import com.hexandria.mechanics.events.logic.Create;
import com.hexandria.mechanics.events.logic.Delete;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.logic.Update;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;
import com.hexandria.websocket.Message;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by frozenfoot on 02.05.17.
 */
@SuppressWarnings("OverlyBroadThrowsClause")
public class MapperTest {
    final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testPing() throws Exception {
        final Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.PING\",\"payload\":\"testytest\"}",
                Message.class);
        assertThat(message.getClass()).isEqualTo(Ping.class);

    }

    @SuppressWarnings("OverlyBroadThrowsClause")
    @Test
    public void testMove() throws Exception {
        final Message message = objectMapper.readValue(
                "{\"event\":\"EVENTS.LOGIC.MOVE\",\"payload\":{\"from\":{\"x\":5,\"y\":5},\"to\":{\"x\":6,\"y\":5}}}",
                Message.class);
        assertThat(message.getClass()).isEqualTo(Move.class);
        }

    @Test
    public void testConnect() throws Exception {
        final Message message = objectMapper.readValue("{\"event\":\"EVENTS.SERVICE.CONNECT\",\"payload\":\"testytest\"}",
                Message.class);
        assertThat(message.getClass()).isEqualTo(Connect.class);
    }

    @Test
    public void testUpdate() throws JsonProcessingException {
        final Update update = new Update(new Coordinates(2, 3), new Coordinates(0, 0), 10, 10);
        assertThat(objectMapper.writeValueAsString(update))
                .isEqualTo("{\"event\":\"EVENTS.LOGIC.UPDATE\",\"payload\":" +
                        "{\"position\":{\"x\":2,\"y\":3},\"newPosition\":{\"x\":0,\"y\":0}," +
                        "\"newCount\":10,\"newMorale\":10}}");
    }

    @Test
    public void testDelete() throws JsonProcessingException {
        final Delete delete = new Delete(new Coordinates(0, 0));
        assertThat(objectMapper.writeValueAsString(delete))
                .isEqualTo("{\"event\":\"EVENTS.LOGIC.DELETE\",\"payload\":{\"position\":{\"x\":0,\"y\":0}}}");
    }

    @Test
    public void testStart() throws JsonProcessingException {
        @SuppressWarnings("unchecked") final List<UserAvatar> avatars = new LinkedList();
        avatars.add(new UserAvatar(1L, "TestAvatar1"));
        avatars.add(new UserAvatar(2L, "TestAvatar2"));
        final Game game = new Game(avatars);
        final Start start = new Start(game);
        assertThat(objectMapper.writeValueAsString(start)).isEqualTo("{\"event\":\"EVENTS.GAME.START\"," +
                "\"payload\":{\"sizeX\":10,\"sizeY\":15,\"towns\":[{\"coordinates\":{\"x\":2,\"y\":3}," +
                "\"name\":\"Town1\",\"moraleGenerated\":10,\"troopsGenerated\":15}," +
                "{\"coordinates\":{\"x\":7,\"y\":8},\"name\":\"Town2\",\"moraleGenerated\":10," +
                "\"troopsGenerated\":15}],\"capitals\":[{\"coordinates\":{\"x\":0,\"y\":0},\"squad\":{\"count\":50,\"morale\":30,\"owner\":{\"name\":\"TestAvatar1\"}},\"name\":\"capital1\",\"owner\":{\"name\":\"TestAvatar1\"},\"moraleGenerated\":10,\"troopsGenerated\":15},{\"coordinates\":{\"x\":9,\"y\":14},\"squad\":{\"count\":50,\"morale\":30,\"owner\":{\"name\":\"TestAvatar2\"}},\"name\":\"capital2\",\"owner\":{\"name\":\"TestAvatar2\"},\"moraleGenerated\":10,\"troopsGenerated\":15}],\"squads\":[{\"count\":50,\"morale\":30,\"owner\":{\"name\":\"TestAvatar1\"}},{\"count\":20,\"morale\":11,\"owner\":{\"name\":\"TestAvatar1\"}},{\"count\":10,\"morale\":5,\"owner\":{\"name\":\"TestAvatar1\"}},{\"count\":17,\"morale\":20,\"owner\":{\"name\":\"TestAvatar2\"}},{\"count\":50,\"morale\":10,\"owner\":{\"name\":\"TestAvatar2\"}},{\"count\":41,\"morale\":7,\"owner\":{\"name\":\"TestAvatar2\"}},{\"count\":50,\"morale\":30,\"owner\":{\"name\":\"TestAvatar2\"}}]}}");
    }

    @Test
    public void testCreate() throws JsonProcessingException {
        final Town testTown = new Town(new Coordinates(0, 0), "TestTown");
        testTown.setOwner(new UserAvatar((long) 1, "TestOwner"));
        testTown.generateSquads();
        final Create create = new Create(testTown);
        System.out.println(objectMapper.writeValueAsString(create));
    }
}
