package org.appland.settlers.fuzzing;

import org.appland.settlers.maps.MapFile;
import org.appland.settlers.maps.MapLoader;
import org.appland.settlers.model.*;

import java.awt.Color;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class SettlersModelDriver {

    private static final String MAP_FILENAME = "/home/johan/projects/settlers-map-manager/maps/WORLDS/WELT01.SWD";
    //private static final String MAP_FILENAME = "/home/johan/projects/settlers-map-manager/maps/WORLDS/WELT02.SWD";

    private static final Map<Integer, Class<? extends Building>> buildingClassMap = new HashMap<>();
    private static final Map<Integer, Tile.Vegetation> vegetationMap = new HashMap<>();

    public static void main( String[] args ) throws Exception {

        final String filename = args[0];

        System.out.println("Reading: " + filename);

        File file = new File(filename);

        //BufferedReader br = new BufferedReader(new FileReader(file));
        FileInputStream inputStream = new FileInputStream(file);

        /* Set up the building class map */
        buildingClassMap.put(0, Armory.class);
        buildingClassMap.put(1, Bakery.class);
        buildingClassMap.put(2, Barracks.class);
        buildingClassMap.put(3, Brewery.class);
        buildingClassMap.put(4, Catapult.class);
        buildingClassMap.put(5, CoalMine.class);
        buildingClassMap.put(6, DonkeyFarm.class);
        buildingClassMap.put(7, Farm.class);
        buildingClassMap.put(8, Fishery.class);
        buildingClassMap.put(9, ForesterHut.class);
        buildingClassMap.put(10, Fortress.class);
        buildingClassMap.put(11, GoldMine.class);
        buildingClassMap.put(12, GraniteMine.class);
        buildingClassMap.put(13, GuardHouse.class);
        buildingClassMap.put(14, Headquarter.class);
        buildingClassMap.put(15, HunterHut.class);
        buildingClassMap.put(16, IronMine.class);
        buildingClassMap.put(17, Mill.class);
        buildingClassMap.put(18, Mint.class);
        buildingClassMap.put(19, PigFarm.class);
        buildingClassMap.put(20, Quarry.class);
        buildingClassMap.put(21, Sawmill.class);
        buildingClassMap.put(22, SlaughterHouse.class);
        buildingClassMap.put(23, Storage.class);
        buildingClassMap.put(24, WatchTower.class);
        buildingClassMap.put(25, Well.class);
        buildingClassMap.put(26, Woodcutter.class);

        /* Set up the vegetation map */
        vegetationMap.put(1, Tile.Vegetation.WATER);
        vegetationMap.put(2, Tile.Vegetation.GRASS);
        vegetationMap.put(3, Tile.Vegetation.SWAMP);
        vegetationMap.put(4, Tile.Vegetation.MOUNTAIN);
        vegetationMap.put(5, Tile.Vegetation.SAVANNAH);
        vegetationMap.put(6, Tile.Vegetation.SNOW);
        vegetationMap.put(7, Tile.Vegetation.DESERT);
        vegetationMap.put(8, Tile.Vegetation.DEEP_WATER);
        vegetationMap.put(9, Tile.Vegetation.SHALLOW_WATER);
        vegetationMap.put(10, Tile.Vegetation.STEPPE);
        vegetationMap.put(11, Tile.Vegetation.LAVA);
        vegetationMap.put(12, Tile.Vegetation.MAGENTA);
        vegetationMap.put(13, Tile.Vegetation.MOUNTAIN_MEADOW);
        vegetationMap.put(14, Tile.Vegetation.BUILDABLE_MOUNTAIN);

        /* Create game map */
        List<Player> players = new ArrayList<>();
        players.add(new Player("Player 0", Color.BLUE));

        MapLoader mapLoader = new MapLoader();
        MapFile mapFile = mapLoader.loadMapFromFile(MAP_FILENAME);

        GameMap map = mapLoader.convertMapFileToGameMap(mapFile);

        map.setPlayers(players);

        /* Adjust the amount of wild animals */
        List<WildAnimal> wildAnimals = map.getWildAnimals();
        if (wildAnimals.size() > 10) {
            while (wildAnimals.size() > 10) {
                wildAnimals.remove(10);
            }
        }

        /* Execute the commands from the input file */
        String st;

        ArgumentsHandler arguments = new ArgumentsHandler(inputStream);

        while (true) {

            String command = arguments.getChar();

            try {
                switch (command) {
                    case "1": // BUILD_ROAD_AUTO PLAYER_ID START.X START.Y END.X END.Y
                        try {
                            buildRoadAutomatic(map, arguments);
                        } catch (InvalidEndPointException e) {
                            System.out.println(e);
                        }
                        break;
                    case "2": // DELETE_ROAD POINT.X POINT.Y
                        try {
                            deleteRoadAtPoint(map, arguments);
                        } catch (NullPointerException e) {
                            System.out.println(e);
                        }
                        break;
                    case "3": // RAISE_FLAG PLAYER_ID POINT.X POINT.Y
                        try {
                            raiseFlag(map, arguments);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;
                    case "4": //""DELETE_FLAG":
                        try {
                            deleteFlagAtPoint(map, arguments);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                        break;
                    case "5": //""FAST_FORWARD":
                        fastForward(map, arguments);
                        break;
                    case "6": // RAISE_BUILDING PLAYER_ID TYPE POINT.X POINT.Y
                        raiseBuilding(map, arguments);
                        break;
                    case "7": // DELETE_BUILDING_AT_POINT POINT.X POINT.Y
                        deleteBuilding(map, arguments);
                        break;
                    case "8": // SET_VEGETATION_BELOW TYPE POINT.X POINT.Y
                        setVegetationBelow(map, arguments);
                        break;
                    case "9": // SET_VEGETATION_DOWN_RIGHT TYPE POINT.X POINT.Y
                        setVegetationDownRight(map, arguments);
                        break;
                    case "A": // START_PRODUCTION HOUSE_INDEX
                        startProduction(map, arguments);
                        break;
                    case "B": // STOP_PRODUCTION HOUSE_INDEX
                        stopProduction(map, arguments);
                        break;
                    case "C": // CALL_SCOUT POINT.X POINT.Y
                        callScout(map, arguments);
                        break;
                    case "D": // CALL_GEOLOGIST POINT.X POINT.Y
                        callGeologist(map, arguments);
                        break;
                    case "E": // CHANGE_TRANSPORTATION_PRIORITY
                        changeTransportationPriority(map, arguments);
                        break;
                    case "F": // CHANGE_COAL_ALLOCATION
                        changeCoalAllocation(map, arguments);
                        break;
                    case "G": // CHANGE_FOOD_ALLOCATION
                        changeFoodAllocation(map, arguments);
                        break;
                    default:
                        System.out.println("CAN'T HANDLE: '" + command + "'");
                        return;
                }
            } catch (SettlersModelDriverException e) {
                System.out.println(e);
            } catch (InvalidUserActionException e) {
                System.out.println(e);
            }
        }
    }

    private static void changeFoodAllocation(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        Player player = null;
        Class<? extends Building> buildingClass = null;
        int quota = 0;

        try {
            player = map.getPlayers().get(arguments.getIntFor1Chars());

            int type = arguments.getIntFor1Chars();

            if (type == 0) {
                buildingClass = CoalMine.class;
            } else if (type == 1) {
                buildingClass = GraniteMine.class;
            } else if (type == 2) {
                buildingClass = IronMine.class;
            } else if (type == 3) {
                buildingClass = GoldMine.class;
            } else {
                throw new SettlersModelDriverException();
            }

            quota = arguments.getIntFor2Chars();
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        player.setFoodQuota(buildingClass, quota);
    }

    private static void changeCoalAllocation(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        Player player = null;
        Class<? extends Building> buildingClass = null;
        int quota = 0;

        try {
            buildingClass = buildingClassMap.get(arguments.getIntFor2Chars());
            quota = arguments.getIntFor2Chars();

            if (player == null) {
                throw new SettlersModelDriverException();
            }
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        player.setCoalQuota(buildingClass, quota);
    }

    private static void changeTransportationPriority(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        Player player = null;
        int priority = 0;
        Material material = null;

        try {
            player = map.getPlayers().get(arguments.getIntFor1Chars());

            material = Material.values()[arguments.getIntFor2Chars()];

            priority = arguments.getIntFor2Chars();

            if (player == null) {
                throw new SettlersModelDriverException();
            }
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        player.setTransportPriority(priority, material);
    }

    private static void callScout(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        Flag flag = null;

        try {
            Point point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );

            flag = map.getFlagAtPoint(point);

            if (flag == null) {
                throw new Exception();
            }
        } catch (Throwable t) {
            System.out.println(t);

            throw new SettlersModelDriverException();
        }

        flag.callScout();
    }

    private static void callGeologist(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        Flag flag = null;

        try {
            Point point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );

            flag = map.getFlagAtPoint(point);

            if (flag == null) {
                throw new Exception();
            }
        } catch (Throwable t) {
            System.out.println(t);

            throw new SettlersModelDriverException();
        }

        flag.callGeologist();
    }

    private static void stopProduction(GameMap map, ArgumentsHandler arguments) throws Exception {
        Building building = null;

        try {
            int buildingIndex = arguments.getIntFor2Chars();
            building = map.getBuildings().get(buildingIndex);
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        building.stopProduction();
    }

    private static void startProduction(GameMap map, ArgumentsHandler arguments) throws Exception {
        Building building = null;

        try {
            int buildingIndex = arguments.getIntFor2Chars();
            building = map.getBuildings().get(buildingIndex);
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        building.resumeProduction();
    }

    private static void setVegetationDownRight(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        System.out.println("SET VEGETATION DOWN RIGHT: ");

        Tile.Vegetation vegetation = null;
        Point point = null;

        try {

            int vegetationInt = arguments.getIntFor1Chars();

            if (!vegetationMap.containsKey(vegetationInt)) {
                throw new Exception();
            }

            vegetation = vegetationMap.get(vegetationInt);

            point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        System.out.println(" - Point: " + point);
        System.out.println(" - Vegetation: " + vegetation);

        map.getTerrain().getTileDownRight(point).setVegetationType(vegetation);

    }

    private static void setVegetationBelow(GameMap map, ArgumentsHandler arguments) throws SettlersModelDriverException {
        System.out.println("SET VEGETATION BELOW");

        Tile.Vegetation vegetation = null;
        Point point = null;

        try {
            int vegetationInt = arguments.getIntFor1Chars();

            if (!vegetationMap.containsKey(vegetationInt)) {
                throw new Exception();
            }

            vegetation = vegetationMap.get(vegetationInt);

            point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        System.out.println(" - Point: " + point);
        System.out.println(" - Vegetation: " + vegetation);

        map.getTerrain().getTileBelow(point).setVegetationType(vegetation);
    }

    /**
     *
     * DELETE_BUILDING_AT_POINT POINT.X POINT.Y
     *
     * @param map
     * @param arguments
     * @throws Exception
     */
    private static void deleteBuilding(GameMap map, ArgumentsHandler arguments) throws Exception {
        Building building = null;

        System.out.println("DELETE_BUILDING_AT_POINT");

        try {
            Point point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );
            building = map.getBuildingAtPoint(point);

            if (building == null) {
                throw new Exception();
            }

        } catch (Throwable t) {
            System.out.println(t);

            throw new SettlersModelDriverException();
        }

        building.tearDown();
    }

    /**
     *
     * RAISE_BUILDING PLAYER_ID TYPE POINT.X POINT.Y
     *                    1      2     3       3
     * @param map
     * @param arguments
     */
    private static void raiseBuilding(GameMap map, ArgumentsHandler arguments) throws Exception {

        System.out.println("RAISE BUILDING: ");

        Building building = null;
        Point point = null;

        try {
            Player player = map.getPlayers().get(arguments.getIntFor1Chars());
            building = createBuilding(player, arguments.getIntFor2Chars());
            point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );
        } catch (Throwable t) {
            System.out.println(t);
            throw new SettlersModelDriverException();
        }

        map.placeBuilding(building, point);
        //System.out.println(" -- placed building " + building + " at " + building.getPosition());
        //System.out.println(" -- Flag is " + building.getFlag() + ", road to flag " + map.getRoad(building.getPosition(), building.getFlag().getPosition()));
    }

    private static Building createBuilding(Player player, int parseInt) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<? extends Building> buildingClass = buildingClassMap.get(parseInt);

        Constructor<?> constructor = buildingClass.getConstructor(Player.class);

        Building building =  (Building)constructor.newInstance(player);

        //System.out.println("Created " + building);

        return building;
    }

    private static List<String> stringToArguments(String substring) {
        String remaining = substring;
        List<String> arguments = new ArrayList<>();

        while (remaining.length() > 0) {
            arguments.add(remaining.substring(0, 2));

            if (remaining.length() > 2) {
                remaining = remaining.substring(2);
            } else {
                break;
            }
        }

        return arguments;
    }

    private static void fastForward(GameMap map, ArgumentsHandler arguments) throws Exception {

        int iterations = 0;

        try {

            iterations = arguments.getIntFor3Chars();

            if (iterations < 1 || iterations > 2000) {
                return;
            }
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        System.out.println("Fast forward: " + iterations);

        for (int i = 0; i < iterations; i++) {

            map.stepTime();
        }
    }

    private static void deleteFlagAtPoint(GameMap map, ArgumentsHandler arguments) throws Exception {

        Flag flag = null;

        System.out.println("DELETE FLAG AT POINT");

        try {
            Point point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );

            flag = map.getFlagAtPoint(point);
        } catch (Throwable t) {
            throw  new SettlersModelDriverException();
        }

        map.removeFlag(flag);
    }

    /**
     *
     * RAISE_FLAG PLAYER_ID POINT.X POINT.Y
     *
     * @param map
     * @param arguments
     * @throws Exception
     */
    private static void raiseFlag(GameMap map, ArgumentsHandler arguments) throws Exception {
        Player player = null;
        Point point = null;

        System.out.println("RAISE FLAG");

        try {
            int playerIndex = arguments.getIntFor1Chars();

            point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );

            player = map.getPlayers().get(playerIndex);
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        map.placeFlag(player, point);

    }

    /**
     *
     * DELETE_ROAD ROAD_ID
     * @param map
     * @param arguments
     */
    private static void deleteRoadAtPoint(GameMap map, ArgumentsHandler arguments) throws Exception {

        Road road = null;

        System.out.println("DELETE ROAD AT POINT");

        try {
            Point point = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );

            road = map.getRoadAtPoint(point);
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        map.removeRoad(road);
    }

    /**
     *
     * BUILD_ROAD_AUTO PLAYER_ID START.X START.Y END.X END.Y
     *                      ^      ^       ^       ^    ^
     *                      |      |       |       |    |
     *                      |      |       |       |    --- 3 char int
     *                      |      |       |        --- 3 char int
     *                      |      |        --- 3 char int
     *                      |       --- 3 char int
     *                       -- 1 char int
     *
     * @param map
     * @param arguments
     */
    private static void buildRoadAutomatic(GameMap map, ArgumentsHandler arguments) throws Exception {

        Point start = null;
        Point end = null;
        Player player = null;

        System.out.println("PLACE ROAD AUTOMATICALLY");

        try {
            int playerIndex = arguments.getIntFor1Chars();

            start = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );
            end = new Point(
                    arguments.getIntFor3Chars(),
                    arguments.getIntFor3Chars()
            );

            player = map.getPlayers().get(playerIndex);
        } catch (Throwable t) {
            throw new SettlersModelDriverException();
        }

        System.out.println(" - Start: " + start);
        System.out.println(" - End: " + end);
        System.out.println(" - Player: " + player);

        map.placeAutoSelectedRoad(player, start, end);
    }
}
