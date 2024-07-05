import src.*;

public class Main {
    public static void main(String[] args) throws NTMSimError {
        String in = ".init: start:q; end: q2; alphabet: a,b; symbols: a,b,c; .code: q,a,q1,a, >; q1,b,q2,b,>;";
        Parser p = new Parser(in);
        p.parse();
        Sim s = p.getSim();
        s.setInput("ab");
        boolean a = s.solve();
        System.out.println("a: " + a);
        System.out.println(p.getStart() + ": " + p.getEnd());

    }
}