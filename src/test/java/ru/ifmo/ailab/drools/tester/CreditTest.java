package ru.ifmo.ailab.drools.tester;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.drools.definition.type.FactType;
import org.drools.runtime.StatefulKnowledgeSession;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import ru.ifmo.ailab.drools.tester.services.KnowledgeBaseHelper;

@Ignore
public class CreditTest {

    @Test
    public void testEmptyWM() throws IOException, InstantiationException, IllegalAccessException {
        final byte[] pkg = IOUtils.toByteArray(this.getClass().getResourceAsStream("credit.pkg"));
        StatefulKnowledgeSession session = KnowledgeBaseHelper.createStatefulSession(pkg);
        FactType applicationForCredit = session.getKnowledgeBase().getFactType("credit", "ApplicationForCredit");
        FactType creditDecision = session.getKnowledgeBase().getFactType("credit", "CreditDecision");
        Object application = applicationForCredit.newInstance();
        applicationForCredit.set(application, "Age", 17);
        Object decision = creditDecision.newInstance();
        session.insert(application);
        session.insert(decision);
        session.fireAllRules();
        assertEquals(false, creditDecision.get(decision, "Decision"));
    }
}
