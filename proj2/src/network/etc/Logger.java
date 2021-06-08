package network.etc;

public class Logger {
    static Boolean show = true;
    static Boolean show_err = true;
    static Boolean show_suc = true;
    static Boolean show_info = true;
    static Boolean show_any = true;
    static Boolean show_abort_send = true;
    static Boolean show_request= true;
        
    /**
    * Create a logger message when some error occur
    * @param className name of class where logger was invoked
    * @param err Error message to be displayed
    */
    public static void ERR(String className, String err){
        if (show && show_err) System.err.println("[ ERR     ] " + className + " - " + err);
    }

    /**
    * Create a success logger message
    * @param className name of class where logger was invoked
    * @param message Message with information to be display
    */
    public static void SUC(String className, String message){
        if (show && show_suc) System.err.println("[     SUC ] " + className + " - " + message);
    }

    /**
    * Create a information logger message
    * @param className name of class where logger was invoked
    * @param message Message with information to be display
    */
    public static void INFO(String className, String message){
        if (show && show_info) System.err.println("[  INFO   ] " + className + " - " + message);
    }

    /**
    * Miscellaneous logger message
    * @param className name of class where logger was invoked
    * @param message Message with information to be display
    */
    public static void ANY(String className, String message){
        if (show && show_any) System.err.println("[=========] " + className + " - " + message);
    }

    /**
    * Abort a send message 
    * @param className name of class where logger was invoked
    * @param message Message with information to be display
    */
    public static void ABORT_SEND(String className, String message){
        if (show && show_abort_send) System.err.println("[ ABORT   ] " + className + " - " + message);
    }

    /**
    * Request logger message 
    * @param className name of class where logger was invoked
    * @param message Message with information to be display
    */
    public static void REQUEST(String className, String message){
        if (show && show_request) System.err.println("[ REQUEST ] " + className + " - " + message);
    }
}
