package pl.edu.uw.heroes.magic;

import lombok.AllArgsConstructor;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.simulation.GameState;

@AllArgsConstructor
public abstract class PlayerSpell implements Spell {

    protected final Player player;

    public void changeMagicState(Player player, GameState state) {
        if(player == state.getPlayerLeft()) {
            state.setPlayerLeftHasCastedASpell(true);
        } else {
            state.setPlayerRightHasCastedASpell(true);
        }
    }
}
