package src;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws NTMSimError, IOException {
        Parser p = new Parser(new File("TestText.txt"));
        p.parse();
        Sim s = p.getSim();
        s.setInput("ab");
        boolean a = s.solve();
        System.out.println("a: " + a);
        System.out.println(p.getStart() + ": " + p.getEnd());

    }
}