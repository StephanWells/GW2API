package util;

import org.yaml.snakeyaml.error.YAMLException;
import parsers.YamlParser;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Defs
{
    public static String tag;
    public static String language;
    public static String saveFile;
    public static String idKey;
    public static String baseUrl;
    public static String resource;
    public static String idsKey;
    public static String languageKey;
    public static LinkedHashMap<String, String> apiReplacements;
    public static LinkedHashMap<String, String> replacements;

    /**
     * Populates the properties specified as global variables from config.yml.
     */
    public static void init()
    {
        try
        {
            tag = (String) YamlParser.getProperty("settings.tag");
            language = (String)YamlParser.getProperty("settings.language");
            saveFile = (String)YamlParser.getProperty("settings.saveFile");
            idKey = (String)YamlParser.getProperty("settings.idKey");
            baseUrl = (String)YamlParser.getProperty("settings.api.baseUrl");
            resource = (String)YamlParser.getProperty("settings.api.resource");
            idsKey = (String)YamlParser.getProperty("settings.api.queryParameterKeys.ids");
            languageKey = (String)YamlParser.getProperty("settings.api.queryParameterKeys.language");
            apiReplacements = flattenMap((ArrayList<LinkedHashMap<String, String>>)YamlParser.getProperty("apiReplacements"));
            replacements = flattenMap((ArrayList<LinkedHashMap<String, String>>)YamlParser.getProperty("replacements"));
        }
        catch (YAMLException e)
        {
            System.out.println("Error reading YAML config data! " + e.getMessage());
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error: cannot find config.yml! " + e.getMessage());
        }
    }

    /**
     * Retrieves a single map from a list of maps.
     * @param map A list of maps.
     * @return A map.
     */
    private static LinkedHashMap<String, String> flattenMap(List<LinkedHashMap<String, String>> map)
    {
        LinkedHashMap<String, String> flattenedMap = new LinkedHashMap<String, String>();

        for (Map<String, String> entry : map)
        {
            for (Map.Entry<String, String> innerEntry : entry.entrySet())
            {
                flattenedMap.put(innerEntry.getKey(), innerEntry.getValue());
            }
        }

        return flattenedMap;
    }
}
