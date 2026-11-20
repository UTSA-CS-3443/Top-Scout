package edu.utsa.cs3443.topscout.model.fields;

import com.google.gson.JsonObject;

/**
 * Holds a field whose kind has no class yet (table, summary, resource) exactly
 * as it appeared in the JSON, so loading and saving a project never loses it.
 * It never counts as filled. Replace it with real classes when the Before and
 * After stages are built.
 */
public class RawField extends Field {

    private final JsonObject json;

    public RawField(String id, String label, String hint, boolean required, JsonObject json) {
        super(id, label, hint, required);
        this.json = json;
    }

    public JsonObject getJson() {
        return json;
    }

    @Override
    public boolean isFilled() {
        return false;
    }
}
