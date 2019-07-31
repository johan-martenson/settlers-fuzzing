package org.settlers.appland.settlers.fuzzing;

import org.appland.settlers.maps.MapFile;
import org.appland.settlers.maps.MapLoader;
import org.appland.settlers.model.Flag;
import org.appland.settlers.model.GameMap;
import org.appland.settlers.model.InvalidEndPointException;
import org.appland.settlers.model.Player;

import org.appland.settlers.model.Point;
import org.appland.settlers.model.Road;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class SettlersModelDriver {

    private static final String MAP_FILENAME = "/home/johan/projects/settlers-map-manager/maps/WORLDS/WELT01.SWD";

    public static void main( String[] args ) throws Exception {

        final String filename = args[0];

        System.out.println("Reading: " + filename);

        File file = new File(filename);

        BufferedReader br = new BufferedReader(new FileReader(file));

        /* Create game map */
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player 0", Color.BLUE));

        MapLoader mapLoader = new MapLoader();
        MapFile mapFile = mapLoader.loadMapFromFile(MAP_FILENAME);

        GameMap map = mapLoader.convertMapFileToGameMap(mapFile);

        map.setPlayers(players);

        /* Execute the commands from the input file */
        String st;
        while ((st = br.readLine()) != null) {
            System.out.println("Command and arguments: " + st);

            int firstSpace = st.indexOf(" ");
            String command = st.substring(0, firstSpace);
            String arguments = st.substring(firstSpace).strip();

            System.out.println("Command: " + command);
            System.out.println("Arguments: " + arguments);

                switch (command) {
                    case "BUILD_ROAD_AUTO":
                        try {
                            buildRoadAutomatic(map, arguments);
                        } catch (InvalidEndPointException e) {
                            System.out.println(e);
                        }
                        break;
                    case "DELETE_ROAD":
                        try {
                            deleteRoadAtPoint(map, arguments);
                        } catch (NullPointerException e) {
                            System.out.println(e);
                        }
                        break;
                    case "RAISE_FLAG":
                        try {
                            raiseFlag(map, arguments);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;
                    case "DELETE_FLAG":
                        try {
                            deleteFlagAtPoint(map, arguments);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;
                    case "FAST_FORWARD":
                        fastForward(map, arguments);
                        break;
                    default:
                        System.out.println("CAN'T HANDLE: '" + st + "'");
                        return;
                }
        }
    }

    private static void fastForward(GameMap map, String argumentsString) throws Exception {
        String[] arguments = argumentsString.split(" ");

        int iterations = Integer.parseInt(arguments[0]);

        if (iterations < 1 || iterations > 2000) {
            return;
        }

        System.out.println("Fast forward: " + iterations);

        for (int i = 0; i < iterations; i++) {

            map.stepTime();
            System.out.println(i);
        }
    }

    private static void deleteFlagAtPoint(GameMap map, String argumentsString) throws Exception {
        String[] arguments = argumentsString.split(" ");

        Point point = new Point(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));

        Flag flag = map.getFlagAtPoint(point);

        map.removeFlag(flag);
    }

    private static void raiseFlag(GameMap map, String argumentsString) throws Exception {
        String[] arguments = argumentsString.split(" ");

        int playerIndex = Integer.parseInt(arguments[0]);

        Point point = new Point(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]));

        Player player = map.getPlayers().get(playerIndex);

        map.placeFlag(player, point);

    }

    /**
     *
     * DELETE_ROAD ROAD_ID
     * @param map
     * @param argumentsString
     */
    private static void deleteRoadAtPoint(GameMap map, String argumentsString) throws Exception {
        String[] arguments = argumentsString.split(" ");

        Point point = new Point(Integer.parseInt(arguments[0]), Integer.parseInt(arguments[1]));

        Road road = map.getRoadAtPoint(point);

        map.removeRoad(road);
    }

    /**
     *
     * BUILD_ROAD_AUTO PLAYER_ID START.X START.Y END.X END.Y
     *
     * @param map
     * @param argumentsString
     */
    private static void buildRoadAutomatic(GameMap map, String argumentsString) throws Exception {
        String[] arguments = argumentsString.split(" ");

        int playerIndex = Integer.parseInt(arguments[0]);

        Point start = new Point(Integer.parseInt(arguments[1]), Integer.parseInt(arguments[2]));
        Point end = new Point(Integer.parseInt(arguments[3]), Integer.parseInt(arguments[4]));

        Player player = map.getPlayers().get(playerIndex);

        map.placeAutoSelectedRoad(player, start, end);
    }
}
