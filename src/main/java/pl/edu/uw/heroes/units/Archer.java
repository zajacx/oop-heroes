package pl.edu.uw.heroes.units;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.board.Field;
import java.util.Random;

@ToString
public class Archer extends Unit {

    // IMMUTABLE:

    @Getter
    private final int baseAttack = 6, baseDefense = 3, baseSpeed = 4,
            singleArrows = 12, singleHealth = 10, minDamage = 2, maxDamage = 3;

    @Getter
    private final Player owner;

    @Getter
    private final boolean isFlying = false;

    @Getter
    private final String fractionName = "Castle";

    // MUTABLE DURING THE GAME:

    @Setter
    @Getter
    private int armySize, attack, defense, speed, arrows, health;
    // Attack, defense and speed are considered mutable using magic (or other (?) methods).


    private boolean hasWaitedInThisRound = false;

    public Archer(int n, Player owner) {
        this.owner = owner;
        this.armySize = n;
        this.attack = baseAttack;
        this.defense = baseDefense;
        this.arrows = singleArrows * n;
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
        // CASE 1: Shooter attacks neighboring enemy => can't shoot, half of total damage,
        // but without arrow loss.
        // CASE 2: The enemy is too far away (distance > 10) => half of total damage, arrow loss.
        // CASE 3: The enemy is closer (1 < distance <= 10) => normal damage, arrow loss.

        Unit enemy = aim.getUnit();
        int distance = bfsCalculator.calculateDistance(field, aim);

        int luckBonus = calculateLuckBonus();

        int totalDamage;

        if(blessingCounter != 0) {
            totalDamage = (int) Math.round(calculateMaxArmyDamage(armySize, maxDamage) *
                    (1 + calculateSkillBonus(aim)) * (1 + luckBonus) * (1 - calculateSkillReduction(aim)));
        } else {
            totalDamage = (int) Math.round(calculateArmyDamage(armySize, minDamage, maxDamage) *
                    (1 + calculateSkillBonus(aim)) * (1 + luckBonus) * (1 - calculateSkillReduction(aim)));
        }


        if (distance == 1 || distance > 10)
            totalDamage = totalDamage / 2;

        int healthBefore = enemy.getHealth();
        if(healthBefore > totalDamage) {
            enemy.setHealth(healthBefore - totalDamage);
            enemy.setArmySize((int) Math.ceil(1.0 * enemy.getHealth() / enemy.getSingleHealth()));
        } else {
            enemy.setHealth(0);
            enemy.setArmySize(0);
        }

        if (distance > 1) {
            arrows -= armySize;
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
        return "Archer";
    }
}