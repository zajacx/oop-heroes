package pl.edu.uw.heroes.board;

import pl.edu.uw.heroes.units.Unit;

import java.util.*;
import java.util.stream.Collectors;

public class BFSCalculator {

    public Collection<Field> calculatePossibleMoves(Field start) {
        Set<Field> visited = new HashSet<>();
        List<Field> currentLevel = new ArrayList<>();
        List<Field> nextLevel = new ArrayList<>();

        visited.add(start);
        currentLevel.add(start);
        for (int movesLeft = start.getUnit().getSpeed(); movesLeft > 0; movesLeft--) {
            for (Field field : currentLevel) {
                for (Field neighbor : field.getNeighbors()) {
                    if ((neighbor.isEmpty() || start.getUnit().isFlying()) && !visited.contains(neighbor)) {
                        visited.add(neighbor);
                        nextLevel.add(neighbor);
                    }
                }
            }
            currentLevel = nextLevel;
            nextLevel = new ArrayList<>();
        }

        visited.remove(start);
        return visited.stream()
                .filter(Field::isEmpty)
                .collect(Collectors.toSet());
    }

    public int calculateDistance(Field start, Field finish) {
        if (start == finish) {
            return 0;
        }

        Set<Field> visited = new HashSet<>();
        List<Field> currentLevel = new ArrayList<>();
        List<Field> nextLevel = new ArrayList<>();

        int distance = 1;

        visited.add(start);
        currentLevel.add(start);
        while (!currentLevel.isEmpty()) {
            for (Field field : currentLevel) {
                for (Field neighbor : field.getNeighbors()) {
                    if (!visited.contains(neighbor)) {
                        if (neighbor == finish) {
                            return distance;
                        }
                        visited.add(neighbor);
                        nextLevel.add(neighbor);
                    }
                }
            }
            currentLevel = nextLevel;
            nextLevel = new ArrayList<>();
            distance++;
        }
        return distance;    // Never reached.
    }

}
