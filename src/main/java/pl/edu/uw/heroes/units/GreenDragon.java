package pl.edu.uw.heroes.units;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import pl.edu.uw.heroes.board.Direction;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.board.Field;

import java.util.Map;

@ToString
public class GreenDragon extends Unit {

    // IMMUTABLE:

    @Getter
    private final int baseAttack = 18, baseDefense = 18, baseSpeed = 10,
            singleArrows = 0, singleHealth = 180, minDamage = 40, maxDamage = 50;

    @Getter
    private final Player owner;

    @Getter
    private final boolean isFlying = true;

    @Getter
    private final String fractionName = "Bastion";

    // MUTABLE DURING THE GAME:

    @Setter
    @Getter
    private int armySize, attack, defense, speed, arrows, health;
    // Attack, defense and speed are considered mutable using magic (or other (?) methods).


    private boolean hasWaitedInThisRound = false;

    public GreenDragon(int n, Player owner) {
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
        // We find a field that is perfectly behind 'enemy':

        int luckBonus = calculateLuckBonus();

        int totalDamage;
        if(blessingCounter != 0) {
            totalDamage = (int) Math.round(calculateMaxArmyDamage(armySize, maxDamage) *
                    (1 + calculateSkillBonus(aim)) * (1 + luckBonus) * (1 - calculateSkillReduction(aim)));
        } else {
            totalDamage = (int) Math.round(calculateArmyDamage(armySize, minDamage, maxDamage) *
                    (1 + calculateSkillBonus(aim)) * (1 + luckBonus) * (1 - calculateSkillReduction(aim)));
        }

        Direction direction = getAimDirection(aim);
        Field secondAim = aim.getNeigborsMap().get(direction);

        // We assume that dragon only attacks a unit on 'aim' field and behind it, no further.

        slay(aim, totalDamage);
        if(secondAim != null && secondAim.getUnit() != null && secondAim.getUnit().getOwner() == this.owner.getOpponent()) {
            slay(secondAim, totalDamage);
        }

        if(luckBonus == 1) {
            System.out.println(this.getName() + " gets lucky - double attack!");
        }
    }

    private void slay(Field aim, int totalDamage) {
        Unit enemy = aim.getUnit();
        int healthBefore = enemy.getHealth();
        if(healthBefore > totalDamage) {
            enemy.setHealth(healthBefore - totalDamage);
            enemy.setArmySize((int) Math.ceil(1.0 * enemy.getHealth() / enemy.getSingleHealth()));
        } else {
            enemy.setHealth(0);
            enemy.setArmySize(0);
        }
    }

    private Direction getAimDirection(Field aim) {
        Direction direction;
        Map<Direction, Field> fields = field.getNeigborsMap();
        for (Map.Entry<Direction, Field> entry : fields.entrySet()) {
            if (entry.getValue() == aim) {
                return entry.getKey();
            }
        }
        return null;
    }

    private double calculateSkillBonus(Field aim) {
        assert (aim != null);
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
            return Math.min(result, 0.7);
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
        return "Green Dragon";
    }
}

