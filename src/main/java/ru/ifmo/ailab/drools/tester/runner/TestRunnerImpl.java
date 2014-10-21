package ru.ifmo.ailab.drools.tester.runner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.drools.runtime.StatefulKnowledgeSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.ifmo.ailab.drools.tester.services.KnowledgeBaseHelper;
import ru.ifmo.ailab.drools.tester.services.TestHelper;
import ru.ifmo.ailab.drools.tester.services.TestScenario;

public class TestRunnerImpl implements TestRunner {

    private final String packageName;
    private final byte[] archive;
    private final JSONArray models;
    private final List<TestScenario> scenarios;

    TestRunnerImpl(final String packageName, final byte[] archive,
            final JSONArray models, final Map<String, JSONObject> scenarious) {
        this.packageName = packageName;
        this.archive = archive;
        this.models = models;
        this.scenarios = new ArrayList<TestScenario>();
        for (String name : scenarious.keySet()) {
            this.scenarios.add(new TestScenario(name, scenarious.get(name)));
        }
    }

    @Override
    public void run()
            throws JSONException, InstantiationException, IllegalAccessException,
            AssertionError {
        StatefulKnowledgeSession session = null;
        try {
            session = KnowledgeBaseHelper
                    .createStatefulSession(archive);
            TestHelper.validateModels(models, packageName, session.getKnowledgeBase());
        } finally {
            if (session != null) {
                session.dispose();
            }
        }
        for (TestScenario scenario : scenarios) {
            try {
                session = KnowledgeBaseHelper
                        .createStatefulSession(archive);
                scenario.execute(packageName, session);
            } finally {
                if (session != null) {
                    session.dispose();
                }
            }
        }
    }

}
