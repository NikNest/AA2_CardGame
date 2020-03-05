package game.gameState;

import edu.kit.informatik.Terminal;
import game.gameState.utils.*;
import java.util.*;

public class Game {
    Stack<Card> deck = new Stack<>();
    ArrayList<Resource> resources = new ArrayList<>();
    Animal enemy;
    boolean encounter = false;
    boolean endeavour = false;
    ArrayList<String> equipment = new ArrayList<>();

    public Game(String[] cards) throws IncorrectInputException {
        for (String card : cards) {
            switch (card){
                case "wood":
                    deck.push(new Resource(CardValue.WOOD, true));
                    break;
                case "metal":
                    deck.push(new Resource(CardValue.METAL, true));
                    break;
                case "plastic":
                    deck.push(new Resource(CardValue.PLASTIC, true));
                    break;
                case "spider":
                    deck.push(new Animal(CardValue.SPIDER));
                    break;
                case "snake":
                    deck.push(new Animal(CardValue.SNAKE));
                    break;
                case "tiger":
                    deck.push(new Animal(CardValue.TIGER));
                    break;
                case "thunderstorm":
                    deck.push(new Catastrophe(CardValue.THUNDERSTORM));
                    break;
                default:
                    Terminal.printLine("smth went wrong...");
                    break;
            }
        }
        Terminal.printLine("OK");
    }

    //return true for end game situation
    public boolean makeTurn(String userInput) throws IncorrectInputException {
        try {
            if (userInput.equals(""))
                throw new IncorrectInputException();
            String[] temp = userInput.split(" ");
            String commandName = temp[0];
            switch (commandName) {
                case "draw":
                    if (temp.length != 1)
                        throw new IncorrectInputException();
                    return draw();
                case "list-resources":
                    if (temp.length != 1)
                        throw new IncorrectInputException();
                    listResources();
                    break;
                case "build":
                    if (temp.length != 2 && temp[1].matches("^axe|club|shack|fireplace|" +
                            "sailingraft|hangglider|steamboat|ballon$"))
                        throw new IncorrectInputException();
                    return build(temp[1]);
                case "list-buildings":
                    if (temp.length != 1)
                        throw new IncorrectInputException();
                    listBuildings();
                    break;
                case "build?":
                    if (temp.length != 1)
                        throw new IncorrectInputException();
                    listAvailBuildings();
                    break;
                default:
                    if(temp.length != 2 && temp[0].matches("^rollD\\d{1}") && temp[1].matches("^\\d{1}$")) {
                        String[] rollStr = temp[0].split("D");
                        return dice(rollStr[1], temp[1]);
                    } else
                        throw new IncorrectInputException();
            }
            return false;
        } catch (IncorrectInputException e) {
            return false;
        }
    }

    //return true for end game situation
    private boolean dice(String s, String s1) throws IncorrectInputException {
        if(endeavour) {
            if(s.equals("6")) {
                if(s1.matches("^4|5|6?")) {
                    Terminal.printLine("win");
                    return true;
                } else {
                    Terminal.printLine("lose");
                    endeavour = false;
                    return false;
                }
            } else
                throw new IncorrectInputException("wrong dice type");
        } else if (encounter) {
            int power = 0;
            if(equipment.stream().anyMatch(s2 -> s2.equals("club")))
                power = 1;
            if(equipment.stream().anyMatch(s2 -> s2.equals("axe")))
                power = 2;
            if (enemy.getNumDice() == 8) {
                if (s.equals("8")) {
                    if (!(1 <= Integer.parseInt(s1) && Integer.parseInt(s1) <= 8))
                        throw new IncorrectInputException("wrong number on the dice");
                    if (Integer.parseInt(s1) + power > 4) {
                        Terminal.printLine("survived");
                    } else {
                        Terminal.printLine("lose");
                        executeRobbery();
                    }
                    return false;
                } else
                    throw new IncorrectInputException("wrong dice type");
            } else if (enemy.getNumDice() == 6) {
                if (s.equals("6")) {
                    if (!(1 <= Integer.parseInt(s1) && Integer.parseInt(s1) <= 6))
                        throw new IncorrectInputException("wrong number on the dice");
                    if(Integer.parseInt(s1) + power > 3) {
                        Terminal.printLine("survived");
                    } else {
                        Terminal.printLine("lose");
                        executeRobbery();
                    }
                } else
                    throw new IncorrectInputException("wrong dice type");
            } else {
                if (s.equals("4")) {
                    if (!(1 <= Integer.parseInt(s1) && Integer.parseInt(s1) <= 4))
                        throw new IncorrectInputException("wrong number on the dice");
                    if(Integer.parseInt(s1) + power > 2) {
                        Terminal.printLine("survived");
                    } else {
                        Terminal.printLine("lose");
                        executeRobbery();
                    }
                } else
                    throw new IncorrectInputException("wrong dice type");
            }
        } else
            throw new IncorrectInputException("you can't roll the dice now");
        return false;
    }

    private void executeRobbery() {
        if(equipment.stream().anyMatch(s -> s.equals("shack"))) {
            ArrayList<Resource> temp = new ArrayList<>();
            int numSaved = Math.min(resources.size(), 5);
            ListIterator<Resource> iter = resources.listIterator(resources.size());
            while(numSaved > 0) {
                temp.add(iter.previous());
                numSaved--;
            }
            Collections.reverse(temp);
            resources = temp;
        } else
            resources.clear();
    }
    private void listAvailBuildings() {
        int numPlastic, numWood, numMetall;
        numPlastic = (int) resources.stream().
                filter(resource -> resource.getType().equals("plastic")).count();
        numMetall = (int) resources.stream().
                filter(resource -> resource.getType().equals("metal")).count();
        numWood = (int) resources.stream().
                filter(resource -> resource.getType().equals("wood")).count();
        String str = "";

        if(!equipment.stream().anyMatch(s -> s.equals("axe")))
            if(numMetall >= 3)
                str += "axe\n";
        if(!equipment.stream().anyMatch(s -> s.equals("ballon")))
            if(numPlastic >= 6 && numWood >= 1)
                str += "ballon\n";
        if(!equipment.stream().anyMatch(s -> s.equals("club")))
            if(numWood >= 3)
                str += "club\n";
        if(!equipment.stream().anyMatch(s -> s.equals("fireplace")))
            if(numWood >= 3 && numMetall >= 1)
                str += "fireplace\n";
        if(!equipment.stream().anyMatch(s -> s.equals("hangglider")))
            if(numPlastic >= 4 && numMetall >= 2 && numWood >= 2)
                str += "hangglider\n";
        if(!equipment.stream().anyMatch(s -> s.equals("sailingraft")))
            if(numPlastic >= 2 && numMetall >= 2 && numWood >= 4)
                str += "sailingraft\n";
        if(!equipment.stream().anyMatch(s -> s.equals("shack")))
            if(numPlastic >= 2 && numMetall >= 1 && numWood >= 2)
                str += "shack\n";
        if(!equipment.stream().anyMatch(s -> s.equals("steamboat")))
            if(numPlastic >= 1 && numMetall >= 6)
                str += "steamboat\n";
        Terminal.printLine(str);
    }

    private void listBuildings() {
        if (equipment.size() == 0) {
            Terminal.printLine("EMPTY");
            return;
        }
        String str = "";
        for (String s : equipment) {
            str += s + "\n";
        }
        Terminal.printLine(str.trim());
    }

    //return true for endgame
    private boolean build(String s) throws IncorrectInputException {
        if (encounter || endeavour)
            throw new IncorrectInputException("you can't do this now");
        switch (s) {
            case "axe":
                buildTool(true);
                break;
            case "club":
                buildTool(false);
                break;
            case "shack":
                buildShelter(false);
                break;
            case "fireplace":
                buildShelter(true);
                break;
            case "sailingraft":
                return buildTransport(TransportType.SAILINGRAFT);
            case "hangglider":
                return buildTransport(TransportType.HANGGLIDER);
            case "steamboat":
                return buildTransport(TransportType.STEAMBOAT);
            case "baloon":
                return buildTransport(TransportType.BALLON);
            default:
                throw new IncorrectInputException();
        }
        return false;
    }

    private void buildShelter(boolean fireplace) throws IncorrectInputException {
        //3 w 1 m
        //2 w 1 m 2 p

        if (fireplace) {
            for(String s : equipment) {
                if (s.equals("fireplace")) {
                    throw new IncorrectInputException("you can't simultaneously have two fireplaces");
                }
            }
            int numMetal = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("metal"); }).count();
            int numWood = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("wood"); }).count();
            if (numMetal < 1 || numWood < 3)
                throw new IncorrectInputException("there are not enough resources for this building");
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            equipment.add("fireplace");
        } else {
            for (String s : equipment) {
                if (s.equals("shack"))
                    throw new IncorrectInputException("you can't simultaneously have two shacks");
            }
            int numMetal = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("metal"); }).count();
            int numWood = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("wood"); }).count();
            int numPlastic = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("plastic"); }).count();
            if (numMetal < 1 || numWood < 2 || numPlastic < 2)
                throw new IncorrectInputException("there are not enough resources for this building");
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.PLASTIC, false));
            resources.remove(new Resource(CardValue.PLASTIC, false));
            equipment.add("shack");
        }
        Terminal.printLine("OK");
    }

    private void buildTool(boolean axe) throws IncorrectInputException {
        if (axe) {
            if (equipment.stream().anyMatch(s -> s.equals("axe"))) {
                throw new IncorrectInputException("you can't simultaneously have two axes");
            }
            int numMetal = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("metal"); }).count();
            if (numMetal < 3)
                throw new IncorrectInputException("there are not enough resources for this tool");
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.METAL, false));
            equipment.add("axe");
        } else {
            if(equipment.stream().anyMatch(s -> s.equals("club")))
                throw new IncorrectInputException("you can't simultaneously have two clubs");

            int numWood = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("wood"); }).count();
            if (numWood < 3)
                throw new IncorrectInputException("there are not enough resources for this tool");
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            equipment.add("club");
        }
        Terminal.printLine("OK");
    }

    private boolean buildTransport(TransportType transportType) throws IncorrectInputException {
        int numPlastic, numWood, numMetall;
        boolean firePlace;
        switch (transportType) {
            case BALLON:
                if(equipment.stream().noneMatch(s -> s.equals("fireplace")))
                    throw new IncorrectInputException("you need campfire for this kind of transport");
                numPlastic = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("plastic"); }).count();
                numWood = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("metal"); }).count();
                if (numPlastic < 6 || numWood < 1)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                Terminal.printLine("win");
                return true;
            case STEAMBOAT:
                if(equipment.stream().noneMatch(s -> s.equals("fireplace"))) {
                    throw new IncorrectInputException("you need campfire for this kind of transport");
                }
                numPlastic = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("plastic"); }).count();
                numMetall = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("metal"); }).count();
                if (numPlastic < 1 || numMetall < 6)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                Terminal.printLine("win");
                return true;
            case SAILINGRAFT:
                numPlastic = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("plastic"); }).count();
                numMetall = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("metal"); }).count();
                numWood = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("wood"); }).count();
                if (numPlastic < 2 || numMetall < 2 || numWood < 4)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                endeavour = true;
                equipment.add("sailingraft");
                return false;
            case HANGGLIDER:
                numPlastic = (int) resources.stream().
                        filter(resource -> resource.getType().equals("plastic")).count();
                numMetall = (int) resources.stream().
                        filter(resource -> resource.getType().equals("metal")).count();
                numWood = (int) resources.stream().
                        filter(resource -> resource.getType().equals("wood")).count();
                if (numPlastic < 4 || numMetall < 2 || numWood < 2)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                endeavour = true;
                equipment.add("hangglider");
                return false;
            default:
                throw new IncorrectInputException();
        }
    }

    private void listResources() {
        if (resources.size() == 0) {
            Terminal.printLine("EMPTY");
            return;
        }
        String str = "";
        for (Resource resource : resources) {
            str += resource.getType() + "\n";
        }
        Terminal.printLine(str.trim());
    }

    private void executeThunderStorm() {
        equipment.remove("fireplace");
        if(equipment.stream().anyMatch(s -> s.equals("shack"))) {
            ArrayList<Resource> temp = new ArrayList<>();
            int numSaved = Math.min(resources.size(), 5);
            ListIterator<Resource> iter = resources.listIterator(resources.size());
            while(numSaved > 0) {
                temp.add(iter.previous());
                numSaved--;
            }
            Collections.reverse(temp);
            resources = temp;
        } else
            resources.clear();
    }

    //true for lose situation
    private boolean checkForLoseSituation() {
        int numPlastic, numWood, numMetall;
        numPlastic = (int) resources.stream().
                filter(resource -> resource.getType().equals("plastic")).count();
        numMetall = (int) resources.stream().
                filter(resource -> resource.getType().equals("metal")).count();
        numWood = (int) resources.stream().
                filter(resource -> resource.getType().equals("wood")).count();
        if ((numPlastic >= 4 && numMetall >= 2 && numWood >= 2) || (numPlastic >= 2 && numMetall >= 2 && numWood >= 4)) {
            return false;
        }

        if(equipment.stream().anyMatch(s -> s.equals("fireplace"))) {
            if ((numPlastic >= 6 && numWood >= 1) || (numPlastic >= 1 && numMetall >= 6)) {
                return false;
            }
        } else
        if ((numPlastic >= 6 && numWood >= 4 && numMetall >= 1) || (numPlastic >= 1 && numMetall >= 1 && numWood >= 3)) {
            return false;
        }
        return true;
    }

    //returns true for lose game situation
    private boolean draw() throws IncorrectInputException {
        if (deck.size() == 0)
            throw new IncorrectInputException("you should try to escape now. There no more card in the deck");
        if (endeavour || encounter) {
            throw new IncorrectInputException("you can't draw the card now");
        }
        Card card = deck.pop();

        if(Resource.class.isInstance(card)) {
            Terminal.printLine(card.getType());
            resources.add((Resource) card);
        } else if (Animal.class.isInstance((card))) {
            Terminal.printLine(card.getType());
            enemy = (Animal) card;
            encounter = true;
        } else
            executeThunderStorm();
        if(deck.size() == 0) {
            return checkForLoseSituation();
        }
        return false;
    }
}





