package src;

/**
 *
 */
public class Command {
    String name;
    char currentChar;
    String nextCommand;
    char replaceChar;
    Direction direction;
    public Command(String name, char currentChar, String nextCommand, char replaceChar, Direction direction) {
        this.name = name;
        this.currentChar = currentChar;
        this.nextCommand = nextCommand;
        this.replaceChar = replaceChar;
        this.direction = direction;
    }

    /**
     * getter for the char the TM is at right now
     * @return the looked at char
     */
    public char getCurrentChar() {
        return currentChar;
    }

    /**
     * getter for the next Command that will get executed
     * @return the name of the Command
     */
    public String getNextCommand() {
        return nextCommand;
    }

    /**
     * getter for the char that will replace the current char
     * @return the next char that will be at the current position
     */
    public char getReplaceChar() {
        return replaceChar;
    }

    /**
     * gets the direction the TM will walk into
     * @return a Direction (Direction. LEFT, Direction. RIGHT, Direction. STAY)
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * getter for the name of the Command
     * @return the Commands name
     */
    public String getName() {
        return name;
    }


}
