package game;

import com.hexandria.mechanics.Game;
import com.hexandria.mechanics.avatar.UserAvatar;
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
        final List<UserAvatar> players = new LinkedList<>();
        players.add(new UserAvatar((long) 1, "User1"));
        players.add(new UserAvatar((long) 2, "User2"));
        final Game game = new Game(players);
        game.mergeSquads(
                game.getMap()[2][1],
                game.getMap()[1][1]);
        assertThat(game.getMap()[1][1].getSquad().getCount()).isEqualTo(30);
        assertThat(game.getMap()[2][1].getSquad()).isEqualTo(null);
    }
}
