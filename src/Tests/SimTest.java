

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import src.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimTest {

    @Test
    public void testSolve_SuccessfulPath() {
        // Define input data for the test
        String start = "start";
        String end = "end";
        ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<Character> symbols = new ArrayList<>(Arrays.asList('X', 'Y', 'Z'));

        // Create commands
        Command command1 = new Command("start", 'a', "middle", 'X', Direction.RIGHT);
        Command command2 = new Command("middle", 'b', end, 'Y', Direction.STAY);
        ArrayList<Command> commands = new ArrayList<>(Arrays.asList(command1, command2));

        String input = "ab";

        // Create a new instance of Sim
        Sim sim = new Sim(start, end, alphabet, symbols, commands, input);

        // Call the method to be tested
        boolean result = sim.solve();

        // Assert the result
        assertTrue( "Expected the solve method to return true for successful path.", result);
    }

    @Test
    public void testSolve_UnsuccessfulPath() {
        // Define input data for the test
        String start = "start";
        String end = "end";
        ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a', 'b', 'c'));
        ArrayList<Character> symbols = new ArrayList<>(Arrays.asList('X', 'Y', 'Z'));

        // Create commands
        Command command1 = new Command("start", 'a', "middle", 'X', Direction.RIGHT);
        ArrayList<Command> commands = new ArrayList<>(List.of(command1));

        String input = "ab";

        // Create a new instance of Sim
        Sim sim = new Sim(start, end, alphabet, symbols, commands, input);

        // Call the method to be tested
        boolean result = sim.solve();

        // Assert the result
        assertFalse(result, "Expected the solve method to return false for unsuccessful path.");
    }
    @Test
    public void testSolve_SeveralPaths(){
        // this is a TM for 2 chars
        // it shows "XOR" as an "AND" or "OR"
        String start = "start";
        String end = "end";
        ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> symbols = new ArrayList<>(alphabet);
        symbols.add('X');

        ArrayList<Command> commands = new ArrayList<>();
        Command command1 = new Command(start, 'a', "q1", 'a', Direction.RIGHT);
        Command command2 = new Command("q1", 'a', "end", 'a', Direction.STAY);
        commands.add(command1);
        commands.add(command2);

        Command command4 = new Command(start, 'a', "q2", 'a', Direction.RIGHT);
        Command command5 = new Command("q2", 'b', end, 'b', Direction.STAY);
        commands.add(command4);
        commands.add(command5);

        Command command6 = new Command(start, 'b', "q3", 'b', Direction.RIGHT);
        Command command7 = new Command("q3", 'a', "end", 'a', Direction.STAY);
        commands.add(command6);
        commands.add(command7);

        String input = "ab";

        Sim sim = new Sim(start, end, alphabet, symbols, commands, input);
        sim.setResearchDepth(1);
        boolean result = sim.solve();

        Assertions.assertTrue(result, "Success");
    }
    @Test
    public void testSolve_SeveralPaths_Copy(){
        // this is a TM for 2 chars
        // it shows "XOR" as an "AND" or "OR"
        String start = "start";
        String end = "end";
        ArrayList<Character> alphabet = new ArrayList<>(Arrays.asList('a', 'b'));
        ArrayList<Character> symbols = new ArrayList<>(alphabet);
        symbols.add('X');

        ArrayList<Command> commands = new ArrayList<>();
        Command command1 = new Command(start, 'a', "q1", 'a', Direction.RIGHT);
        Command command2 = new Command("q1", 'a', "end", 'a', Direction.STAY);
        commands.add(command1);
        commands.add(command2);

        Command command4 = new Command(start, 'a', "q2", 'a', Direction.RIGHT);
        Command command5 = new Command("q2", 'b', end, 'b', Direction.STAY);
        commands.add(command4);
        commands.add(command5);

        Command command6 = new Command(start, 'b', "q3", 'b', Direction.RIGHT);
        Command command7 = new Command("q3", 'a', "end", 'a', Direction.STAY);
        commands.add(command6);
        commands.add(command7);

        String input = "ab";

        Sim sim = new Sim(start, end, alphabet, symbols, commands, input);
        sim.setResearchDepth(1);
        boolean result = sim.solve();

        Assertions.assertTrue(result, "Success");
    }
}
