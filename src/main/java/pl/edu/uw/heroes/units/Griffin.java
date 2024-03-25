package pl.edu.uw.heroes.units;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.board.Field;

//@ToString
public class Griffin extends Unit {

    // IMMUTABLE:

    @Getter
    private final int baseAttack = 8, baseDefense = 8, baseSpeed = 6,
            singleArrows = 0, singleHealth = 25, minDamage = 3, maxDamage = 6;

    @Getter
    private final Player owner;

    @Getter
    private final boolean isFlying = true;

    @Getter
    private final String fractionName = "Castle";

    // MUTABLE DURING THE GAME:

    @Setter
    @Getter
    private int armySize, attack, defense, speed, arrows, health;
    // Attack, defense and speed are considered mutable using magic (or other (?) methods).


    private boolean hasWaitedInThisRound = false;

    public Griffin(int n, Player owner) {
        this.owner = owner;
        this.armySize = n;
        this.attack = baseAttack;
        this.defense = baseDefense;
        this.arrows = 0;
        this.health = singleHealth * n;
        this.speed = baseSpeed;
    }

    @Override
    public boolean canWait() {
        return !hasWaitedInThisRound;
    }

    @Override
    public void doWait() {
        hasWaitedInThisRound = true;
    }

    @Override
    public void doDefend() {
        defense = (int) Math.ceil(defense * 1.25);      // Default bonus.
    }

    @Override
    public void doAttack(Field aim) {
        Unit enemy = aim.getUnit();
        int luckBonus = calculateLuckBonus();

        int totalDamage;
        if(blessingCounter != 0) {
            totalDamage = (int) Math.round(calculateMaxArmyDamage(armySize, maxDamage) *
                    (1 + calculateSkillBonus(aim)) * (1 + luckBonus) * (1 - calculateSkillReduction(aim)));
        } else {
            totalDamage = (int) Math.round(calculateArmyDamage(armySize, minDamage, maxDamage) *
                    (1 + calculateSkillBonus(aim)) * (1 + luckBonus) * (1 - calculateSkillReduction(aim)));
        }

        int healthBefore = enemy.getHealth();
        if(healthBefore > totalDamage) {
            enemy.setHealth(healthBefore - totalDamage);
            enemy.setArmySize((int) Math.ceil(1.0 * enemy.getHealth() / enemy.getSingleHealth()));
        } else {
            enemy.setHealth(0);
            enemy.setArmySize(0);
        }

        if(luckBonus == 1) {
            System.out.println(this.getName() + " gets lucky - double attack!");
        }
    }

    private double calculateSkillBonus(Field aim) {
        if (attack > aim.getUnit().getDefense()) {
            double result = 0.05 * (attack - aim.getUnit().getDefense());
            return Math.min(result, 3.0);
        } else {
            return 0.0;
        }
    }

    private double calculateSkillReduction(Field aim) {
        if(attack < aim.getUnit().getDefense()) {
            double result = 0.025 * (aim.getUnit().getDefense() - attack);
            return Math.min(result, 0.7);   // stała Kwaśniewskiego?
        } else {
            return 0.0;
        }
    }

    @Override
    public void resetRound() {
        hasWaitedInThisRound = false;
        defense = baseDefense;
    }

    public String getName() {
        return "Griffin";
    }
}
