package mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.base.Capital;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.base.Town;
import com.hexandria.mechanics.events.game.Start;
import com.hexandria.mechanics.events.logic.Create;
import com.hexandria.mechanics.events.logic.Delete;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.events.logic.Update;
import com.hexandria.mechanics.events.service.Connect;
import com.hexandria.mechanics.events.service.Ping;
import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.websocket.Message;
import org.junit.Test;

import java.util.ArrayList;
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
        final List<GamePlayer> avatars = new ArrayList<>();
        avatars.add(new GamePlayer(1L, "TestAvatar1"));
        avatars.add(new GamePlayer(2L, "TestAvatar2"));
        final Game game = new Game(avatars);
        final Start start = new Start(game);
        assertThat(objectMapper.writeValueAsString(start)).isEqualTo("{\"event\":\"EVENTS.GAME.START\"," +
                "\"payload\":{\"sizeX\":10,\"sizeY\":15,\"towns\":[{\"position\":{\"x\":2,\"y\":3}," +
                "\"name\":\"Town1\"},{\"position\":{\"x\":7,\"y\":8},\"name\":\"Town2\"}]," +
                "\"capitals\":[{\"position\":{\"x\":0,\"y\":0},\"squad\":{\"count\":50,\"morale\":30}," +
                "\"name\":\"capital1\",\"owner\":{\"name\":\"TestAvatar1\"}},{\"position\":{\"x\":8,\"y\":13}," +
                "\"squad\":{\"count\":50,\"morale\":30},\"name\":\"capital2\"," +
                "\"owner\":{\"name\":\"TestAvatar2\"}}]}}");
    }

//    @Test
//    public void testStep() throws JsonProcessingException {
//        assertThat(objectMapper.writeValueAsString(new Turn())).isEqualTo("{\"event\":\"EVENTS.GAME.TURN\"}");
//    }

    @Test
    public void testCreate() throws JsonProcessingException {
        final Town testTown = new Town(new Coordinates(0, 0), "TestTown");
        final Capital testCapital = new Capital(new Coordinates(0, 1), "TestCapital", new GamePlayer((long) 2, "TestOwner2"));
        testTown.setOwner(new GamePlayer((long) 1, "TestOwner"));
        testTown.generateSquads();
        testCapital.setSquad(null);
        testCapital.generateSquads();
        final Create create = new Create(testTown);
        System.out.println(objectMapper.writeValueAsString(create));
        assertThat(objectMapper.writeValueAsString(create)).isEqualTo("{\"event\":\"EVENTS.LOGIC.CREATE\",\"payload\":{\"count\":15,\"morale\":10,\"owner\":\"TestOwner\",\"position\":{\"x\":0,\"y\":0}}}");
        assertThat(objectMapper.writeValueAsString(new Create(testCapital))).isEqualTo("{\"event\":\"EVENTS.LOGIC.CREATE\",\"payload\":{\"count\":50,\"morale\":30,\"owner\":\"TestOwner2\",\"position\":{\"x\":0,\"y\":1}}}");
    }
}
