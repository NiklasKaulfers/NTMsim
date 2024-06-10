package src;

import java.util.ArrayList;
import java.util.stream.*;


public class Sim{
    private final char DEFAULT_BLANK_SPACE = '_';
    public final char EPSILON = 'ε';
    private final String start;
    private final String end;
    private final ArrayList<Command> commands;
    private ArrayList<Command> commandsCopy;
    private final ArrayList<String> commandDecisionLog = new ArrayList<>();
    private final ArrayList<String> commandsLog = new ArrayList<>();
    private int logIndex;
    private String input;
    private  ArrayList<Character> alphabet;
    private ArrayList<Character> symbols;
    private Boolean outputTooBig = false;
    private int researchDepth = 10;

    /**
     * creates the simulator
     * @param start the start Command by name
     * @param end the end Command by name
     * @param alphabet the alphabet used for input
     * @param symbols the symbols that can be placed
     * @param commands the commands for the movement of the TM
     * @param input input to use the grammar on
     */
    public Sim(String start
            , String end
            , ArrayList<Character> alphabet
            , ArrayList<Character> symbols
            , ArrayList<Command> commands
            , String input){
        this.start = start;
        this.end = end;
        this.commands = commands;
        this.commandsCopy = commands;
        this.input = input;
        this.alphabet = alphabet;
        this.symbols = symbols;
        symbols.add(EPSILON);
    }

    /**
     * new attempt of calculating the result with the input String
     * resets a lot of logging values
     * @return the result of the calculation with start and 0
     */
    public boolean solve(){
        outputTooBig = false;
        commandDecisionLog.clear();
        commandsCopy = new ArrayList<>(commands);
        logIndex = 0;
        commandsLog.clear();
        commandsLog.add(" ");

        boolean returnVal = false;
        for (int i =0; i < researchDepth; i++) {
            commandsLog.add("");
            logIndex++;
            returnVal = calc(start, 0);
            if (returnVal){
                break;
            }
        }
        for (String s : commandsLog) {
            if (s.length() > 4 && s.endsWith("end")){
                s = s + "-> TRUE";
            } else {
                if (s.length() > 1) {
                    s = s + "-> FALSE";
                }
            }
            System.out.println(s);
        }
        return returnVal;
    }

    /**
     * calculates the result
     * is private so u cant enter a different start from outside by accident
     * calls itself recursively
     * @param currentCommandName the name of the command the TM is at
     * @param posIndex position of the string
     * @return if the input is part of given grammar
     */
    private boolean calc(String currentCommandName, int posIndex){
        commandsLog.set(logIndex, commandsLog.get(logIndex) + " " + currentCommandName);
        boolean result = false;
        int finalPosIndex = posIndex;

        ArrayList<Command> possibleCommands = new ArrayList<>((
                commandsCopy.stream().filter(c -> c.getName().equals(currentCommandName)
                && c.getCurrentChar() == input.charAt(finalPosIndex)).collect(Collectors.toList())));
        Command currentCommand;
        if (possibleCommands.isEmpty()){
            return false;
        } else if (possibleCommands.size() == 1){
            currentCommand = possibleCommands.getFirst();
            if (currentCommand.currentChar == input.charAt(posIndex)){
                input = input.substring(0, posIndex) + currentCommand.getReplaceChar() + input.substring(posIndex+1);
                if (currentCommand.getDirection().equals(Direction.LEFT)) {
                    posIndex--;
                    if (posIndex < 0) {
                        input = DEFAULT_BLANK_SPACE + input;
                        posIndex = 0;
                    }
                }
                if (currentCommand.getDirection().equals(Direction.RIGHT)){
                    posIndex ++;
                    if (posIndex >= input.length()){
                        input = input + DEFAULT_BLANK_SPACE;
                    }
                }

                result = calc(currentCommand.getNextCommand(), posIndex);
            } else {
                return false;
            }
        } else {
            if (commandDecisionLog.size() >= researchDepth){
                outputTooBig = true;
                return false;
            } else {
                for (int i = 0; i < possibleCommands.size(); i++){
                    int finalPosIndex1 = posIndex;
                    // substring 1 = commandName
                    // substring 0, 1 = index
                    int finalI = i;
                    if (commandDecisionLog.stream().anyMatch(cd-> cd.equals(finalPosIndex1 + possibleCommands.get(finalI).getName()))){
                        possibleCommands.remove(i);
                    }
                }
                double v = (double) possibleCommands.size() * (Math.random());
                currentCommand = possibleCommands.get((int) v);
                commandDecisionLog.add(posIndex + currentCommand.getName());
                if (currentCommand.getDirection().equals(Direction.LEFT)) {
                    posIndex--;
                    if (posIndex < 0) {
                        input = DEFAULT_BLANK_SPACE + input;
                        posIndex = 0;
                    }
                }
                if (currentCommand.getDirection().equals(Direction.RIGHT)){
                    posIndex ++;
                    if (posIndex >= input.length()){
                        input = input + DEFAULT_BLANK_SPACE;
                    }
                }

                result = calc(currentCommand.getNextCommand(), posIndex);

            }
        }

        if (currentCommand.getNextCommand().equals(end)){
            input = input.substring(0, posIndex) + currentCommand.getReplaceChar() + input.substring(posIndex+1);
            return true;
        }
        return result;
    }

    /**
     * changes the amount of attempts to find the right path the machine makes (default is 10)
     * @param researchDepth change the amount to this
     */
    public void setResearchDepth(int researchDepth){
        this.researchDepth = researchDepth;
    }
}