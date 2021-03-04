package sdis.proj; 

public class Utils { 
    // Reset 
    public static final String RESET =  "\033[0m";   
    // Colors 
    public static final String RED =  "\033[0;31m"; 

    public static void errorText(String ...args){ 
        System.out.println(RED + args + RESET); 
    }

}
