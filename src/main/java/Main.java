import util.Defs;
import parsers.HtmlParser;
import java.io.IOException;
import java.util.Scanner;

public class Main
{
    /**
     * Main method.
     * @param args
     */
    public static void main(String[] args)
    {
        Defs.init();
        String text = getTextFromURL();
    }

    /**
     * Receives url from user as input and returns the text as the content of the tag global variable.
     * @return The content inside the <tag></tag> elements, where tag is specified as a global variable.
     */
    public static String getTextFromURL()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the URL of the Wiki edit page: ");
        String url = sc.next();
        String text = "";

        try
        {
            text = HtmlParser.returnTagContent(url, Defs.tag);
        }
        catch (IOException e)
        {
            System.out.println("IO Error: " + e.getMessage());
        }

        return text;
    }


}