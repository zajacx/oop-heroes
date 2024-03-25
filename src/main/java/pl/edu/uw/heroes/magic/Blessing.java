package pl.edu.uw.heroes.magic;

import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.units.Unit;
import pl.edu.uw.heroes.simulation.GameState;

public class Blessing extends PlayerSpell {
    private final Unit unit;

    private final int numberOfRounds;

    public Blessing(Player player, Unit unit, int numberOfRounds) {
        super(player);
        this.unit = unit;
        this.numberOfRounds = numberOfRounds;
    }

    @Override
    public void execute(GameState gameState) {
        unit.setBlessingCounter(numberOfRounds);
        int mana = player.getMana();
        player.setMana(mana - 5);
        changeMagicState(player, gameState);
    }

    @Override
    public String toString() {
        return "Blessing (for unit: " + unit.getName() + " (" + unit.getArmySize() + "))";
    }
}
