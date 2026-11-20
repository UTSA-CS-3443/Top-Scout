package edu.utsa.cs3443.topscout.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import edu.utsa.cs3443.topscout.model.fields.Contact;
import edu.utsa.cs3443.topscout.model.fields.Date;
import edu.utsa.cs3443.topscout.model.fields.Field;
import edu.utsa.cs3443.topscout.model.fields.Image;
import edu.utsa.cs3443.topscout.model.fields.Number;
import edu.utsa.cs3443.topscout.model.fields.RadioButton;
import edu.utsa.cs3443.topscout.model.fields.RawField;
import edu.utsa.cs3443.topscout.model.fields.Text;
import edu.utsa.cs3443.topscout.model.fields.TextArea;

/**
 * Teaches Gson how to save and load the Field classes. Every field is written
 * with a "type" (text, textarea, date, number, radio, image, contact) and is
 * rebuilt as the matching subclass when loaded. Gson cannot do this alone
 * because a list of Field does not say which subclass each item is.
 */
public class FieldAdapter implements JsonSerializer<Field>, JsonDeserializer<Field> {

    private static final String[] CONTACT_KEYS =
            {"name", "phone", "email", "street", "city", "state", "zip"};

    @Override
    public JsonElement serialize(Field field, Type type, JsonSerializationContext context) {
        if (field instanceof RawField raw) {
            return raw.getJson();
        }

        JsonObject json = new JsonObject();
        json.addProperty("id", field.getId());
        json.addProperty("label", field.getLabel());
        if (field.getHint() != null) {
            json.addProperty("hint", field.getHint());
        }
        json.addProperty("required", field.isRequired());

        // Check the most specific classes first (Text, Date extend TextArea)
        if (field instanceof Text text) {
            json.addProperty("type", "text");
            json.addProperty("value", text.getValue());
        } else if (field instanceof Date date) {
            json.addProperty("type", "date");
            json.addProperty("value", date.getValue());
        } else if (field instanceof TextArea area) {
            json.addProperty("type", "textarea");
            json.addProperty("value", area.getValue());
        } else if (field instanceof Number number) {
            json.addProperty("type", "number");
            json.addProperty("value", number.getValue());
        } else if (field instanceof RadioButton radio) {
            json.addProperty("type", "radio");
            JsonArray options = new JsonArray();
            for (String option : radio.getOptions()) {
                options.add(option);
            }
            json.add("options", options);
            json.addProperty("value", radio.getValue());
        } else if (field instanceof Image image) {
            json.addProperty("type", "image");
            JsonArray paths = new JsonArray();
            for (String path : image.getPaths()) {
                paths.add(path);
            }
            json.add("images", paths);
        } else if (field instanceof Contact contact) {
            json.addProperty("type", "contact");
            JsonObject info = new JsonObject();
            info.addProperty("name", contact.getName());
            info.addProperty("phone", contact.getPhone());
            info.addProperty("email", contact.getEmail());
            info.addProperty("street", contact.getStreet());
            info.addProperty("city", contact.getCity());
            info.addProperty("state", contact.getState());
            info.addProperty("zip", contact.getZip());
            json.add("contact", info);
        } else {
            throw new JsonParseException("Unknown field class " + field.getClass().getName());
        }
        return json;
    }

    @Override
    public Field deserialize(JsonElement element, Type type, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject json = element.getAsJsonObject();
        String id = text(json, "id", "");
        String label = text(json, "label", "");
        String hint = text(json, "hint", "");
        boolean required = json.has("required") && json.get("required").getAsBoolean();
        String kind = text(json, "type", "text");

        switch (kind) {
            case "text":
            case "textarea":
            case "notepad":
            case "date":
                String value = text(json, "value", "");
                if (kind.equals("text")) {
                    return new Text(id, label, hint, required, value);
                } else if (kind.equals("date")) {
                    return new Date(id, label, hint, required, value);
                }
                return new TextArea(id, label, hint, required, value);
            case "number":
                return new Number(id, label, hint, required, number(json));
            case "radio":
                ArrayList<String> options = new ArrayList<>();
                if (json.has("options")) {
                    for (JsonElement option : json.getAsJsonArray("options")) {
                        options.add(option.getAsString());
                    }
                }
                return new RadioButton(id, label, hint, required, options, text(json, "value", ""));
            case "image":
                ArrayList<String> paths = new ArrayList<>();
                if (json.has("images")) {
                    for (JsonElement path : json.getAsJsonArray("images")) {
                        paths.add(path.getAsString());
                    }
                }
                return new Image(id, label, hint, required, paths);
            case "contact":
                HashMap<String, String> info = new HashMap<>();
                if (json.has("contact")) {
                    for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("contact").entrySet()) {
                        info.put(entry.getKey(), entry.getValue().getAsString());
                    }
                }
                return new Contact(id, label, hint, required, info);
            default:
                // table, summary, resource: keep exactly as written until those classes exist
                return new RawField(id, label, hint, required, json);
        }
    }

    /** Reads a string property, or the fallback if it is missing or null. */
    private static String text(JsonObject json, String key, String fallback) {
        JsonElement e = json.get(key);
        if (e == null || e.isJsonNull()) {
            return fallback;
        }
        return e.getAsString();
    }

    /** Reads the number value. An empty or missing value means 0. */
    private static double number(JsonObject json) {
        JsonElement e = json.get("value");
        if (e == null || e.isJsonNull() || !(e instanceof JsonPrimitive) || e.getAsString().isBlank()) {
            return 0.0;
        }
        return e.getAsDouble();
    }
}
