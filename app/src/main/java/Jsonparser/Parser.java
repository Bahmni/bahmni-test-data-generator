package Jsonparser;

import Config.LoggerConfig;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser {
    Logger logger = LoggerConfig.LOGGER;
    private static String response;

    public Parser(HttpResponse response) {
        try {
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            Parser.response = responseHandler.handleResponse(response);
        } catch (IOException e) {
            logger.severe("Response could'nt be parsed " + e.getLocalizedMessage());
        }
    }

    public Object get(String path) {
        JSONObject root = new JSONObject(response);
        return root.get(path);
    }

    public String getStringFromArray(String path) {
        JSONArray jrr = new JSONArray(response);
        JSONObject job = (JSONObject) jrr.get(0);
        return job.getString(path);

    }

    public List<String> getValuesForGivenKey(String key) {
        JSONObject root = new JSONObject(response);
        JSONArray jsonArray = root.getJSONArray("results");
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject) jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
    }

    public static Map<String, String> zipToMap(List<String> keys, List<String> values) {
        Iterator<String> keyIter = keys.iterator();
        Iterator<String> valIter = values.iterator();
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(_i -> keyIter.next(), _i -> valIter.next()));
    }

}
