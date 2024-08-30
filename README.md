<h1>simple non deterministic turing machine simulator</h1><br>

<h2>TODO:</h2>
implementation of Epsilon <br>
implementation of a GUI representing runs and allowing changes <br>
<h2>what to change</h2>
the TestText.txt file contains example code, replace whats in there with ur code and it will run <br>

<h2>how to install</h2>
use: <br>
javac *.java <br>
java Main <br><br>
if something goes wrong or it shows a file is missing just compile that file with: <br>
javac MissingFile.java<br><br>



<h2>syntax:</h2>
here is how the syntax would look like replace the given cariable names with ones you like <br><br>
.init: <br>
start: <u>your start</u>; <br>
end: <u>your end</u>; <br>
alphabet: <u>char1, char2, ...</u>; <br>
symbols: <u>char1, char2, char3,...</u>; <br>
.code: <br>
</u>currentState, currentChar, nextState, nextChar, (< or > or _)</u>; <br><br>
for the direction use <, > or _ to show where the tm should read next <br>
the order in init or in code doesnt matter, but the .init part has to come before the .code part <br>
you need 1 start point and 1 end point, whenever the next command is read as end while going through ur tm it will terminate successfully <br>
the size of the alphabet or the symbols doesnt matter, should be bigger then 1 to make a functional program tho <br>
in code u have to create the commands for each situationhere is also recommeneded a size of atleast 2 so it does smth <br>
whitespaces can be placed however you want to, the parser will remove them within itself anyways <br>
<br> 
example : <br><br> .init: start:q; end: q2; alphabet: a,b; symbols: a,b,c; .code: q,a,q1,a, >; q1,b,q2,b,>;<br><br>
this works <br>
<br>.init: ; symbols: a,b,c <br>
  end: q2; alphabet: a,b; start: q; .code: q, <br>
  a,q1,a, >; q1,b,q2,b,>; <br>
  <br>
  so does this even tho it doesnt look good <br>
