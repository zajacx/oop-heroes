package pl.edu.uw.heroes;

import pl.edu.uw.heroes.board.Board;
import pl.edu.uw.heroes.board.Position;
import pl.edu.uw.heroes.players.ExamplePlayer;
import pl.edu.uw.heroes.players.Player;
import pl.edu.uw.heroes.simulation.GameSimulation;
import pl.edu.uw.heroes.simulation.GameState;
import pl.edu.uw.heroes.units.*;

public class Main {

    public static void main(String[] args) {
        Player playerLeft = new ExamplePlayer("Żółw", 3, 30, 6);
        Unit playerLeftUnit1 = new Archer(10, playerLeft);
        playerLeft.getUnits().add(playerLeftUnit1);
        Unit playerLeftUnit2 = new Griffin(5, playerLeft);
        playerLeft.getUnits().add(playerLeftUnit2);
        Unit playerLeftUnit3 = new Pikeman(20, playerLeft);
        playerLeft.getUnits().add(playerLeftUnit3);
        Unit playerLeftUnit4 = new Archer(10, playerLeft);

        Player playerRight = new ExamplePlayer("Zając", 2, 20, 4);
        Unit playerRightUnit1 = new GreenDragon(3, playerRight);
        playerRight.getUnits().add(playerRightUnit1);
        Unit playerRightUnit2 = new Lich(20, playerRight);
        playerRight.getUnits().add(playerRightUnit2);
        Unit playerRightUnit3 = new Griffin(12, playerRight);
        playerRight.getUnits().add(playerRightUnit3);
        Unit playerRightUnit4 = new Archer(7, playerRight);
        playerRight.getUnits().add(playerRightUnit4);

        playerLeft.setOpponent(playerRight);
        playerRight.setOpponent(playerLeft);

        playerLeft.setMorale(playerLeft.calculateMorale());
        playerRight.setMorale(playerRight.calculateMorale());

        GameState gameState = new GameState(playerLeft, playerRight, new Board(10, 10));

        playerLeftUnit1.doMove(gameState.getBoard().getField(new Position(0, 0)));
        playerLeftUnit2.doMove(gameState.getBoard().getField(new Position(3, 0)));
        playerLeftUnit3.doMove(gameState.getBoard().getField(new Position(6, 0)));
        playerLeftUnit4.doMove(gameState.getBoard().getField(new Position(9, 9)));
        playerRightUnit1.doMove(gameState.getBoard().getField(new Position(2, 9)));
        playerRightUnit2.doMove(gameState.getBoard().getField(new Position(4, 9)));
        playerRightUnit3.doMove(gameState.getBoard().getField(new Position(6, 9)));
        playerRightUnit4.doMove(gameState.getBoard().getField(new Position(8, 9)));


        GameSimulation gameSimulation = new GameSimulation(gameState);
        System.out.println("You can check all available actions in each move by decommenting proper lines in ExamplePlayer :)");

        while(!playerLeft.getUnits().isEmpty() && !playerRight.getUnits().isEmpty()) {
            gameSimulation.executeOneAction();
            System.out.println("\n");
            gameSimulation.printState();
            System.out.println("\n");
        }

        if(playerLeft.getUnits().isEmpty()) {
            System.out.println(playerRight.getName() + " won!");
        } else {
            System.out.println(playerLeft.getName() + " won!");
        }
    }
}

