package util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class Api
{
    private String baseUrl;
    private static HttpURLConnection conn;

    public Api(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    /**
     * Performs a GET request to the specified resource.
     * @param resource The API resource to ping, e.g. "items".
     * @param queryParams A map of the query parameters.
     * @return A String containing the response from the API.
     * @throws IOException Returned if the URL is malformed or did not return a good response.
     */
    public String get(String resource, Map<String, String> queryParams) throws IOException
    {
        StringBuilder response;

        try
        {
            URL url = new URL(baseUrl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(queryParams));
            out.flush();
            out.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            response = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                response.append(line);
                response.append(System.lineSeparator());
            }
        }
        finally
        {
            conn.disconnect();
        }

        return response.toString();
    }
}
