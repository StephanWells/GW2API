import util.HtmlParser;

import java.io.IOException;
import java.util.Scanner;

public class Main
{
    public static final String tag = "textarea"

    public static void main(String[] args)
    {

    }

    public static String getTextFromURL()
    {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter the URL of the Wiki edit page: ");
        String url = sc.next();

        try
        {
            String text = HtmlParser.returnTagContent(url, tag);
        }
        catch (IOException e)
        {
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}