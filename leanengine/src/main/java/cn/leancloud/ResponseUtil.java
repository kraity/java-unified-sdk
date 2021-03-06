package cn.leancloud;


import cn.leancloud.ops.Utils;
import cn.leancloud.types.AVGeoPoint;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class ResponseUtil {
  private static final String TYPE = "__type";

  public static String filterResponse(String response) {
    JSONObject resp = JSON.parseObject(response, JSONObject.class);
    Object result = resp.get("result");
    if (result instanceof JSONObject) {
      removeType((JSONObject) result);
    } else if (result instanceof JSONArray) {
      for (Object o : (JSONArray) result) {
        if (o instanceof JSONObject) {
          removeType((JSONObject) o);
        }
      }
    }
    return restfulCloudData(resp);
  }

  private static void removeType(JSONObject object) {
    if (object.containsKey("className") && object.containsKey(TYPE)) {
      object.remove("className");
      object.remove(TYPE);
    }
  }

  static String restfulCloudData(Object object) {
    if (object == null)
      return "{}";
    if (object instanceof Map) {
      return jsonStringFromMapWithNull(Utils.getParsedMap((Map<String, Object>) object, true));
    } else if (object instanceof Collection) {
      return jsonStringFromObjectWithNull(Utils.getParsedList((Collection) object, true));
    } else if (object instanceof AVObject) {
      return jsonStringFromMapWithNull(Utils.mapFromAVObject((AVObject) object, true));
    } else if (object instanceof AVGeoPoint) {
      return jsonStringFromMapWithNull(Utils.mapFromGeoPoint((AVGeoPoint) object));
    } else if (object instanceof Date) {
      return jsonStringFromObjectWithNull(Utils.mapFromDate((Date) object));
    } else if (object instanceof byte[]) {
      return jsonStringFromMapWithNull(Utils.mapFromByteArray((byte[]) object));
    } else if (object instanceof AVFile) {
      return jsonStringFromMapWithNull(((AVFile) object).toMap());
    } else if (object instanceof JSONObject) {
      return jsonStringFromObjectWithNull(JSON.parse(object.toString()));
    } else if (object instanceof JSONArray) {
      return jsonStringFromObjectWithNull(JSON.parse(object.toString()));
    } else {
      return jsonStringFromObjectWithNull(object);
    }
  }
  static String jsonStringFromMapWithNull(Object map) {
    return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullNumberAsZero);
  }

  static String jsonStringFromObjectWithNull(Object map) {
    return JSON.toJSONString(map, SerializerFeature.WriteMapNullValue,
            SerializerFeature.WriteNullBooleanAsFalse, SerializerFeature.WriteNullNumberAsZero);
  }
}
