package src;

import java.io.*;
import java.util.ArrayList;

/**
 * takes a txt file and makes Commands out of it
 */
public class Parser{
    private String input;
    private String inputCopy;

    private String start;
    private String end;
    private ArrayList<Character> alphabet;
    private ArrayList<Character> symbols;
    private ArrayList<Command> commands;

    private int simDefLoops = 0;
    private Sim grammar;

    private boolean hasInit = false;

    /**
     * constructor for testing
     * @param input the text that will become a command
     */
    public Parser(String input) {
        this.input = input;
    }

    /**
     * constructor using the real file
     * @param inputFile the file containing code for the NTM
     * @throws NTMSimError reading errors (for processing or display)
     */
    public Parser(File inputFile) throws NTMSimError, IOException {
        if (!inputFile.getName().endsWith(".txt")){
            throw new NTMSimError("can only read .txt files (file is not .txt)");
        }
        if (!inputFile.exists()){
            throw new NTMSimError("can't find .txt file");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            StringBuilder stringBuilder = new StringBuilder();
            String s;
            while ((s = br.readLine()) != null) {
                stringBuilder.append(s);
            }
            input = stringBuilder.toString();
        } catch (FileNotFoundException _) {
            throw new NTMSimError("can't open file (file not found)");
        }
    }

    /**
     * transforms the Code contained in a .txt file into Commands of the NTM
     * @throws NTMSimError if anything goes wrong reading through the text, will be shown as feedback for user
     */
    public void parse() throws NTMSimError {
        simDefLoops = 0;
        // removes all whitespaces
        input = input.replaceAll("\\s+", "");
        inputCopy = input;
        if (inputCopy.startsWith(".init:")){
            if (hasInit) {
                throw new NTMSimError("The .init part is found twice.");
            }else {
                hasInit = true;
                inputCopy = inputCopy.replace(".init:", "");
                initiateSimDefinition();
            }
        } else {
            throw new NTMSimError("file needs to start with '.init:'");
        }
        grammar = new Sim(start, end, alphabet, symbols, commands);
    }


    /**
     * creates the states for start, end, alphabet and symbols
     * basically code part after .init:
     * @throws NTMSimError syntax errors
     */
    private void initiateSimDefinition() throws NTMSimError {
        if (start == null
                && inputCopy.startsWith("start:")){
            inputCopy = inputCopy.replace("start:", "");
            start = buildStringAfterInit();
        }
        if (end == null
                && inputCopy.startsWith("end:")){
            inputCopy = inputCopy.replace("end:", "");
            end = buildStringAfterInit();
        }
        if (alphabet == null
                && inputCopy.startsWith("alphabet:")){
            inputCopy = inputCopy.replace("alphabet:", "");
            alphabet = buildArrayListAfterInit();
        }
        if (inputCopy.startsWith("symbols:")
                && symbols == null){
            inputCopy = inputCopy.replace("symbols:", "");
            symbols = buildArrayListAfterInit();
        }


        // if .init is completed -> move on to .code part
        if (start != null
                && end != null
                && alphabet != null
                && symbols != null){
            if (inputCopy.startsWith(".code:")){
                inputCopy = inputCopy.replace(".code:", "");
                createCommands();
            } else {
                throw new NTMSimError("The .code part is not initiated.");
            }
        } else {
            // prop checks too often, but just to be sure
            simDefLoops++;
            if(simDefLoops > 6){
                throw new NTMSimError("Not everything proper in init. Please check your code");
            } else {
                initiateSimDefinition();
            }
        }
    }

    /**
     * creates the commands after reading .code
     * @throws NTMSimError in case of a syntax error in the code
     */
    private void createCommands() throws NTMSimError {
        commands = new ArrayList<>();
        while (!inputCopy.isEmpty()) {
            int nextComma = findNextComma();
            String currentState = inputCopy.substring(0, nextComma);
            inputCopy = inputCopy.substring(nextComma + 1);
            char currentChar = inputCopy.charAt(0);
            if (inputCopy.charAt(1) != ',') {
                throw new NTMSimError("Error at currentChar in .code");
            }
            inputCopy = inputCopy.substring(2);
            nextComma = findNextComma();
            String nextState = inputCopy.substring(0, nextComma);
            inputCopy = inputCopy.substring(nextComma + 1);
            char replaceChar = inputCopy.charAt(0);
            if (inputCopy.charAt(1) != ',') {
                throw new NTMSimError("Error at nextChar creation in .code");
            }
            Direction direction;
            switch (inputCopy.charAt(2)) {
                case '>' -> direction = Direction.RIGHT;
                case '<' -> direction = Direction.LEFT;
                case '_' -> direction = Direction.STAY;
                default -> throw new NTMSimError("Error with direction in .code");
            }
            if (inputCopy.charAt(3) == ';') {
                inputCopy = inputCopy.substring(4);
            } else {
                throw new NTMSimError("Error in .code. Misses ';'");
            }
            commands.add(new Command(currentState, currentChar, nextState, replaceChar, direction));
        }
    }


    /**
     * creates one the initial Strings for start and end
     * @return a build string representing everything between initialisation and the semicolon
     * @throws NTMSimError in case its empty
     */
    private String buildStringAfterInit() throws NTMSimError {
        StringBuilder strB = new StringBuilder();
        if (inputCopy.startsWith(";")){
            throw new NTMSimError("Empty initializing statement in init: " + strB.getClass().getName());
        }
        int steps = 1;
        for (char c : inputCopy.toCharArray()){
            if (c == ';'){
                inputCopy = inputCopy.substring(steps);
                break;
            } else {
                strB.append(c);
            }
            steps++;
        }
        return strB.toString();
    }

    /**
     * creates an ArrayList of chars for alphabet or symbols
     * @return the created new arrayList
     */
    private ArrayList<Character> buildArrayListAfterInit(){
        ArrayList<Character> arrayList = new ArrayList<>();
        int steps = 1;
        for (char c : inputCopy.toCharArray()){
            if (c == ';'){
                inputCopy = inputCopy.substring(steps);
                break;
            }
            if (Character.isLetter(c) || Character.isDigit(c)) arrayList.add(c);
            steps++;
        }
        return arrayList;
    }

    /**
     * finds the index of the next comma
     * @return Integer of the index
     */
    private int findNextComma() throws NTMSimError {
        for (int i = 0; i < inputCopy.length(); i++){
            if (inputCopy.charAt(i) == ','){
                return i;
            }
        }
        throw new NTMSimError("Code expects a comma.");
    }

    /**
     * getter for the alphabet
     * @return the alphabet as ArrayList
     */
    public ArrayList<Character> getAlphabet(){
        return alphabet;
    }

    /**
     * getter for symbols
     * @return the symbols as ArrayList
     */
    public ArrayList<Character> getSymbols(){
        return symbols;
    }

    /**
     * getter for the start point
     * @return the name of the start
     */
    public String getStart(){
        return start;
    }

    /**
     * getter for the end point
     * @return the name of the end
     */
    public String getEnd(){
        return end;
    }

    /**
     * getter for sim, that is just grammar without input
     * @return a Sim that's the grammar of what has been parsed
     */
    public Sim getGrammar(){
        return grammar;
    }

    /**
     * same functionality as getGrammar
     * getter for sim, that just gets the grammar without input
     * @return a Sim with no set Grammar
     */
    public Sim getSim(){
        return grammar;
    }
}