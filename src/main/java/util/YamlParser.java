package util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class YamlParser
{
    public static String getProperty(String key)
    {
        String[] elements = key.split("\\.");

        Yaml yaml = new Yaml();
        InputStream is = YamlParser.class.getClassLoader().getResourceAsStream("config.yaml");
        Map<String, Object> yamlProps = yaml.load(is);
        Object value = yamlProps.get(elements[0]);

        for (int i = 1; i < elements.length; i++)
        {
            value = ((Map<String, Object>)value).get(elements[i]);
        }

        return value.toString();
    }
}
