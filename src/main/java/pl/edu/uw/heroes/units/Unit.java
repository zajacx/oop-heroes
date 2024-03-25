package pl.edu.uw.heroes.units;

import lombok.Getter;
import lombok.Setter;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.board.BFSCalculator;

import java.util.Random;

public abstract class Unit {

    protected BFSCalculator bfsCalculator = new BFSCalculator();

    @Getter
    protected Field field = null;

    @Getter
    @Setter
    protected int blessingCounter = 0;

    public abstract int getBaseAttack();
    public abstract int getBaseDefense();
    public abstract int getBaseSpeed();
    public abstract int getSingleArrows();
    public abstract int getSingleHealth();
    abstract int getMinDamage();
    abstract int getMaxDamage();

    public abstract Player getOwner();

    public abstract int getSpeed();

    public abstract int getAttack();

    public abstract int getDefense();

    public abstract int getArmySize();

    public abstract int getHealth();

    public abstract String getName();

    public abstract String getFractionName();

    public abstract boolean canWait();

    public abstract void doWait();

    public abstract void doDefend();

    public abstract void doAttack(Field aim);

    public abstract void setArmySize(int n);

    public abstract void setHealth(int n);

    public abstract void setArrows(int n);

    public abstract void resetRound();

    public abstract boolean isFlying();

    public abstract int getArrows();

    public void doMove(Field destination) {
        if (field != null)
            field.setUnit(null);
        field = destination;
        destination.setUnit(this);
    }

    protected int calculateArmyDamage(int armySize, int minDamage, int maxDamage) {
        int damage = 0;
        Random random = new Random();
        if(armySize < 10) {
            for(int i = 0; i < armySize; i++)
                damage +=  random.nextInt(minDamage, maxDamage + 1);
        } else {
            for(int i = 0; i < 10; i++)
                damage +=  random.nextInt(minDamage, maxDamage + 1);
            damage = damage * armySize / 10;
        }
        return damage;
    }

    protected int calculateMaxArmyDamage(int armySize, int maxDamage) {
        return armySize * maxDamage;
    }

    int calculateLuckBonus() {
        // 0 => 0,0% chance of double damage
        // 1 => 4,2% chance of double damage
        // 2 => 8,3% chance of double damage
        // 3 => 12,5% chance of double damage
        int level0 = 0, level1 = 42, level2 = 83, level3 = 125;
        Random random = new Random();
        int luck = getOwner().getLuck();
        int number = random.nextInt(0, 1000);
        switch (luck) {
            case 3 -> {
                if (number < level3) {
                    return 1;
                } else {
                    return 0;
                }
            }
            case 2 -> {
                if (number < level2) {
                    return 1;
                } else {
                    return 0;
                }
            }
            case 1 -> {
                if (number < level1) {
                    return 1;
                } else {
                    return 0;
                }
            }
            case 0 -> {
                if (number < level0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return 0;
    }
}
