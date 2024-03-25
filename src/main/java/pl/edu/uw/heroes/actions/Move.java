package pl.edu.uw.heroes.actions;

import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.simulation.GameState;
import pl.edu.uw.heroes.units.Unit;

public class Move extends UnitAction {

    private final Field destination;

    public Move(Unit unit, Field destination) {
        super(unit);
        this.destination = destination;
    }

    @Override
    public void execute(GameState gameState) {
        unit.doMove(destination);
    }

    @Override
    public String toString() {
        return "Unit " + unit.getName() + " (" + unit.getArmySize()
                + ") moves from (h = " + unit.getField().getPosition().height()
                + ", w = " + unit.getField().getPosition().width()
                + ") to (h = " + destination.getPosition().height()
                + ", w = " + destination.getPosition().width() + ")";
    }
}
