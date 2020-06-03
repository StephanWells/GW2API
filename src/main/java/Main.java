import parsers.HtmlParser;
import util.Defs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
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
        text = replaceTextWithApiReplacements(text);
        text = replaceTextWithDirectReplacements(text);
        writeTextToFile(text);
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

    public static String replaceTextWithApiReplacements(String text)
    {
        try
        {
            text = HtmlParser.apiReplacements(text);
        }
        catch (IOException e)
        {
            System.out.println("IO Error: " + e.getMessage());
        }
        catch (InterruptedException e)
        {
            System.out.println("Connection Interrupted: " + e.getMessage());
        }

        return text;
    }

    private static String replaceTextWithDirectReplacements(String text)
    {
        for (Map.Entry<String, String> entry : Defs.replacements.entrySet())
        {
            text = text.replace(entry.getKey(), entry.getValue());
        }

        return text;
    }

    private static void writeTextToFile(String text)
    {
        try (PrintWriter out = new PrintWriter(Defs.saveFile))
        {
            out.println(text);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("FileNotFound Error: " + e.getMessage());
        }
    }
}