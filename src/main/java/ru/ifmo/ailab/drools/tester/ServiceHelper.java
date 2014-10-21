package ru.ifmo.ailab.drools.tester;

import java.security.DigestInputStream;
import javax.ws.rs.core.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceHelper {

    /**
     * A naive implementation of mail address validation.
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(final String email) {
        return email != null && email.contains("@");
    }

    public static String createResponse(final Response.Status status) {
        return createResponse(status, -1);
    }

    public static String createResponse(final Response.Status status, int code) {
        return createResponse(status, code, null);
    }

    public static String createResponse(final Response.Status status,
            int code, final String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("status", status.getStatusCode());
            object.put("code", code);
            object.put("message", message);
        } catch (JSONException __) {
        }
        return object.toString();
    }
    
    public static String calculateChecksum(final byte[] bytes) {
        return DigestUtils.md5Hex(bytes);
    }
    
}
