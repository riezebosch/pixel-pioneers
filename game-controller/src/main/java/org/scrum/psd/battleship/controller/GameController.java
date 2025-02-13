package org.scrum.psd.battleship.controller;

import org.scrum.psd.battleship.controller.dto.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class GameController {
    public static boolean checkIsHit(Collection<Ship> ships, Position shot, BattleTracker battleTracker, List<Position> hitPositions) {
        if (ships == null) {
            throw new IllegalArgumentException("ships is null");
        }

        if (shot == null) {
            throw new IllegalArgumentException("shot is null");
        }

        for (Ship ship : ships) {
            for (Position position : ship.getPositions()) {
                if (position.equals(shot)) {
                    hitPositions.add(shot); // Voeg de hit toe aan de lijst
                    System.out.println("Hit!");

                    // Controleer of het schip gezonken is
                    battleTracker.checkAndMarkSunkShip(ship, hitPositions);

                    return true;
                }
            }
        }

        System.out.println("Miss!");
        return false;
    }


    public static List<Ship> initializeShips() {
        return Arrays.asList(
                new Ship("Aircraft Carrier", 5, Color.CADET_BLUE),
                new Ship("Battleship", 4, Color.RED),
                new Ship("Submarine", 3, Color.CHARTREUSE),
                new Ship("Destroyer", 3, Color.YELLOW),
                new Ship("Patrol Boat", 2, Color.ORANGE));
    }

    public static boolean isShipValid(Ship ship) {
        return ship.getPositions().size() == ship.getSize();
    }

    public static Position getRandomPosition(int size) {
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(size)];
        int number = random.nextInt(size);
        Position position = new Position(letter, number);
        return position;
    }
}
