package ru.ifmo.ailab.drools.tester.runner;

import org.json.JSONException;

public interface TestRunner {

    public void run() throws JSONException, InstantiationException,
            IllegalAccessException, AssertionError;

}
