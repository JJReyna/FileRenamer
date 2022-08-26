//borrowed from StackOverflow:
//stackoverflow.com/questions/228477/how-do-i-programmatically-determine-operating-system-in-java
public class OsUtils{

    private static String OS = null;

    public static String getOSName(){
        if(OS == null){OS = System.getProperty("os.name");}
        return OS;
    }

    public static boolean isWindows(){
        return getOSName().startsWith("Windows");
    }

    public static boolean isUnix(){
        return getOSName().startsWith("Linux");
    }
}