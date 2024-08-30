import src.NTMSimError;
import src.Parser;
import src.Sim;

import java.io.File;
import java.io.IOException;

/**
 * opens the sim
 */
public class Main {
    public static void main(String[] args) throws NTMSimError, IOException {
        File f = new File("TestText.txt");
        boolean b = f.exists();
        Parser p;
        if (b) {
            p = new Parser(f);
        } else {
            System.out.println("error");
            p = new Parser("""
                    .init:
                    start:q;
                    end: q2;
                    alphabet: a,b;
                    symbols: a,b,c;
                    .code:
                    q,a,q1,a, >;
                    q1,b,q2,b,>;""");
        }
        p.parse();
        Sim s = p.getSim();
        s.setInput("ab");
        boolean a = s.solve();
        System.out.println("a: " + a);
        System.out.println(s.getLog());

    }
}