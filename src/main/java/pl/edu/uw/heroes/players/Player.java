package pl.edu.uw.heroes.players;

import pl.edu.uw.heroes.actions.Action;
import pl.edu.uw.heroes.board.Field;
import pl.edu.uw.heroes.magic.Spell;
import pl.edu.uw.heroes.units.Unit;

import java.util.Collection;

public interface Player {

    Collection<Unit> getUnits();

    void setOpponent(Player player);
    Player getOpponent();

    String getName();

    int getLuck();

    int getPower();

    int getMorale();

    int getMana();

    void setMorale(int morale);

    void setUnits(Collection<Unit> units);

    void setMana(int magicPoints);

    int calculateMorale();

    Action chooseAction(Collection<Action> actions);

    Spell chooseSpell(Collection<Spell> spells);

    void castMagicArrow(Field aim);
}
