package src;

/**
 * class for errors and their display
 */

public class NTMSimError extends Throwable{
    /**
     * constructor for the error message if smth goes wrong in the simulator
     * @param message given message for the error display
     */
    public NTMSimError(String message){
        super(message);
    }

    /**
     * returns the error message
     * @return String of the message
     */
    public String getMessage(){
        return super.getMessage();
    }
}
