package ru.ifmo.ailab.drools.tester.services;

import java.util.Iterator;
import org.drools.KnowledgeBase;
import org.drools.definition.type.FactField;
import org.drools.definition.type.FactType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import static org.junit.Assert.*;

public class TestHelper {

    private static final String CLASSNAME = "className";
    private static final String FIELDS = "fields";

    public static void validateModels(final JSONArray models,
            final String packageName, final KnowledgeBase kbase)
            throws JSONException, AssertionError {
        for (int i = 0; i < models.length(); i++) {
            JSONObject model = models.optJSONObject(i);
            String className = model.getString(CLASSNAME);
            FactType type = kbase.getFactType(packageName, className);
            assertNotNull("Модель [" + className + "] необъявлена в пакете [" 
                    + packageName + "]!", type);

            JSONObject fields = model.getJSONObject(FIELDS);
            for (Iterator iter = fields.keys(); iter.hasNext();) {
                String fieldName = (String) iter.next();
                String fieldType = fields.getString(fieldName);
                FactField field = type.getField(fieldName);
                assertNotNull("Модель [" + className + "]: поле [" + fieldName + "] необъявлено!", field);
                assertEquals("Модель [" + className + "]: тип поля [" + fieldName + "] неверен.",
                        fieldType, field.getType().getSimpleName());
            }
        }
    }
}
