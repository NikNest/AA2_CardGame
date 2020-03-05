package game.gameState.utils;

import edu.kit.informatik.Terminal;

import java.util.ArrayList;

/**
 * class that accumulate listing methods of the game
 * @author Nikita
 * @version 1
 */
public class ListingMethodsGame {
    /**
     * list-resources implementation
     * @param resources player's resources
     */
    public static void listResources(ArrayList<Resource> resources) {
        String str = "";
        for (Resource resource : resources) {
            str += resource.getType() + "\n";
        }
        str = str.equals("") ? "EMPTY" : str.trim();
        Terminal.printLine(str);
    }

    /**
     * list-buildings implementation
     * @param equipment player's equipment
     */
    public static void listBuildings(ArrayList<String> equipment) {
        String str = "";
        for (String s : equipment) {
            str += s + "\n";
        }
        str = str.equals("") ? "EMPTY" : str.trim();
        Terminal.printLine(str);
    }

    /**
     * build? implementation
     * @param resources player's resources
     * @param equipment player's equipment
     */
    public static void listAvailBuildings(ArrayList<Resource> resources, ArrayList<String> equipment) {
        int numPlastic;
        int numWood;
        int numMetall;
        numPlastic = (int) resources.stream().
                filter(resource -> resource.getType().equals("plastic")).count();
        numMetall = (int) resources.stream().
                filter(resource -> resource.getType().equals("metal")).count();
        numWood = (int) resources.stream().
                filter(resource -> resource.getType().equals("wood")).count();
        String str = "";
        if (equipment.stream().noneMatch(s -> s.equals("axe")))
            if (numMetall >= 3)
                str += "axe\n";
        if (equipment.stream().noneMatch(s -> s.equals("ballon")))
            if (numPlastic >= 6 && numWood >= 1)
                str += "ballon\n";
        if (equipment.stream().noneMatch(s -> s.equals("club")))
            if (numWood >= 3)
                str += "club\n";
        if (equipment.stream().noneMatch(s -> s.equals("fireplace")))
            if (numWood >= 3 && numMetall >= 1)
                str += "fireplace\n";
        if (equipment.stream().noneMatch(s -> s.equals("hangglider")))
            if (numPlastic >= 4 && numMetall >= 2 && numWood >= 2)
                str += "hangglider\n";
        if (equipment.stream().noneMatch(s -> s.equals("sailingraft")))
            if (numPlastic >= 2 && numMetall >= 2 && numWood >= 4)
                str += "sailingraft\n";
        if (equipment.stream().noneMatch(s -> s.equals("shack")))
            if (numPlastic >= 2 && numMetall >= 1 && numWood >= 2)
                str += "shack\n";
        if (equipment.stream().noneMatch(s -> s.equals("steamboat")))
            if (numPlastic >= 1 && numMetall >= 6)
                str += "steamboat\n";
        str = str.equals("") ? "EMPTY" : str.trim();
        Terminal.printLine(str);
    }

}
