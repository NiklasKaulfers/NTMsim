package src;
/**
 * an enum of all directions the TM can go
 */
public enum Direction {
    /**
     * to read to the left in the TM
     */
    LEFT ('<', "left"),
    /**
     * to read to the right in the TM
     */
    RIGHT('>', "right"),
    /**
     * to stay in place in the TM
     */
    STAY('_', "stay");
    /**
     * the char for the user input
     */
    final char direction;
    /**
     * to return the name
     */
    final String name;

    /**
     * creates a direction
     * @param direction shows where the TM will go
     * @param name returns name for debugging
     */
    Direction(char direction, String name){
        this.direction = direction;
        this.name = name;
    }

    /**
     * to get the name of the direction
     * @return returns the name as a String
     */
    public String getName(){
        return name;
    }

    /**
     * to get the char representing the direction
     * @return char representing the direction
     */
    public char getDirection(){
        return direction;
    }
}
