package ru.ifmo.ailab.drools.tester.services;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.drools.KnowledgeBase;
import org.drools.command.CommandFactory;
import org.drools.definition.type.FactType;
import org.drools.impl.StatefulKnowledgeSessionImpl.ObjectStoreWrapper;
import org.drools.runtime.StatefulKnowledgeSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestScenario {

    private static final String INSERTIONS = "insertions";
    private static final String EXPECTATIONS = "expectations";
    private static final String CLASSNAME = "className";
    private static final String VARIABLE = "variable";
    private static final String VALUES = "values";
    private final String name;
    private final JSONObject json;

    public TestScenario(final String name, final JSONObject json) {
        this.name = name;
        this.json = json;
    }

    public void execute(final String packageName,
            final StatefulKnowledgeSession session)
            throws JSONException, InstantiationException, IllegalAccessException {
        System.out.println(name);
        final KnowledgeBase base = session.getKnowledgeBase();
        final Map<String, Variable> variables = new HashMap<String, Variable>();

        //Insert facts
        JSONArray insertions = json.getJSONArray(INSERTIONS);
        for (int i = 0; i < insertions.length(); i++) {
            final JSONObject obj = insertions.getJSONObject(i);
            final FactType factType = base.getFactType(packageName,
                    obj.getString(CLASSNAME));
            Object instance = factType.newInstance();
            if (obj.has(VALUES)) {
                final JSONObject values = obj.getJSONObject(VALUES);
                for (Iterator iter = values.keys(); iter.hasNext();) {
                    final String valueName = (String) iter.next();
                    final Object value = values.get(valueName);
                    factType.set(instance, valueName,
                            cast(factType.getField(valueName).getType(), value));
                }
            }
            if (obj.has(VARIABLE)) {
                final String varName = obj.getString(VARIABLE);
                variables.put(varName, new Variable(factType, instance));
            }
            session.insert(instance);
        }

        session.fireAllRules();

        //Check expected facts
        if (json.has(EXPECTATIONS)) {
            final JSONArray expectations = json.getJSONArray(EXPECTATIONS);
            assertExpectedFacts(expectations, packageName, session, variables);
        } else {
            throw new JSONException("[" + name + "] No expected facts declared!");
        }
    }

    private void assertExpectedFacts(final JSONArray expectations,
            final String packageName,
            final StatefulKnowledgeSession session,
            final Map<String, Variable> variables)
            throws AssertionError, JSONException {
        for (int i = 0; i < expectations.length(); i++) {
            JSONObject obj = expectations.getJSONObject(i);
            if (obj.has(VARIABLE)
                    && variables.containsKey(obj.getString(VARIABLE))) {
                final String varName = obj.getString(VARIABLE);
                Variable var = variables.get(varName);
                assertValues(var.type, var.instance, obj.getJSONObject(VALUES));
            } else if (obj.has(CLASSNAME)) {
                final String className = obj.getString(CLASSNAME);
                boolean found = false;
                ObjectStoreWrapper store = (ObjectStoreWrapper) session
                        .execute(CommandFactory.newGetObjects());
                for (Object o : store) {
                    System.out.println(o);
                    if (o.getClass().getSimpleName().equals(className)) {
                        FactType type = session.getKnowledgeBase().getFactType(
                                packageName, o.getClass().getSimpleName());
                        found = assertValues(type, o, obj.optJSONObject(VALUES));
                        if(found) {
                            break; //The expected fact has been found
                        }
                    }
                }
                if (!found) {
                    throw new AssertionError("Тест [" + name + "] не пройден!");
                }
            } else {
                throw new JSONException(
                        "Variable name of class name are not declared for " + i + " expected fact!");
            }
        }
    }

    private boolean assertValues(final FactType type, final Object instance,
            final JSONObject values) throws JSONException {
        if (values != null) {
            for (Iterator iter = values.keys(); iter.hasNext();) {
                final String valueName = (String) iter.next();
                final Object value = values.get(valueName);
                System.out.println(valueName + "=" + value);
                final Object result = type.get(instance, valueName);
                System.out.println("result=" + result);
                return result != null && value.equals(result);
            }
        } else {
            return true;
        }
        return false;
    }

    private Object cast(final Class<?> type, final Object value) {
        if (type == BigDecimal.class) {
            if (value instanceof Integer) {
                return new BigDecimal((Integer) value);
            } else if (value instanceof Double) {
                return new BigDecimal((Double) value, MathContext.DECIMAL32);
            } else {
                return value;
            }
        } else {
            return value;
        }
    }

    private static class Variable {

        public final FactType type;
        public final Object instance;

        public Variable(final FactType type, final Object instance) {
            this.type = type;
            this.instance = instance;
        }

    }
}
