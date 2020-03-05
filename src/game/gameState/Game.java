package game.gameState;

import edu.kit.informatik.Terminal;
import game.gameState.utils.*;
import java.util.*;

/**
 * class for the game's process. Logic of commands is implemented here
 * @author Nikita
 * @version 1
 */
public class Game {
    /**
     * deck of cards
     */
    Stack<Card> deck = new Stack<>();
    /**
     * player's resources
     */
    ArrayList<Resource> resources = new ArrayList<>();
    /**
     * enemy for encounter
     */
    Animal enemy;
    /**
     * if player in encounter state
     */
    boolean encounter = false;
    /**
     * if player in endeavour state
     */
    boolean endeavour = false;
    /**
     * player's equipment
     */
    ArrayList<String> equipment = new ArrayList<>();

    /**
     * constructor that initializes the deck of cards
     * @param cards string representation of the deck
     * @throws IncorrectInputException rules of start command are broken
     */
    public Game(String[] cards) throws IncorrectInputException {
        for (String card : cards) {
            switch (card) {
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
                    deck.push(new Catastrophe());
                    break;
                default:
                    throw new IncorrectInputException();
            }
        }
        Terminal.printLine("OK");
    }

    /**
     * realization of the game's turn
     * @param userInput command
     * @return true for "end game'-situation
     * @throws IncorrectInputException in case if rules of the given command's are broken
     */
    public boolean makeTurn(String userInput) throws IncorrectInputException {
        try {
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
                    ListingMethodsGame.listResources(resources);
                    break;
                case "build":
                    if (temp.length != 2 && temp[1].matches("^axe|club|shack|fireplace|"
                            + "sailingraft|hangglider|steamboat|ballon$"))
                        throw new IncorrectInputException();
                    return build(temp[1]);
                case "list-buildings":
                    if (temp.length != 1)
                        throw new IncorrectInputException();
                    ListingMethodsGame.listBuildings(equipment);
                    break;
                case "build?":
                    if (temp.length != 1)
                        throw new IncorrectInputException();
                    ListingMethodsGame.listAvailBuildings(resources, equipment);
                    break;
                default:
                    if (temp.length != 2 && temp[0].matches("^rollD\\d{1}") && temp[1].matches("^\\d{1}$")) {
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

    /**
     * method for throwing a dice
     * @param s dice type
     * @param s1 number on dice
     * @return true foe "end-game" situation
     * @throws IncorrectInputException for logicaly wrong user's input
     */
    private boolean dice(String s, String s1) throws IncorrectInputException {
        if (endeavour) {
            if (s.equals("6")) {
                if (s1.matches("^4|5|6?")) {
                    Terminal.printLine("win");
                    return true;
                }
                Terminal.printLine("lose");
                endeavour = false;
                return false;
            } else
                throw new IncorrectInputException("wrong dice type");
        } else if (encounter) {
            int power = equipment.stream().anyMatch(s2 -> s2.equals("axe")) ? 2
                    : equipment.stream().anyMatch(s2 -> s2.equals("club")) ? 1 : 0;
            if (enemy.getNumDice() == 8) {
                if (s.equals("8")) {
                    if (!(1 <= Integer.parseInt(s1) && Integer.parseInt(s1) <= 8))
                        throw new IncorrectInputException("wrong number on the dice");
                    if (Integer.parseInt(s1) + power > 4)
                        Terminal.printLine("survived");
                    else {
                        Terminal.printLine("lose");
                        executeRobbery();
                    }
                    encounter = false;
                    return false;
                } else
                    throw new IncorrectInputException("wrong dice type");
            } else if (enemy.getNumDice() == 6) {
                if (s.equals("6")) {
                    if (!(1 <= Integer.parseInt(s1) && Integer.parseInt(s1) <= 6))
                        throw new IncorrectInputException("wrong number on the dice");
                    if (Integer.parseInt(s1) + power > 3)
                        Terminal.printLine("survived");
                    else {
                        Terminal.printLine("lose");
                        executeRobbery();
                    }
                    encounter = false;
                    return false;
                } else
                    throw new IncorrectInputException("wrong dice type");
            } else {
                if (s.equals("4")) {
                    if (!(1 <= Integer.parseInt(s1) && Integer.parseInt(s1) <= 4))
                        throw new IncorrectInputException("wrong number on the dice");
                    if (Integer.parseInt(s1) + power > 2)
                        Terminal.printLine("survived");
                    else {
                        Terminal.printLine("lose");
                        executeRobbery();
                    }
                    encounter = false;
                    return false;
                } else
                    throw new IncorrectInputException("wrong dice type");
            }
        } else
            throw new IncorrectInputException("you can't roll the dice now");
    }

    /**
     * if animals are taking player's resources
     */
    private void executeRobbery() {
        if (equipment.stream().anyMatch(s -> s.equals("shack"))) {
            ArrayList<Resource> temp = new ArrayList<>();
            int numSaved = Math.min(resources.size(), 5);
            ListIterator<Resource> iter = resources.listIterator(resources.size());
            while (numSaved > 0) {
                temp.add(iter.previous());
                numSaved--;
            }
            Collections.reverse(temp);
            resources = temp;
        } else
            resources.clear();
    }

    /**
     * build <x> implementation
     * @param s user's command
     * @return true for "end game" situation
     * @throws IncorrectInputException for logicaly wrong user's input
     */
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

    /**
     * is called by "build" - command for buildings construction
     * @param fireplace true to build fireplace, false to build shack
     * @throws IncorrectInputException for logicaly wrong user's input
     */
    private void buildShelter(boolean fireplace) throws IncorrectInputException {
        if (fireplace) {
            if (equipment.stream().anyMatch(s -> s.equals("fireplace")))
                throw new IncorrectInputException("you can't simultaneously have two fireplaces");
            int numMetal = (int) resources.stream().
                    filter(resource -> resource.getType().equals("metal")).count();
            int numWood = (int) resources.stream().
                    filter(resource -> resource.getType().equals("wood")).count();
            if (numMetal < 1 || numWood < 3)
                throw new IncorrectInputException("there are not enough resources for this building");
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            equipment.add("fireplace");
        } else {
            if (equipment.stream().anyMatch(s -> s.equals("shack")))
                throw new IncorrectInputException("you can't simultaneously have two shacks");
            int numMetal = (int) resources.stream().
                    filter(resource -> resource.getType().equals("metal")).count();
            int numWood = (int) resources.stream().
                    filter(resource -> resource.getType().equals("wood")).count();
            int numPlastic = (int) resources.stream().
                    filter(resource -> resource.getType().equals("plastic")).count();
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

    /**
     * is called by "build" - command for tools creating
     * @param axe true to build axe, false to build club
     * @throws IncorrectInputException for logicaly wrong user's input
     */
    private void buildTool(boolean axe) throws IncorrectInputException {
        if (axe) {
            if (equipment.stream().anyMatch(s -> s.equals("axe")))
                throw new IncorrectInputException("you can't simultaneously have two axes");
            int numMetal = (int) resources.stream().
                    filter(resource -> resource.getType().equals("metal")).count();
            if (numMetal < 3)
                throw new IncorrectInputException("there are not enough resources for this tool");
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.METAL, false));
            equipment.add("axe");
        } else {
            if (equipment.stream().anyMatch(s -> s.equals("club")))
                throw new IncorrectInputException("you can't simultaneously have two clubs");
            int numWood = (int) resources.stream().
                    filter(resource -> resource.getType().equals("wood")).count();
            if (numWood < 3)
                throw new IncorrectInputException("there are not enough resources for this tool");
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            equipment.add("club");
        }
        Terminal.printLine("OK");
    }

    /**
     * is called by "build" - command for transport creating
     * @param transportType type of transport
     * @throws IncorrectInputException for logicaly wrong user's input
     */
    private boolean buildTransport(TransportType transportType) throws IncorrectInputException {
        int numPlastic;
        int numWood;
        int numMetall;
        switch (transportType) {
            case BALLON:
                if (equipment.stream().noneMatch(s -> s.equals("fireplace")))
                    throw new IncorrectInputException("you need campfire for this kind of transport");
                numPlastic = (int) resources.stream().
                        filter(resource -> resource.getType().equals("plastic")).count();
                numWood = (int) resources.stream().
                        filter(resource -> resource.getType().equals("metal")).count();
                if (numPlastic < 6 || numWood < 1)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                Terminal.printLine("win");
                return true;
            case STEAMBOAT:
                if (equipment.stream().noneMatch(s -> s.equals("fireplace"))) {
                    throw new IncorrectInputException("you need campfire for this kind of transport");
                }
                numPlastic = (int) resources.stream().
                        filter(resource -> resource.getType().equals("plastic")).count();
                numMetall = (int) resources.stream().
                        filter(resource -> resource.getType().equals("metal")).count();
                if (numPlastic < 1 || numMetall < 6)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                Terminal.printLine("win");
                return true;
            case SAILINGRAFT:
                numPlastic = (int) resources.stream().
                        filter(resource -> resource.getType().equals("plastic")).count();
                numMetall = (int) resources.stream().
                        filter(resource -> resource.getType().equals("metal")).count();
                numWood = (int) resources.stream().
                        filter(resource -> resource.getType().equals("wood")).count();
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

    /**
     * if the card from the deck is thunderstorm
     */
    private void executeThunderstorm() {
        equipment.remove("fireplace");
        if (equipment.stream().anyMatch(s -> s.equals("shack"))) {
            ArrayList<Resource> temp = new ArrayList<>();
            int numSaved = Math.min(resources.size(), 5);
            ListIterator<Resource> iter = resources.listIterator(resources.size());
            while (numSaved > 0) {
                temp.add(iter.previous());
                numSaved--;
            }
            Collections.reverse(temp);
            resources = temp;
        } else
            resources.clear();
    }

    /**
     * if there are no more cards in the deck, this method checks if you are still able to win
     * @return true if you can't
     */
    private boolean checkForLoseSituation() {
        int numPlastic;
        int numWood;
        int numMetall;
        numPlastic = (int) resources.stream().
                filter(resource -> resource.getType().equals("plastic")).count();
        numMetall = (int) resources.stream().
                filter(resource -> resource.getType().equals("metal")).count();
        numWood = (int) resources.stream().
                filter(resource -> resource.getType().equals("wood")).count();
        if ((numPlastic >= 4 && numMetall >= 2 && numWood >= 2) || (numPlastic >= 2 && numMetall >= 2 && numWood >= 4))
            return false;
        if (equipment.stream().anyMatch(s -> s.equals("fireplace"))) {
            if ((numPlastic >= 6 && numWood >= 1) || (numPlastic >= 1 && numMetall >= 6))
                return false;
        } else
            if ((numPlastic >= 6 && numWood >= 4 && numMetall >= 1)
                    || (numPlastic >= 1 && numMetall >= 1 && numWood >= 3))
                       return false;
        return true;
    }

    /**
     * take a new card from the deck
     * @return true for "end-game" situation"
     * @throws IncorrectInputException for logicaly wrong user's input
     */
    private boolean draw() throws IncorrectInputException {
        if (deck.size() == 0)
            throw new IncorrectInputException("you should try to escape now. There no more card in the deck");
        if (endeavour || encounter)
            throw new IncorrectInputException("you can't draw the card now");
        Card card = deck.pop();
        if (Resource.class.isInstance(card)) {
            Terminal.printLine(card.getType());
            resources.add((Resource) card);
        } else if (Animal.class.isInstance((card))) {
            Terminal.printLine(card.getType());
            enemy = (Animal) card;
            encounter = true;
        } else {
            Terminal.printLine(card.getType());
            executeThunderstorm();
        }
        if (deck.size() == 0)
            return checkForLoseSituation();
        return false;
    }
}





