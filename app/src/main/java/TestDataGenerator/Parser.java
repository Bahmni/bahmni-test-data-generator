package TestDataGenerator;

import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Parser
{
    private static String response;
    public Parser(String response)
    {
        this.response=response;
    }

    public  String getString(String path)
    {
        JSONObject root=new JSONObject(response);
        return root.getString(path);
    }
    public  Object get(String path)
    {
        JSONObject root=new JSONObject(response);
        return root.get(path);
    }
        public  JSONArray getJSONArray(String path)
    {
        JSONObject root=new JSONObject(response);
        return root.getJSONArray(path);
    }
    public static String getStringFromArray(String path)
    {
        JSONArray jrr= new JSONArray(response);
        JSONObject job=(JSONObject) jrr.get(0);
        return job.getString(path);

    }

    public List<String> getValuesForGivenKey(String key)
    {
        JSONObject root=new JSONObject(response);
        JSONArray jsonArray = root.getJSONArray("results");
        return IntStream.range(0, jsonArray.length())
                .mapToObj(index -> ((JSONObject)jsonArray.get(index)).optString(key))
                .collect(Collectors.toList());
    }
    public static JSONObject getJsonObject()
    {
        return new JSONObject(response);
    }
    public static  Map<String, String> zipToMap(List<String> keys, List<String> values) {
        Iterator<String> keyIter = keys.iterator();
        Iterator<String> valIter = values.iterator();
        return IntStream.range(0, keys.size()).boxed()
                .collect(Collectors.toMap(_i -> keyIter.next(), _i -> valIter.next()));
    }

}
