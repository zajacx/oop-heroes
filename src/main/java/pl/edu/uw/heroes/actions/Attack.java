package pl.edu.uw.heroes.actions;

import lombok.Getter;
import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.simulation.GameState;
import pl.edu.uw.heroes.units.Unit;

public class Attack extends UnitAction {

    @Getter
    private final Field aim;

    public Attack(Unit unit, Field aim) {
        super(unit);
        this.aim = aim;
    }

    public void execute(GameState gameState) {
        unit.doAttack(aim);
        if(!gameState.isAttackHappened() && aim.getUnit().getHealth() > 0) {
            aim.getUnit().doAttack(unit.getField());
            System.out.println(aim.getUnit().getName() + " counterattacks " + unit.getName() + "!");
        }
    }

    @Override
    public String toString() {
        return "Unit " + unit.getName() + " (" + unit.getArmySize() + ") attacks "
                + aim.getUnit().getName() + " (" + aim.getUnit().getArmySize() + ")";
    }

}
