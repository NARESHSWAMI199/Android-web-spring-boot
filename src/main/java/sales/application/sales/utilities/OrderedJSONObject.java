package sales.application.sales.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class OrderedJSONObject extends JSONObject {

    LinkedHashMap<String,Object> map;


    public OrderedJSONObject() {
        this.map = new LinkedHashMap<>();
    }


    @Override
    public JSONObject put(String key, Object value) throws JSONException {
        super.put(key,value);
        if (key == null) {
            throw new NullPointerException("Null key.");
        }
        if (value != null) {
            testValidity(value);
            this.map.put(key, value);
        } else {
            this.remove(key);
        }
        return this;
    }


    @Override
    public Object remove(String key) {
        return this.map.remove(key);
    }

    @Override
    protected Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

}
