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

    public Command(String name, char currentChar, String nextCommand, char replaceChar) {}

    public char getCurrentChar() {
        return currentChar;
    }

    public String getNextCommand() {
        return nextCommand;
    }

    public char getReplaceChar() {
        return replaceChar;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getName() {
        return name;
    }


}
