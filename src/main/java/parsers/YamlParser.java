package util;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class YamlParser
{
    /**
     * Given a key, access the config.yml resource and return the value.
     * @param key The key uses . to access different parts of the YAML hierarchy, e.g. settings.tag.
     *            Returns null if not found.
     * @return Returns an object that is either a String, boolean, or ArrayList<Map<String, String>>.
     * @throws YAMLException Thrown if the file is not found or can't be parsed into YAML.
     */
    public static Object getProperty(String key) throws YAMLException
    {
        String[] elements = key.split("\\.");

        Yaml yaml = new Yaml();
        InputStream is = YamlParser.class.getClassLoader().getResourceAsStream("config.yml");
        Map<String, Object> yamlProps = yaml.load(is);
        Object value = yamlProps.get(elements[0]);

        for (int i = 1; i < elements.length; i++)
        {
            value = ((Map<String, Object>)value).get(elements[i]);
        }

        return value;
    }
}
