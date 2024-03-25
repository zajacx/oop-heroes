package pl.edu.uw.heroes.simulation;

import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.actions.Action;
import pl.edu.uw.heroes.board.Board;
import pl.edu.uw.heroes.units.Unit;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameState {

    @Getter
    private final Player playerLeft, playerRight;

    @Getter
    private final Board board;

    @Getter
    private List<Action> executedActions = new LinkedList<>();

    @Setter
    @Getter
    private boolean attackHappened = false;

    @Setter
    @Getter
    private boolean playerLeftHasCastedASpell = false;

    @Setter
    @Getter
    private boolean playerRightHasCastedASpell = false;

    @Setter
    @Getter
    private Queue<Unit> units = new LinkedList<>();

    public GameState(Player playerLeft, Player playerRight, Board board) {
        this.playerLeft = playerLeft;
        this.playerRight = playerRight;
        this.board = board;
    }
}
