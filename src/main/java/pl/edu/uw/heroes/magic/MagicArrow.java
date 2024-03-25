package pl.edu.uw.heroes.magic;

import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.simulation.GameState;

public class MagicArrow extends PlayerSpell {

    private final Field aim;

    public MagicArrow(Player player, Field aim) {
        super(player);
        this.aim = aim;
    }

    @Override
    public void execute(GameState gameState) {
        player.castMagicArrow(aim);
        int mana = player.getMana();
        player.setMana(mana - 5);
        changeMagicState(player, gameState);
    }

    @Override
    public String toString() {
        return "Magic Arrow (into: " + aim.getUnit().getName() + " (" + aim.getUnit().getArmySize() + "))";
    }

}
