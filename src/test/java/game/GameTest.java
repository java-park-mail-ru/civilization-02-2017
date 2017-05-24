package game;

import com.hexandria.mechanics.Game;
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
    @Test
    public void mergeTest(){
        final List<GamePlayer> players = new LinkedList<>();
        players.add(new GamePlayer((long) 1, "User1"));
        players.add(new GamePlayer((long) 2, "User2"));
        final Game game = new Game(players);
        game.getMap()[2][1].setSquad(new Squad(15, 15, players.get(0)));
        game.getMap()[1][1].setSquad(new Squad(15, 15, players.get(1)));
        game.mergeSquads(
                game.getMap()[2][1],
                game.getMap()[1][1]);
        assertThat(game.getMap()[1][1].getSquad().getCount()).isEqualTo(30);
        assertThat(game.getMap()[2][1].getSquad()).isEqualTo(null);
    }
}
