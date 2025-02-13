package org.scrum.psd.battleship.ascii;

import com.diogonunes.jcolor.Attribute;
import org.scrum.psd.battleship.controller.GameController;
import org.scrum.psd.battleship.controller.dto.Color;
import org.scrum.psd.battleship.controller.dto.Letter;
import org.scrum.psd.battleship.controller.dto.Position;
import org.scrum.psd.battleship.controller.dto.Ship;

import java.util.*;
import java.util.List;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;

public class Main {
    private static List<Ship> myFleet;
    private static List<Ship> enemyFleet;

    private static final Telemetry telemetry = new Telemetry();

    public static void main(String[] args) {
        telemetry.trackEvent("ApplicationStarted", "Technology", "Java");
        System.out.println(colorize("                                     |__", MAGENTA_TEXT()));
        System.out.println(colorize("                                     |\\/", MAGENTA_TEXT()));
        System.out.println(colorize("                                     ---", MAGENTA_TEXT()));
        System.out.println(colorize("                                     / | [", MAGENTA_TEXT()));
        System.out.println(colorize("                              !      | |||", MAGENTA_TEXT()));
        System.out.println(colorize("                            _/|     _/|-++'", MAGENTA_TEXT()));
        System.out.println(colorize("                        +  +--|    |--|--|_ |-", MAGENTA_TEXT()));
        System.out.println(colorize("                     { /|__|  |/\\__|  |--- |||__/", MAGENTA_TEXT()));
        System.out.println(colorize("                    +---------------___[}-_===_.'____                 /\\", MAGENTA_TEXT()));
        System.out.println(colorize("                ____`-' ||___-{]_| _[}-  |     |_[___\\==--            \\/   _", MAGENTA_TEXT()));
        System.out.println(colorize(" __..._____--==/___]_|__|_____________________________[___\\==--____,------' .7", MAGENTA_TEXT()));
        System.out.println(colorize("|                        Welcome to Battleship                         BB-61/", MAGENTA_TEXT()));
        System.out.println(colorize(" \\_________________________________________________________________________|", MAGENTA_TEXT()));
        System.out.println("");

        InitializeGame();

        StartGame();
    }

    private static void StartGame() {
        Scanner scanner = new Scanner(System.in);
        System.out.println();
        System.out.println(colorize("========================================================================", WHITE_BACK(), BLACK_TEXT()));
        System.out.println();

        System.out.print("\033[2J\033[;H");
        System.out.println("                  __");
        System.out.println("                 /  \\");
        System.out.println("           .-.  |    |");
        System.out.println("   *    _.-'  \\  \\__/");
        System.out.println("    \\.-'       \\");
        System.out.println("   /          _/");
        System.out.println("  |      _  /\" \"");
        System.out.println("  |     /_\'");
        System.out.println("   \\    \\_/");
        System.out.println("    \" \"\" \"\" \"\" \"");

        do {
            System.out.println("");
            System.out.println(colorize("Player, it's your turn", BOLD()));
            System.out.println(colorize("Enter coordinates for your shot (Eg A3):", WHITE_TEXT()));
            Position position = parsePosition(scanner.next());

            if (position == null) {
                System.out.println(colorize("Your input did not match a valid position", BLACK_BACK(), BRIGHT_RED_TEXT()));
                System.out.println();
                continue;
            }

            boolean isHit = GameController.checkIsHit(enemyFleet, position);
            if (isHit) {
                printExplosion();
            }

            System.out.println(isHit ? colorize("Yeah ! Nice hit !", YELLOW_TEXT()) : colorize("Miss", BLUE_TEXT()));
            telemetry.trackEvent("Player_ShootPosition", "Position", position.toString(), "IsHit", Boolean.valueOf(isHit).toString());

            position = getRandomPosition();
            isHit = GameController.checkIsHit(myFleet, position);
            String computerHitText = isHit ? colorize("hit your ship !", YELLOW_TEXT()) : colorize("misses", BLUE_TEXT());

            if (isHit) {
                printExplosion();
            }

            System.out.println("");
            System.out.println(String.format("Computer shoots in %s%s and %s", position.getColumn(), position.getRow(), computerHitText));
            telemetry.trackEvent("Computer_ShootPosition", "Position", position.toString(), "IsHit", Boolean.valueOf(isHit).toString());
        } while (true);
    }

    private static void beep() {
        System.out.print("\007");
    }

    protected static Position parsePosition(String input) {
        Letter letter = Letter.fromInput(input.substring(0, 1));

        if (letter == null) {
            return null;
        }

        try {
            int number = Integer.parseInt(input.substring(1));

            if (number < 1 || number > 8) {
                return null;
            }

            return new Position(letter, number);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private static void printExplosion() {
        beep();

        System.out.println(colorize("                \\         .  ./", BRIGHT_RED_TEXT()));
        System.out.println(colorize("              \\      .:\" \";'.:..\" \"   /", BRIGHT_RED_TEXT()));
        System.out.println(colorize("                  (M^^.^~~:.'\" \").", BRIGHT_RED_TEXT()));
        System.out.println(colorize("            -   (/  .    . . \\ \\)  -", BRIGHT_RED_TEXT()));
        System.out.println(colorize("               ((| :. ~ ^  :. .|))", BRIGHT_RED_TEXT()));
        System.out.println(colorize("            -   (\\- |  \\ /  |  /)  -", BRIGHT_RED_TEXT()));
        System.out.println(colorize("                 -\\  \\     /  /-", BRIGHT_RED_TEXT()));
        System.out.println(colorize("                   \\  \\   /  /", BRIGHT_RED_TEXT()));
    }

    private static Position getRandomPosition() {
        int rows = 8;
        int lines = 8;
        Random random = new Random();
        Letter letter = Letter.values()[random.nextInt(lines)];
        int number = random.nextInt(rows);
        Position position = new Position(letter, number);
        return position;
    }

    private static void InitializeGame() {
        InitializeMyFleet();

        InitializeEnemyFleet();
    }

    private static void InitializeMyFleet() {
        Scanner scanner = new Scanner(System.in);
        myFleet = GameController.initializeShips();

        System.out.println();
        System.out.println(colorize("========================================================================", WHITE_BACK(), BLACK_TEXT()));
        System.out.println(colorize("Please position your fleets (Game board has size from A to H and 1 to 8) :", BOLD(), BRIGHT_WHITE_BACK(), BRIGHT_BLACK_TEXT()));

        for (Ship ship : myFleet) {
            String shipNameColourised = colorize(ship.getName(), fromDtoColor(ship.getColor()));
            System.out.println("");
            System.out.println(String.format("Please enter the positions for the %s (size: %s)", shipNameColourised, ship.getSize()));
            for (int i = 1; i <= ship.getSize(); i++) {

                Position positionParsed = null;

                while (positionParsed == null) {
                    System.out.println(colorize(String.format("Enter position %s of %s (i.e A3):", i, ship.getSize()), WHITE_TEXT()));

                    String positionInput = scanner.next();
                    positionParsed = parsePosition(positionInput);

                    if (positionParsed == null) {
                        System.out.println(colorize("Your input did not match a valid position", BLACK_BACK(), BRIGHT_RED_TEXT()));
                        System.out.println();
                    }
                }

                ship.addPosition(positionParsed);
                telemetry.trackEvent("Player_PlaceShipPosition", "Position", positionParsed.toString(), "Ship", ship.getName(), "PositionInShip", Integer.valueOf(i).toString());
            }
        }
    }

    private static void InitializeEnemyFleet() {
        enemyFleet = GameController.initializeShips();

        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 4));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 5));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 6));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 7));
        enemyFleet.get(0).getPositions().add(new Position(Letter.B, 8));

        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 6));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 7));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 8));
        enemyFleet.get(1).getPositions().add(new Position(Letter.E, 9));

        enemyFleet.get(2).getPositions().add(new Position(Letter.A, 3));
        enemyFleet.get(2).getPositions().add(new Position(Letter.B, 3));
        enemyFleet.get(2).getPositions().add(new Position(Letter.C, 3));

        enemyFleet.get(3).getPositions().add(new Position(Letter.F, 8));
        enemyFleet.get(3).getPositions().add(new Position(Letter.G, 8));
        enemyFleet.get(3).getPositions().add(new Position(Letter.H, 8));

        enemyFleet.get(4).getPositions().add(new Position(Letter.C, 5));
        enemyFleet.get(4).getPositions().add(new Position(Letter.C, 6));
    }

    private static Attribute fromDtoColor(Color color) {
        switch(color) {
            case CADET_BLUE:
                return BRIGHT_BLUE_TEXT();
            case CHARTREUSE:
                return BRIGHT_WHITE_TEXT();
            case ORANGE:
                return YELLOW_TEXT();
            case RED:
                return RED_TEXT();
            case YELLOW:
                return BRIGHT_YELLOW_TEXT();
            default:
                throw new RuntimeException("Nani the fuck");
        }
    }
}
