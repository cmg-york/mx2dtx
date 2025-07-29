package ca.yorku.cmg.istardt.reader.outputs.common;

public class ColorPrinter {
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[94m";

    public static String paintRed(String arg) {
        return (RED + arg + RESET);
    }
    
    public static String paintYellow(String arg) {
        return (YELLOW + arg + RESET);
    }
    
    public static String paintBlue(String arg) {
        return (BLUE + arg + RESET);
    }
    
}
