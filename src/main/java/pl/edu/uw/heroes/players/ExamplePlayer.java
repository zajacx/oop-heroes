package pl.edu.uw.heroes.players;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.edu.uw.heroes.actions.Action;
import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.magic.Spell;
import pl.edu.uw.heroes.units.Unit;

import java.util.*;

public class ExamplePlayer implements Player {

    @Getter
    private final String name;

    @Getter
    @Setter
    private int mana;

    @Getter
    private final int power;

    @Getter
    private final int luck;
    // 0 => 0,0% chance of double damage
    // 1 => 4,2% chance of double damage
    // 2 => 8,3% chance of double damage
    // 3 => 12,5% chance of double damage

    @Setter
    @Getter
    private int morale;
    // morale = 2 - # of fractions in player's army
    // 1 => 4,2% chance of an additional move
    // 0 => 0,0% chance of an additional move
    // -1 => 8,3% chance of losing a move
    // -2 => 16,7% chance of losing a move
    // -3 => 25,0% chance of losing a move

    @Setter
    @Getter
    private Collection<Unit> units = new ArrayList<>();

    private final Random random = new Random();

    @Getter
    @Setter
    private Player opponent;

    public ExamplePlayer(String name, int luck, int mana, int power) {
        this.name = name;
        this.luck = luck;
        this.mana = mana;
        this.power = power;
    }
    @Override
    public Action chooseAction(Collection<Action> actions) {
        // System.out.println("Available actions for " + name + ": ");
        // actions.forEach(System.out::println);
        // System.out.println("Choose action index from 0 to " + actions.size() + ":");
        // int actionNumber = scanner.nextInt();
        int actionNumber = random.nextInt(actions.size());

        return actions.stream()
                .skip(actionNumber)
                .findFirst()
                .get();
    }

    @Override
    public Spell chooseSpell(Collection<Spell> spells) {
        int spellNumber = random.nextInt(spells.size());

        return spells.stream()
                .skip(spellNumber)
                .findFirst()
                .get();
    }

    public int calculateMorale() {
        Collection<Unit> units = this.units;
        int morale = 2;
        Collection<String> fractionNames = new LinkedList<>();
        for(Unit unit : units) {
            if(!fractionNames.contains(unit.getFractionName())) {
                morale--;
                fractionNames.add(unit.getFractionName());
            }
        }
        return morale;
    }

    @Override
    public String toString() {
        return name;
    }


    // SPELLS:

    @Override
    public void castMagicArrow(Field aim) {
        Unit enemy = aim.getUnit();
        int totalDamage = (10 * power) + 20;
        int healthBefore = enemy.getHealth();
        if(healthBefore > totalDamage) {
            enemy.setHealth(healthBefore - totalDamage);
            enemy.setArmySize((int) Math.ceil(1.0 * enemy.getHealth() / enemy.getSingleHealth()));
        } else {
            enemy.setHealth(0);
            enemy.setArmySize(0);
        }
    }


}
