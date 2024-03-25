package pl.edu.uw.heroes.magic;

import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.simulation.GameState;

public class EmptySpell extends PlayerSpell{

    // During a round, if a player can cast a spell but doesn't want to, can use an empty spell to do nothing.

    public EmptySpell(Player player) {
        super(player);
    }

    @Override
    public void execute(GameState gameState) {}

    @Override
    public String toString() {
        return "Empty Spell";
    }

}
