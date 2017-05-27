package game;

import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.base.Coordinates;
import com.hexandria.mechanics.events.logic.Move;
import com.hexandria.mechanics.player.GamePlayer;
import com.hexandria.mechanics.base.Squad;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by frozenfoot on 16.05.17.
 */
public class GameTest {

    public final List<GamePlayer> players;

    public GameTest(){
        this.players = new LinkedList<>();
        players.add(new GamePlayer((long) 1, "User1"));
        players.add(new GamePlayer((long) 2, "User2"));
    }

    @Test
    public void mergeTest(){
        final Game game = new Game(players);
        game.getMap()[2][1].setSquad(new Squad(15, 15, players.get(0)));
        game.getMap()[1][1].setSquad(new Squad(15, 15, players.get(1)));
        game.mergeSquads(
                game.getMap()[2][1],
                game.getMap()[1][1]);
        assertThat(game.getMap()[1][1].getSquad().getCount()).isEqualTo(30);
        assertThat(game.getMap()[2][1].getSquad()).isEqualTo(null);
    }

    @Test
    public void wrongPlayerTest(){
        final Game game = new Game(players);
        final boolean result = game.validate(2L, new Move(new Move.Payload(new Coordinates(0, 0), new Coordinates(0, 1))));
        assertThat(result).isEqualTo(false);
        game.finishTurn();
        assertThat(game.validate(
                2L,
                new Move(new Move.Payload(new Coordinates(0, 0), new Coordinates(0, 1)
                )))).isEqualTo(true);
    }

    @Test
    public void positionValidate(){
        final Game game = new Game(players);
        boolean result = game.validate(1L,
                new Move(new Move.Payload(new Coordinates(0, 0), new Coordinates(0, 1)
        )));
        assertThat(result).isEqualTo(true);
        result = game.validate(1L,
                new Move(new Move.Payload(new Coordinates(0, 0), new Coordinates(0, 2)
                )));
        assertThat(result).isEqualTo(false);
    }

    @Test
    public void nonNegativeCoordinates(){
        final Game game = new Game(players);
        final boolean result = game.validate(1L,
                new Move(new Move.Payload(new Coordinates(-1, 0), new Coordinates(0, 1)
                )));
        assertThat(result).isEqualTo(false);
    }
}
