package ru.ifmo.ailab.drools.tester;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.drools.runtime.StatefulKnowledgeSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Ignore;
import org.junit.Test;
import ru.ifmo.ailab.drools.tester.services.KnowledgeBaseHelper;
import ru.ifmo.ailab.drools.tester.services.TestHelper;

@Ignore
public class TestHelperTest {
    
    @Test
    public void testValidateModels() throws IOException, JSONException {
        final String json = IOUtils.toString(this.getClass().getResourceAsStream("models.json"));
        final byte[] pkg = IOUtils.toByteArray(this.getClass().getResourceAsStream("credit.pkg"));
        final StatefulKnowledgeSession session = KnowledgeBaseHelper.createStatefulSession(pkg);
        TestHelper.validateModels(new JSONArray(json), "credit", session.getKnowledgeBase());
        session.dispose();
    }
}