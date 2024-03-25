package pl.edu.uw.heroes.actions;

import lombok.Getter;
import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.simulation.GameState;
import pl.edu.uw.heroes.units.Unit;

public class MoveAndAttack extends UnitAction {

    private final Field fieldToMove;

    @Getter
    private final Field fieldToAttack;

    public MoveAndAttack(Unit unit, Field fieldToMove, Field fieldToAttack) {
        super(unit);
        this.fieldToMove = fieldToMove;
        this.fieldToAttack = fieldToAttack;
    }

    public void execute(GameState gameState) {
        unit.doMove(fieldToMove);
        unit.doAttack(fieldToAttack);
        if(!gameState.isAttackHappened()) {
            fieldToAttack.getUnit().doAttack(unit.getField());
            System.out.println(fieldToAttack.getUnit().getName() + " counterattacks " + unit.getName() + "!");
        }
    }

    @Override
    public String toString() {
        return "Unit " + unit.getName() + " (" + unit.getArmySize()
                + ") moves from (h = " + unit.getField().getPosition().height()
                + ", w = " + unit.getField().getPosition().width()
                + ") to (h = " + fieldToMove.getPosition().height()
                + ", w = " + fieldToMove.getPosition().width() + ") and attacks "
                + fieldToAttack.getUnit().getName() + " ("
                + fieldToAttack.getUnit().getArmySize() + ")";
    }
}
