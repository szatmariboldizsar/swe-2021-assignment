package boardgame;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Player {
    private String name;
    private int wins;

    public static List<Player> players = new ArrayList<>();

    public static Set<String> getNames() {
        var names = new HashSet<String>();
        for (var player : players) {
            names.add(player.getName());
        }
        return names;
    }

    public Player(String name, int wins) {
        this.name = name;
        this.wins = wins;
        players.add(this);
    }
}
