package org.appland.settlers.utils;

import org.appland.settlers.model.Building;
import org.appland.settlers.model.Flag;
import org.appland.settlers.model.GameMap;
import org.appland.settlers.model.Player;
import org.appland.settlers.model.Point;

public class TestCaseGenerator {
    public void recordPlaceBuilding(Building building, Point point) {
        printStartTry();

        System.out.println(
                "map.placeBuilding(" +
                    "new " + building.getClass().getName() + "(player)" +
                    ", " + printPoint(point) +
                ");"
        );

        System.out.println();

        printEndTry();
    }

    private String printPoint(Point point) {
        return "new Point(" + point.x + ", " + point.y + ")";
    }

    public void recordPlaceFlag(Player player, Point point) {
        printStartTry();

        System.out.println("map.placeFlag(player, " + printPoint(point) + ");");

        System.out.println();

        printEndTry();
    }

    public void recordCallScout(Flag flag) {
        printStartTry();

        System.out.println("map.getFlagAtPoint(" + printPoint(flag.getPosition()) + ").callScout();");

        System.out.println();

        printEndTry();
    }


    public void recordCallGeologist(Flag flag) {
        printStartTry();

        System.out.println("map.getFlagAtPoint(" + printPoint(flag.getPosition()) + ").callGeologist();");

        System.out.println();

        printEndTry();
    }

    public void recordPlaceRoadAutomatically(Player player, Point start, Point end) {
        printStartTry();

        System.out.println(
                "map.placeAutoSelectedRoad(player, " +
                    printPoint(start) + ", " +
                    printPoint(end) +
                ");");

        System.out.println();

        printEndTry();
    }

    public void recordFastForward(int iterations, GameMap map) {
        printStartTry();

        System.out.println("Utils.fastForward(" + iterations + ", map);");

        System.out.println();

        printEndTry();
    }

    public void recordRemoveFlag(Flag flag) {
        printStartTry();

        System.out.println(
                "map.removeFlag(" +
                    "map.getFlagAtPoint(" + printPoint(flag.getPosition()) +
                "));"
        );

        System.out.println();

        printEndTry();
    }

    private void printEndTry() {
        System.out.println("} catch (Exception e) {e.printStackTrace();}");
    }

    private void printStartTry() {
        System.out.println("try {");
    }
}
