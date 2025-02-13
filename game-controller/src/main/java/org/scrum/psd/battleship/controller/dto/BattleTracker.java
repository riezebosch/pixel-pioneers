package org.scrum.psd.battleship.controller.dto;

import java.util.ArrayList;
import java.util.List;

public class BattleTracker {
    private List<Ship> sunkShips = new ArrayList<>();

    public void checkAndMarkSunkShip(Ship ship, List<Position> hitPositions) {
        if (ship.isSunk(hitPositions) && !sunkShips.contains(ship)) {
            sunkShips.add(ship);
            System.out.println("your ship has sunk: " + ship.getName());
        }
    }

    public List<Ship> getSunkShips() {
        return sunkShips;
    }
}
