package com.qa.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.qa.util.Events;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonHelper {
    public static enum EnumJsonCompare {
        isFoundInLeft, isFoundInRight, areFoundNodesEqual;
    }

    /**
     * Convert string to json node
     *
     * @param response
     * @return
     * @throws IOException
     * @throws JsonProcessingException
     */
    public static JsonNode getJsonNodeFromString(String response) throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode responseNode = mapper.readTree(response);
        return responseNode;
    }

    /**
     * Find json object form a json array. 
     * This will iterate over all the json objects in the array. 
     * Get value of the key for each object. 
     * If the value in the object matches with the value which is being searched, the object in returned.
     * Please see the examples bellow to understand it better.
     *
     * Ex: array: [{ "id" : "1", "name":"Asset1"},{ "id" : "2", "name":"Asset2"},{ "id" : "3", "name":"Asset3"}]
     * key: id
     * value: 3
     * returned value: { "id" : "3", "name":"Asset3"}
     *
     * Ex2: array: [{ "id" : "1", "name":"Asset1", "group":{"group_id":"a", "group_name":"group a"} },{ "id" : "2", "name":"Asset2", "group":{"group_id":"b", "group_name":"group b"} },{ "id" : "3", "name":"Asset3", "group":{"group_id":"c", "group_name":"group c"} }]
     * Key: group.group_id
     * value: b
     * Returned value: { "id" : "2", "name":"Asset2", "group":{"group_id":"b", "group_name":"group b"} }
     *
     * @param array: json node array
     * @param key: Key to be used to search the json node
     * @param value: Value to be used to search the json node
     * @return
     */
    public static JsonNode getJsonNodeFromJsonArray(JsonNode array, String key, String value) {
        int size = array.size();
        Events.info("Array: " + array);
        Events.info("Key: " + key);
        Events.info("Value: " + value);

        String[] nodeHierarchy = key.split("\\.");
        for (int i = 0; i < size; i++) {
            JsonNode jsonNode = array.get(i);
            JsonNode subNode = jsonNode;
            for (int j = 0; j < nodeHierarchy.length; j++) {
                if (subNode == null) {
                    continue;
                }
                subNode = subNode.get(nodeHierarchy[j]);
            }
            if (subNode != null) {
                String actualValue = subNode.asText();
                Events.info("getJsonNodeFromJsonArray: " + value + " : " + actualValue);
                if (value.equalsIgnoreCase(actualValue)) {
                    return jsonNode;
                }
            }
        }
        return null;
    }

    /**
     * Get child node from the json node. 
     *
     * Ex2: jsonNode: { "id" : "1", "name":"Asset1", "group":{"group_id":"a", "group_name":"group a"} }
     * childPath: group.group_id
     * Returned value: a
     *
     * @param jsonNode
     * @param childPath
     * @return
     */
    public static JsonNode getChildValueFromJsonNode(JsonNode jsonNode, String childPath) {

        String[] nodeHierarchy = childPath.split("\\.");

        JsonNode subNode = jsonNode;
        for (int j = 0; j < nodeHierarchy.length; j++) {
            subNode = subNode.get(nodeHierarchy[j]);
        }
        if (subNode != null) {
            return subNode;
        }
        return null;
    }

    /**
     * method to return json from string .
     *
     * @param converttojson
     * @return
     */
    public static JsonNode returnJsonFromString(final String converttojson) throws JsonProcessingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonNode rootarray = mapper.readTree(converttojson);
        return rootarray;
    }

    /**
     * method to return json from string .
     *
     * @param convertToJsonElement
     * @return
     */
    public static JsonElement returnJsonElementFromString(final String convertToJsonElement) throws JsonProcessingException, IOException {
        return new JsonParser().parse(convertToJsonElement);
    }

    /**
     * Method to verify specified value of an element in an array.
     *
     * @param -json  Array
     * @param key
     * @param value
     * @return
     */
    public Boolean isKeyValuePresentInArrayNode(ArrayNode node, String key, String value) throws JsonProcessingException {
        final Iterator<JsonNode> iterator = node.elements();
        Boolean success = false;
        while (iterator.hasNext()) {
            final JsonNode bodyJsonElement = iterator.next();
            if (bodyJsonElement.has(key)) {
                final String singleElement = bodyJsonElement.get(key).asText();
                if (singleElement.contains(value)) {
                    if (singleElement.contains(value)) {
                        Events.info("Value of the element : " + singleElement);
                        success = true;
                    }
                }
            }
        }
        return success;
    }

    /**
     *
     * @param checkJsonString
     * @param referenceJsonString
     * @param nodePrimaryKey
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static boolean compareJson1NodesInJson2Nodes(String checkJsonString, String referenceJsonString, String nodePrimaryKey)
            throws JsonProcessingException, IOException {
        boolean areJSONsEqual = true;
        JsonNode jSON_1 = getJsonNodeFromString(checkJsonString);
        JsonNode jSON_2 = getJsonNodeFromString(referenceJsonString);
        if (!jSON_1.isNull() && !jSON_2.isNull()) {
            if (jSON_1.isArray()) {
                Events.info("JSON_1 is an ARRAY. Checking if JSON_1 nodes are present in JSON_2 based on PrimaryKey: " + nodePrimaryKey);
                for (int i = 0; i < jSON_1.size(); i++) {
                    String node1PrimaryKeyValue = jSON_1.get(i).get(nodePrimaryKey).asText();
                    JsonNode node2Compare = getJsonNodeFromJsonArray(jSON_2, nodePrimaryKey, node1PrimaryKeyValue);
                    if (JSONObject.NULL.equals(node2Compare)) {
                        areJSONsEqual = false;
                        Events.info("JSON_1 node with \"" + nodePrimaryKey + "\"=\"" + node1PrimaryKeyValue + "\" not found in JSON_2.");
                        continue;
                    }
                    areJSONsEqual = Boolean.logicalAnd(areJSONsEqual, compareJsonNodes(jSON_1.get(i), node2Compare));
                }
            } else {
                areJSONsEqual = compareJsonNodes(jSON_1, jSON_2);
            }
        } else {
            areJSONsEqual = false;
            Events.warn("Either or both the JsonNodes are NULL.");
        }

        return areJSONsEqual;
    }

    /**
     * This function compares 2 JSONs. 
     *
     * @param jSON_1
     * @param jSON_2
     * @return
     */
    public static boolean compareJsonNodes(JsonNode jSON_1, JsonNode jSON_2) {
        boolean areJSONsEqual = false;
        if (JSONObject.NULL.equals(jSON_1) || JSONObject.NULL.equals(jSON_2)) {
            Events.warn("Provided JSONs cannot be compared as one or both the JSONs are null.");
            return areJSONsEqual;
        }
        Events.info("Json_1 - KeyValueMap");
        HashMap<String, String> node1KVMap = getJsonKeyValueMap(jSON_1);
        Events.info("Json_2 - KeyValueMap");
        HashMap<String, String> node2KVMap = getJsonKeyValueMap(jSON_2);
        areJSONsEqual = node1KVMap.equals(node2KVMap);
        Events.info("areJSONsEqual: " + areJSONsEqual);
        if (!areJSONsEqual) {
            MapDifference<String, String> diff = Maps.difference(node1KVMap, node2KVMap);
            Map<String, String> nodeLeftMap = diff.entriesOnlyOnLeft();
            Events.info("Attributes of JSON_1 missing in JSON_2 [attrib,...]: \n" + nodeLeftMap.keySet().toString());
            Events.info("Values of JSON_1 missing in JSON_2 [json1Value,...]: \n" + nodeLeftMap.values().toString());

            Map<String, String> nodeRightMap = diff.entriesOnlyOnRight();
            Events.info("Attributes of JSON_2 missing in JSON_1 [attrib,...]: \n" + nodeRightMap.keySet().toString());
            Events.info("Values of JSON_2 missing in JSON_1 [json1Value,...]: \n" + nodeRightMap.values().toString());

            Map<String, MapDifference.ValueDifference<String>> nodeDifferenceMap = diff.entriesDiffering();
            Events.info("Attributes with different values in the 2 JSONs [attrib,...]: \n" + nodeDifferenceMap.keySet().toString());
            Events.info("Set of differing values [attrib(json1Value, json2Value),...]: \n" + nodeDifferenceMap.values().toString());
        }
        return areJSONsEqual;
    }

    /**
     *
     * @param json
     * @return
     */
    private static HashMap<String, String> getJsonKeyValueMap(JsonNode json) {
        HashMap<String, String> nodeAllKeyValueMap = new HashMap<>();
        if (!JSONObject.NULL.equals(json)) {
            JSONObject jObject = new JSONObject(json.toString());
            Iterator<?> keys = jObject.keys();

            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = jObject.get(key).toString();
                nodeAllKeyValueMap.put(key, value);
                Events.info("getJsonKeyValueMap == " + key + ", " + value);
            }
        } else {
            nodeAllKeyValueMap = null;
        }
        return nodeAllKeyValueMap;
    }

    //	public static boolean compareJsonBodyFormats(String referenceJsonString, String checkJsonString, String nodePrimaryKey)
    //			throws JsonProcessingException, IOException {
    //		boolean areJSONsEqual = true;
    //		JsonNode jSON_1 = getJsonNodeFromString(referenceJsonString);
    //		JsonNode jSON_2 = getJsonNodeFromString(checkJsonString);
    //		if (!jSON_1.isNull() && !jSON_2.isNull()) {
    //			if (jSON_1.isArray()) {
    //				Events.info("JSON_1 is an ARRAY. Comparing 2 JSONs' formats based on PrimaryKey: " + nodePrimaryKey);
    //				for (int i = 0; i < jSON_1.size(); i++) {
    //					String node1PrimaryKeyValue = jSON_1.get(i).get(nodePrimaryKey).asText();
    //					JsonNode node2Compare = getJsonNodeFromJsonArray(jSON_2, nodePrimaryKey, node1PrimaryKeyValue);
    //					if (JSONObject.NULL.equals(node2Compare)) {
    //						areJSONsEqual = false;
    //						Events.info("JSON_1 node with \"" + nodePrimaryKey + "\"=\"" + node1PrimaryKeyValue + "\" not found in JSON_2.");
    //						continue;
    //					}
    //					areJSONsEqual = Boolean.logicalAnd(areJSONsEqual, compareJsonNodeFormats(jSON_1.get(i), node2Compare));
    //				}
    //			} else {
    //				Events.info("JSON_1 is not an ARRAY. Directly comparing with JSON_2 body");
    //				areJSONsEqual = compareJsonNodeFormats(jSON_1, jSON_2);
    //			}
    //		} else {
    //			areJSONsEqual = false;
    //			Events.warn("Either or both the JsonNodes are NULL.");
    //		}
    //
    //		return areJSONsEqual;
    //	}
    //	/**
    //	 * This function compares
    //	 * 
    //	 * @param node1
    //	 * @param node2
    //	 * @return
    //	 */
    //	public static boolean compareJsonNodeFormats(JsonNode node1, JsonNode node2) {
    //		Events.info("Json_1 - KeyFormatMap");
    //		HashMap<String, String> node1KVMap = getJsonKeyFormatMap(node1);
    //		Events.info("Json_2 - KeyFormatMap");
    //		HashMap<String, String> node2KVMap = getJsonKeyFormatMap(node2);
    //		boolean areJSONFormatsSame = node1KVMap.equals(node2KVMap);
    //		Events.info("areJSONFormatsSame: " + areJSONFormatsSame);
    //		if (!areJSONFormatsSame) {
    //			MapDifference<String, String> diff = Maps.difference(node1KVMap, node2KVMap);
    //			Map<String, String> nodeLeftMap = diff.entriesOnlyOnLeft();
    //			Events.info("Attributes of JSON_1 missing in JSON_2 [attrib,...]: \n" + nodeLeftMap.keySet().toString());
    //			Events.info("Values of JSON_1 missing in JSON_2 [json1Value,...]: \n" + nodeLeftMap.values().toString());
    //
    //			Map<String, String> nodeRightMap = diff.entriesOnlyOnRight();
    //			Events.info("Attributes of JSON_2 missing in JSON_1 [attrib,...]: \n" + nodeRightMap.keySet().toString());
    //			Events.info("Values of JSON_2 missing in JSON_1 [json1Value,...]: \n" + nodeRightMap.values().toString());
    //
    //			Map<String, ValueDifference<String>> nodeDifferenceMap = diff.entriesDiffering();
    //			Events.info("Attributes with different values in the 2 JSONs [attrib,...]: \n" + nodeDifferenceMap.keySet().toString());
    //			Events.info("Set of differing values [attrib(json1Value, json2Value),...]: \n" + nodeDifferenceMap.values().toString());
    //		}
    //		return areJSONFormatsSame;
    //	}
    //	private static HashMap<String, String> getJsonKeyFormatMap(JsonNode json) {
    //		HashMap<String, String> nodeAllKeyFormatMap = new HashMap<>();
    //		JSONObject jObject = new JSONObject(json);
    //		Iterator<?> keys = jObject.keys();
    //
    //		JSONObject jObject2 = new JSONObject(json.toString());
    //		Iterator<?> keys2 = jObject2.keys();
    //
    //		JsonObject jObject3 = new JsonObject();
    //		jObject3.keySet();
    //		Iterator<?> keys3 = jObject.keys();
    //
    //		while (keys.hasNext()) {
    //			String attributeKey = (String) keys.next();
    //			String formatValue = jObject.get(attributeKey).toString();
    //			nodeAllKeyFormatMap.put(attributeKey, formatValue);
    //			Events.info("getJsonKeyFormatMap == " + attributeKey + ", " + formatValue);
    //		}
    //
    //		return nodeAllKeyFormatMap;
    //	}
}