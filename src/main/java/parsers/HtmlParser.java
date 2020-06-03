package parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.Api;
import util.Defs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser
{
    public static final String IDS_REGEX = Defs.idKey + "\\s*=\\s*([^|}\\n ]*)\\s*[|}]";
    public static final String FIELD_REGEX_SUFFIX = "\\s*=\\s*([^|}\\n]*)\\s*[|}]";

    /**
     * Returns the content within the specified tag.
     * @param url The URL to get the HTTP source from.
     * @param tag The HTML tag to look for, e.g. "textarea".
     * @return The text content within the <tag></tag> element.
     * @throws IOException Thrown if the HTML page could not be read.
     */
    public static String returnTagContent(String url, String tag) throws IOException
    {
       Document wikiPage = Jsoup.connect(url).get();
       Element text = wikiPage.selectFirst(tag);

       return text.text();
    }

    public static String apiReplacements(String text) throws IOException, InterruptedException
    {
        int[] ids = getIDsFromText(text);
        List<Map<String, String>> replacements = getStringsFromIds(ids, Defs.apiReplacements);
        String[] lines = text.split("\n");
        String result = "";
        Pattern idsPattern = Pattern.compile(IDS_REGEX);
        Map<String, Pattern> regexPatterns = new LinkedHashMap<>();
        int counter = 0;

        for (Map.Entry<String, String> innerEntry : Defs.apiReplacements.entrySet())
        {
            regexPatterns.put(innerEntry.getKey(), Pattern.compile(innerEntry.getKey() + FIELD_REGEX_SUFFIX));
        }

        for (String line : lines)
        {
            Matcher idsMatcher = idsPattern.matcher(line);
            String newLine = line;
            StringBuffer replacement = new StringBuffer();

            if (idsMatcher.find())
            {
                for (Map.Entry<String, String> innerEntry : Defs.apiReplacements.entrySet())
                {
                    Matcher fieldMatcher = regexPatterns.get(innerEntry.getKey()).matcher(newLine);
                    fieldMatcher.find();
                    fieldMatcher.appendReplacement(replacement,
                            fieldMatcher.group(1).replaceFirst(Pattern.quote(fieldMatcher.group(1)),
                                    innerEntry.getKey() + " = " + replacements.get(counter).get(innerEntry.getKey()) + " |"));
                    fieldMatcher.appendTail(replacement);
                    newLine = replacement.toString();
                }

                counter++;
            }

            result += newLine + System.lineSeparator();
        }

        return result;
    }

    private static List<Map<String, String>> getStringsFromIds(int[] ids, Map<String, String> apiReplacements) throws IOException, InterruptedException
    {
        List<Map<String, String>> replacements;
        Api gw2Api = new Api(Defs.baseUrl);
        gw2Api.setLanguage(Defs.language);
        replacements = gw2Api.getResourceFields(ids, apiReplacements);

        return replacements;
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
