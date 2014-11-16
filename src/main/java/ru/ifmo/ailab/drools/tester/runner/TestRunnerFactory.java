package ru.ifmo.ailab.drools.tester.runner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestRunnerFactory {

    private static final String PROPERTY_NAME = "drools.tester.tests";
    private static final String CATALINA_HOME = "catalina.home";
    private static final String MODELS_FILE = "models.json";
    private static final String FILE_SEPARATOR = "file.separator";

    private File getTestsRootLocation() {
        String location = "tests";
        if (System.getProperty(PROPERTY_NAME) == null) {
            if (System.getProperty("catalina.home") != null) {
                location = System.getProperty(CATALINA_HOME)
                        + System.getProperty(FILE_SEPARATOR) + "tests";
            }
        } else {
            location = System.getProperty(PROPERTY_NAME);
        }
        return new File(location);
    }
    
    public File getTestsAssigmentLocation(final String assigment) {
        return new File(getTestsRootLocation(), assigment);
    }
    
    public int getNumberOfTests() {
        return getTestsRootLocation().list(new AssigmentFilter()).length;
    }
    
    private static class AssigmentFilter implements FilenameFilter {
        
        @Override
        public boolean accept(File file, String string) {
            return string.matches("assignment-\\d+");
        }
    }

    private JSONArray readModels(final File root)
            throws JSONException, IOException {
        final List<String> files = Arrays.asList(root.list());
        JSONArray models;
        if (!files.contains(MODELS_FILE)) {
            throw new FileNotFoundException(
                    "The models.json file does not exists at " + root.getAbsolutePath());
        } else {
            FileReader reader = null;
            try {
                reader = new FileReader(new File(root, MODELS_FILE));
                models = new JSONArray(IOUtils.toString(reader));
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return models;
    }

    private Map<String, JSONObject> readScenarios(final File root)
            throws JSONException, IOException {
        final List<String> files = Arrays.asList(root.list());
        final Map<String, JSONObject> scenarios = new HashMap<String, JSONObject>();
        FileReader reader = null;
        for (String file : files) {
            if (file.equalsIgnoreCase(MODELS_FILE)) {
                continue;
            }
            try {
                reader = new FileReader(new File(root, file));
                scenarios.put(file.replace(".json", ""), 
                        new JSONObject(IOUtils.toString(reader)));
            } finally {
                IOUtils.closeQuietly(reader);
            }
        }
        return scenarios;
    }

    public boolean isTestsExist(final String assignment) {
        if(assignment == null) {
            return false;
        }
        return getTestsAssigmentLocation(assignment).exists();
    }

    public TestRunner createTestRunner(final String email, final String assigment,
            final String packageName, final byte[] archive)
            throws JSONException, IOException {
        final File root = getTestsAssigmentLocation(assigment);
        return new TestRunnerImpl(email, assigment, packageName, archive, readModels(root),
                readScenarios(root));
    }

}
