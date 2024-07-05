package src;

import java.util.ArrayList;
import java.util.stream.*;


public class Sim{
    private final char DEFAULT_BLANK_SPACE = '_';
    public final char EPSILON = 'ε';
    private String start;
    private String end;
    private ArrayList<Command> commands;
    private ArrayList<Command> commandsCopy;
    private final ArrayList<String> commandDecisionLog = new ArrayList<>();
    private final ArrayList<String> commandsLog = new ArrayList<>();
    private final ArrayList<ArrayList<Command>> fullLog = new ArrayList<>();
    private ArrayList<Boolean> successLog = new ArrayList<>();
    private int logIndex;
    private String input;
    private ArrayList<Character> alphabet;
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
     * constructor without an input
     * allows user to just put in the alphabet and call input later
     * @param start the start point of NTM
     * @param end the name of the end point
     * @param alphabet all characters used in the input
     * @param symbols all characters usable for the NTM
     * @param commands the movement of the NTM as ArrayList of Command
     */
    public Sim(String start
            , String end
            , ArrayList<Character> alphabet
            , ArrayList<Character> symbols
            , ArrayList<Command> commands){
        this.start = start;
        this.end = end;
        this.alphabet = alphabet;
        this.symbols = symbols;
        this.commands = commands;
        symbols.add(EPSILON);
    }

    /**
     * creates an object of Sim with everything to be added later
     */
    public Sim(){
        symbols = new ArrayList<>(EPSILON);
    }

    /**
     * new attempt of calculating the result with the input String
     * resets a lot of logging values
     * @return the result of the calculation with start and 0
     */
    public boolean solve(){
        if (
                alphabet.isEmpty()
                        || symbols.isEmpty()
                        || commands.isEmpty()
                        || input.isEmpty()
                        || input == null
                        || start.isEmpty()
                        || end.isEmpty()
        ){
            return false;
        }
        outputTooBig = false;
        commandDecisionLog.clear();
        commandsCopy = new ArrayList<>(commands);
        logIndex = 0;
        commandsLog.clear();
        successLog.clear();
        fullLog.clear();

        boolean returnVal = false;
        for (int i =0; i < researchDepth; i++) {
            commandsLog.add("");
            fullLog.add(new ArrayList<Command>());
            returnVal = calc(start, 0);
            if (returnVal){
                break;
            }
            logIndex++;
        }
        updateLog();
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
        ArrayList<?> fullLogPos = fullLog.get(logIndex);
        if (commandsLog.get(logIndex).isEmpty()){
            commandsLog.set(logIndex, currentCommandName);
        } else{
            commandsLog.set(logIndex, commandsLog.get(logIndex) + " -> " + currentCommandName);
        }

        boolean result;
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

    /**
     * adds a true if the programm terminates and false if it doesn't
     */
    public void updateLog(){
        successLog = new ArrayList<>();
        for (int i =0; i < commandsLog.size(); i++){
            String s = commandsLog.get(i);
            if (!(s.equals(" "))
                    && !(s.isEmpty())
                    && !(s.endsWith("TRUE"))
                    && !(s.endsWith("FALSE"))){
                if (s.endsWith(end)){
                    successLog.add(true);
                    commandsLog.set(i, s + " -> TRUE");
                } else {
                    successLog.add(false);
                    commandsLog.set(i, s + " -> FALSE");
                }
            } else {
                successLog.add(false);
            }
        }
    }

    /**
     * returns the entire log
     * @return an ArrayList of all Log entries
     */
    public ArrayList<String> getLog(){
        return commandsLog;
    }

    /**
     * setter for input
     * @param input changes input to this
     */
    public void setInput(String input){
        this.input = input;
    }

    /**
     * sets a new Grammar
     * @param start the start
     * @param end the end
     * @param alphabet used alphabet
     * @param symbols characters placeable by the TM
     * @param commands usable Commands
     */
    public void setGrammar(String start, String end, ArrayList<Character> alphabet, ArrayList<Character> symbols, ArrayList<Command> commands){
        this.start = start;
        this.end = end;
        this.alphabet = alphabet;
        this.symbols = symbols;
        symbols.add(EPSILON);
        this.commands = commands;
    }

    /**
     * deletes all currently saved commands
     */
    public void deleteAllCommands(){
        commands.clear();
    }

    /**
     * adds 1 new command
     * @param c the new Command
     */
    public void addCommand(Command c){
        commands.add(c);
    }

    /**
     * changes Commands to this
     * @param commands the new Commands
     */
    public void setCommands(ArrayList<Command> commands){
        this.commands = commands;
    }

    /**
     * changes the start point
     * @param start new start point
     */
    public void setStart(String start){
        this.start = start;
    }

    /**
     * changes the name of the end
     * @param end new end point
     */
    public void setEnd(String end){
        this.end = end;
    }

    /**
     * getter for input
     * @return String of input
     */
    public String getInput(){
        return input;
    }

    /**
     * getter for successLog
     * @return returns the successLog
     */
    public ArrayList<Boolean> getSuccessLog(){
        return successLog;
    }
}