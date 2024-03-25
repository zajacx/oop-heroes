package pl.edu.uw.heroes.actions;

import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.simulation.GameState;
import pl.edu.uw.heroes.units.Unit;

public class Shoot extends UnitAction {

    private final Field aim;

    public Shoot(Unit unit, Field aim) {
        super(unit);
        this.aim = aim;
    }
    public void execute(GameState gameState) {
        unit.doAttack(aim);     // Although we use doAttack here, a shoot will be added to the list of actions
    }                           // only if arrows > 0, so there is no risk of invocating shoot for non-shooter unit.

    @Override
    public String toString() {
        return "Unit " + unit.getName() + " (" + unit.getArmySize() + ") shoots "
                + aim.getUnit().getName() + " (" + aim.getUnit().getArmySize() + ")";
    }
}
