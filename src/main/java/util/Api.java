package util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Api
{
    private String baseUrl;
    private String language;
    private HttpClient client;

    public Api(String baseUrl)
    {
        this.baseUrl = baseUrl;
        this.client = HttpClient.newHttpClient();
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    /**
     * Returns a List of a map of strings based on the API replacements specified.
     * @param ids A list of IDs to search the API for.
     * @param apiReplacements A map where each key is the key name in the wiki edit page and each value is the key in
     *                        the API.
     * @return A list of string maps where each key is the row name .
     * @throws IOException Returned if the URL is malformed or did not return a good response.
     */
    public List<Map<String, String>> getResourceFields(int[] ids, Map<String, String> apiReplacements) throws IOException, InterruptedException
    {
        Map<String, String> queryParams = new LinkedHashMap<String, String>();
        String idsParameter = "";

        for (int i : ids)
        {
            idsParameter += i + ",";
        }

        idsParameter = idsParameter.substring(0, idsParameter.length() - 1); // Remove last comma.
        queryParams.put(Defs.idsKey, idsParameter);
        queryParams.put(Defs.languageKey, language);
        List<Map<String, String>> replacements = new ArrayList<Map<String, String>>();

        String response = get(Defs.resource, queryParams);
        JSONArray jsonArray = new JSONArray(response);

        for (int i = 0, j = 0; i < ids.length; i++)
        {
            JSONObject jsonObject = new JSONObject();
            if (i < jsonArray.length())
            {
                jsonObject = jsonArray.getJSONObject(j);
            }

            replacements.add(new LinkedHashMap<String, String>());
            boolean found = false;

            for (Map.Entry<String, String> innerEntry : apiReplacements.entrySet())
            {
                if (i >= jsonArray.length())
                {
                    replacements.get(i).put(innerEntry.getKey(), "");
                }
                else if (jsonObject.getInt("id") != ids[i])
                {
                    replacements.get(i).put(innerEntry.getKey(), "");
                }
                else
                {
                    replacements.get(i).put(innerEntry.getKey(), jsonObject.getString(innerEntry.getValue()));
                    found = true;
                }
            }

            if (found)
            {
                j++;
                found = false;
            }
        }

        return replacements;
    }

    /**
     * Performs a GET request to the specified resource.
     * @param resource The API resource to ping, e.g. "items".
     * @param queryParams A map of the query parameters.
     * @return A String containing the response from the API.
     * @throws IOException Returned if the URL is malformed or did not return a good response.
     */
    private String get(String resource, Map<String, String> queryParams) throws IOException, InterruptedException
    {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + resource + "?" + ParameterStringBuilder.getParamsString(queryParams)))
                .GET()
                .build();

        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());

        return httpResponse.body();
    }
}
