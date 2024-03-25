package pl.edu.uw.heroes.simulation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.edu.uw.heroes.actions.*;
import pl.edu.uw.heroes.board.BFSCalculator;
import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.magic.Blessing;
import pl.edu.uw.heroes.magic.EmptySpell;
import pl.edu.uw.heroes.magic.MagicArrow;
import pl.edu.uw.heroes.magic.Spell;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.units.Unit;
import java.util.Random;

import java.util.*;
import java.util.stream.Stream;

@AllArgsConstructor
public class GameSimulation {

    private GameState state;

    private final BFSCalculator bfsCalculator = new BFSCalculator();

    public void executeOneAction() {
        Queue<Unit> units = state.getUnits();
        if (units.isEmpty())
            prepareNextRound();

        Unit unit = units.poll();
        int additionalMoveBonus = additionalMoveBonus(unit.getOwner());

        spellHelper(unit);

        if(additionalMoveBonus == 1) {
            executeHelper(unit);
            System.out.println("Great morale! " + unit.getOwner().getName() + " gets an additional move.");
            executeHelper(unit);
        } else if (additionalMoveBonus == 0) {
            executeHelper(unit);
        } else {
            System.out.println("Low morale... " + unit.getOwner().getName() + " loses the move.");
        }
    }

    public void executeHelper(Unit unit) {
        Collection<Action> actions = actionsForUnit(unit);
        Action action = unit.getOwner().chooseAction(actions);
        System.out.println(unit.getOwner() + " chose: " + action);
        action.execute(state);
        // After each action there can be dead units on board:
        Queue<Unit> newUnits = removeDeadUnits();
        state.setUnits(newUnits);
        state.getExecutedActions().add(action);
    }

    public void spellHelper(Unit unit) {
        Player player = unit.getOwner();
        if(player == state.getPlayerLeft()) {
            if(!state.isPlayerLeftHasCastedASpell()) {
                spellExecutor(player);
            }
        } else {
            if(!state.isPlayerRightHasCastedASpell()) {
                spellExecutor(player);
            }
        }
    }

    private void spellExecutor(Player player) {
        Collection<Spell> spells = spellsForPlayer(player);
        Spell spell = player.chooseSpell(spells);
        if(!spell.toString().equals("Empty Spell")) {
            System.out.println(player.getName() + " casts a spell: " + spell);
        }
        spell.execute(state);
        Queue<Unit> newUnits = removeDeadUnits();
        state.setUnits(newUnits);
    }

    private int additionalMoveBonus(Player player) {
        // morale = 2 - # of fractions in player's army
        // 1 => 4,2% chance of an additional move
        // 0 => 0,0% chance of an additional move
        // -1 => 8,3% chance of losing a move
        // -2 => 16,7% chance of losing a move
        // -3 => 25,0% chance of losing a move
        int level1 = 42, level0 = 0, levelNeg1 = 83, levelNeg2 = 167, levelNeg3 = 250;

        Random random = new Random();
        int number = random.nextInt(0, 1000);
        int morale = player.getMorale();

        switch (morale) {
            case(1) -> {
                if (number < level1) {
                    return 1;
                } else {
                    return 0;
                }
            }
            case(0) -> {
                return 0;
            }
            case(-1) -> {
                if (number < levelNeg1) {
                    return -1;
                } else {
                    return 0;
                }
            }
            case(-2) -> {
                if (number < levelNeg2) {
                    return -1;
                } else {
                    return 0;
                }
            }
            case(-3) -> {
                if (number < levelNeg3) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }

    private Queue<Unit> removeDeadUnits() {
        Queue<Unit> newUnits = new LinkedList<>();
        while(!state.getUnits().isEmpty()) {
            Unit unit = state.getUnits().poll();
            if (unit.getHealth() > 0 && unit.getArmySize() > 0) {
                newUnits.add(unit); // Add the unit back to the queue.
            } else {
                state.getPlayerLeft().getUnits().remove(unit);
                state.getPlayerRight().getUnits().remove(unit);
            }
        }
        return newUnits;
    }

    private void prepareNextRound() {
        // Bonuses and magic fixes:
        state.setAttackHappened(false);
        state.setPlayerLeftHasCastedASpell(false);
        state.setPlayerRightHasCastedASpell(false);
        for(Unit unit : state.getUnits()) {
            int blessingCounter = unit.getBlessingCounter();
            if(blessingCounter != 0) {
                unit.setBlessingCounter(blessingCounter - 1);
            }
        }

        Queue<Unit> units = state.getUnits();
        Stream.concat(
                        state.getPlayerLeft().getUnits().stream(),
                        state.getPlayerRight().getUnits().stream()
                )
                .peek(Unit::resetRound)
                .sorted((u1, u2) -> Integer.compare(u2.getSpeed(), u1.getSpeed()))
                .forEach(units::add);
        if (units.isEmpty())
            throw new IllegalStateException("No units on board!");
    }

    private Collection<Action> actionsForUnit(Unit unit) {
        Collection<Action> actions = new ArrayList<>();

        // DEFENSE:
        actions.add(new Defend(unit));

        // WAITING:
        if (unit.canWait()) {
            actions.add(new Wait(unit));
        }

        // ATTACK ON A NEIGHBORING UNIT:
        Collection<Field> neighboringFields = unit.getField().getNeighbors();
        neighboringFields.stream()
                .filter(field -> !field.isEmpty())
                .forEach(field -> actions.add(new Attack(unit, field)));


        // MOVE TO A PARTICULAR FIELD:
        bfsCalculator.calculatePossibleMoves(unit.getField())
                .forEach(field -> actions.add(new Move(unit, field)));

        // MOVE TO A PARTICULAR FIELD AND ATTACK ON A NEIGHBORING UNIT:
        Collection<Field> reachableFields = bfsCalculator.calculatePossibleMoves(unit.getField());
        Collection<Unit> opponentUnits = unit.getOwner().getOpponent().getUnits();
        for(Unit opponentUnit : opponentUnits) {
            Collection<Field> enemyNeighboringFields = opponentUnit.getField().getNeighbors();
            enemyNeighboringFields.stream()
                    .filter(reachableFields::contains)
                    .forEach(field -> actions.add(new MoveAndAttack(unit, field, opponentUnit.getField())));
        }

        // SHOT TO A PARTICULAR UNIT OF THE OPPONENT:
        if(unit.getArrows() != 0) {
            opponentUnits.forEach(opponentUnit -> actions.add(new Shoot(unit, opponentUnit.getField())));
        }

        return actions;
    }

    private Collection<Spell> spellsForPlayer(Player player) {
        Collection<Spell> spells = new ArrayList<>();
        Collection<Unit> opponentUnits = player.getOpponent().getUnits();

        // EMPTY SPELL:
        spells.add(new EmptySpell(player));

        // MAGIC ARROW:
        if(player.getMana() >= 5) {
            opponentUnits.forEach(opponentUnit -> spells.add(new MagicArrow(player, opponentUnit.getField())));
        }

        // BLESSING:
        if(player.getMana() >= 5) {
            player.getUnits().forEach(unit -> spells.add(new Blessing(player, unit, player.getPower())));
        }

        return spells;
    }

    public void printState() {
        System.out.println("Player left (" + state.getPlayerLeft() + "): ");
        for (Unit unit : state.getPlayerLeft().getUnits()) {
            System.out.println(unit.getName()
                    + " | position: height = " + unit.getField().getPosition().height()
                    + ", width = " + unit.getField().getPosition().width()
                    + " | health: " + unit.getHealth()
                    + " | army size: " + unit.getArmySize()
                    + " | arrows: " + unit.getArrows());
        }
        System.out.println("Player right (" + state.getPlayerRight() + "): ");
        for (Unit unit : state.getPlayerRight().getUnits()) {
            System.out.println(unit.getName()
                    + " | position: height = " + unit.getField().getPosition().height()
                    + ", width = " + unit.getField().getPosition().width()
                    + " | health: " + unit.getHealth()
                    + " | army size: " + unit.getArmySize()
                    + " | arrows: " + unit.getArrows());
        }
    }
}
