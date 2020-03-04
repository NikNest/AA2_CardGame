package game.gameState;

import edu.kit.informatik.Terminal;
import game.gameState.utils.*;


import java.util.*;

public class Game {
    Stack<Card> deck = new Stack<>();
    ArrayList<Resource> resources = new ArrayList<>();
    Animal enemy;
    Tool tool = new Tool();
    Shack shack = new Shack();
    boolean firePlace = false;
    boolean encounter = false;
    boolean endeavour = false;
    Dice diceNeeded = new Dice();


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

    public boolean makeTurn() throws IncorrectInputException{
        String userInput = Terminal.readLine().trim();
        if(userInput.equals(""))
            throw new IncorrectInputException();
        String[] temp = userInput.split(" ");
        String commandName = temp[0];
        switch (commandName) {
            case "draw":
                if(temp.length != 1)
                    throw new IncorrectInputException();
                if(encounter || endeavour)
                    throw new IncorrectInputException("you can't do this now");
                draw();
                break;
            case "list-resources":
                if(temp.length != 1)
                    throw new IncorrectInputException();
                listResources();
                break;
            case "build":
                if(temp.length != 2 && temp[1].matches("^axe|club|shack|fireplace|" +
                        "sailingraft|hangglider|steamboat|ballon$"))
                    throw new IncorrectInputException();
                if(encounter || endeavour)
                    throw new IncorrectInputException("you can't do this now");
                return !build(temp[1]);
        }
        return true;
    }

    private boolean build(String s) throws IncorrectInputException {
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
            if (firePlace) {
               throw new IncorrectInputException("you can't simultaneously have two fireplaces");
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
            firePlace = true;
        } else {
            if(shack.isInGame())
                throw new IncorrectInputException("you can't simultaneously have two shacks");
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
            shack.setInGame(true);
        }
        Terminal.printLine("OK");
    }

    private void buildTool(boolean axe) throws IncorrectInputException {
        if (axe) {
            if (tool.getPower() == 2) {
                throw new IncorrectInputException("you can't simultaneously have two axes");
            }
            int numMetal = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("metal"); }).count();
            if (numMetal < 3)
                throw new IncorrectInputException("there are not enough resources for this tool");
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.METAL, false));
            resources.remove(new Resource(CardValue.METAL, false));
            tool.setPower(2);
        } else {
            if (tool.getPower() == 2) {
                throw new IncorrectInputException("you have an axe already");
            }
            if (tool.getPower() == 1) {
                throw new IncorrectInputException("you can't simultaneously have two clubs");
            }
            int numWood = (int) resources.stream().
                    filter(resource -> {return resource.getType().equals("wood"); }).count();
            if (numWood < 3)
                throw new IncorrectInputException("there are not enough resources for this tool");
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            resources.remove(new Resource(CardValue.WOOD, false));
            tool.setPower(1);
        }
        Terminal.printLine("OK");
    }

    private boolean buildTransport(TransportType transportType) throws IncorrectInputException {
        int numPlastic, numWood, numMetall;
        switch (transportType) {
            case BALLON:
                if(!firePlace)
                    throw new IncorrectInputException("you need campfire for this kind of transport");
                numPlastic = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("plastic"); }).count();
                numWood = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("metal"); }).count();
                if (numPlastic < 6 || numWood < 1)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                Terminal.printLine("survived");
                return true;
            case STEAMBOAT:
                if(!firePlace)
                    throw new IncorrectInputException("you need campfire for this kind of transport");
                numPlastic = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("plastic"); }).count();
                numMetall = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("metal"); }).count();
                if (numPlastic < 1 || numMetall < 6)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                Terminal.printLine("survived");
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
                diceNeeded.setNumSides(6);
                return false;
            case HANGGLIDER:
                numPlastic = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("plastic"); }).count();
                numMetall = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("metal"); }).count();
                numWood = (int) resources.stream().
                        filter(resource -> {return resource.getType().equals("wood"); }).count();
                if (numPlastic < 4 || numMetall < 2 || numWood < 2)
                    throw new IncorrectInputException("there are not enough resources for this transport");
                endeavour = true;
                diceNeeded.setNumSides(6);
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
        Iterator<Resource> iter = resources.iterator();
        while (iter.hasNext()) {
            str += iter.next().getType() + "\n";
        }
        Terminal.printLine(str.trim());
    }

    private void executeThunderStorm() {

    }

    private void draw() throws IncorrectInputException {
        if (deck.size() == 0)
            throw new IncorrectInputException("you should try to escape now. There are no more cards in the deck");
        if (endeavour || encounter) {
            throw new IncorrectInputException("you can't draw the card now");
        }
        Card card = deck.pop();
        if(Resource.class.isInstance(card)) {
            Terminal.printLine(card.getType());
            resources.add((Resource) card);
        } else if (Animal.class.isInstance((card))) {
            Terminal.printError(card.getType());
            enemy = (Animal) card;
            encounter = true;
        } else
            executeThunderStorm();
    }
}





