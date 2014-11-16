package ru.ifmo.ailab.drools.tester;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Ignore;
import org.junit.Test;
import ru.ifmo.ailab.drools.tester.services.KnowledgeBaseHelper;
import ru.ifmo.ailab.drools.tester.services.TestScenario;

@Ignore
public class TestScenarioTest {

    @Test
    public void testExecute() throws IOException, JSONException,
            InstantiationException, IllegalAccessException {
        final String json = IOUtils.toString(this.getClass().getResourceAsStream("test1.json"));
        final byte[] pkg = IOUtils.toByteArray(this.getClass().getResourceAsStream("credit.pkg"));
        TestScenario scenario = new TestScenario("test1", new JSONObject(json));
        StatefulKnowledgeSession session = KnowledgeBaseHelper.createStatefulSession(pkg);
        scenario.execute("test@test.com", "assignment-1", "credit", session);
    }
}