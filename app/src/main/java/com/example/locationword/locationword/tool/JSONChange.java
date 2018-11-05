package com.example.locationword.locationword.tool;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONChange {
    public static JsonObject StringToJsonObject(String s){
        JsonParser jp =new JsonParser();
        return jp.parse(s).getAsJsonObject();
    }
    public static JsonArray StringToJsonArrary(String s){
        JsonParser jp =new JsonParser();
        return jp.parse(s).getAsJsonArray();
    }
}
