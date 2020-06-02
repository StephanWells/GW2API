package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser
{
    public static final String IDS_REGEX = Defs.idKey + "\\s*=\\s*([^|}\\n ]*)\\s*[|}]";

    /**
     * Returns the content within the specified tag.
     * @param url The URL to get the HTTP source from.
     * @param tag The HTML tag to look for, e.g. "textarea".
     * @return The text content within the <tag></tag> element.
     * @throws IOException
     */
    public static String returnTagContent(String url, String tag) throws IOException
    {
       Document wikiPage = Jsoup.connect(url).get();
       Element text = wikiPage.selectFirst(tag);

       return text.text();
    }

    public static String apiReplacements(String text)
    {
        int[] ids = getIDsFromText(text);

        return "";
    }

    /**
     * Reads through text from the wiki edit page and returns an ordered list of item IDs.
     * @param text The text from the wiki edit page as a complete string with line breaks.
     * @return An ordered list of item IDs to ping the API with.
     */
    private static int[] getIDsFromText(String text)
    {
        String[] lines = text.split("\n");
        Pattern idsPattern = Pattern.compile(IDS_REGEX);
        List<Integer> ids = new ArrayList<Integer>();

        for (String line : lines)
        {
            try
            {
                Matcher matcher = idsPattern.matcher(line);
                matcher.find();
                ids.add(Integer.parseInt(matcher.group(1)));
            }
            catch (IllegalStateException e)
            {
                continue;
            }
            catch (NumberFormatException e)
            {
                System.out.println("Warning! The following line has an incorrectly-formatted ID:");
                System.out.println(line);
                continue;
            }
        }

        return ids.stream()
                .mapToInt(i->i)
                .toArray();
    }
}
