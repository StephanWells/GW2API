package util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class HtmlParser
{
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
}
