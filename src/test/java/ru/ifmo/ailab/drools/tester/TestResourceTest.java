package ru.ifmo.ailab.drools.tester;

import java.io.File;
import java.net.URISyntaxException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.*;
import org.junit.Test;

public class TestResourceTest extends JerseyTest {

    public TestResourceTest() {
        super(new ApplicationConfig());
    }

    @Override
    protected void configureClient(ClientConfig config) {
        super.configureClient(config);
        config.register(MultiPartFeature.class);
    }

    @Test
    public void testUpload() throws URISyntaxException {
        final FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.field("file",
                new File(this.getClass().getResource("default.pkg").toURI()),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = client().target("file")
                .request(MediaType.MULTIPART_FORM_DATA)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
        assertNotNull(response);
    }

    @Test
    public void testMinimalPackage() throws URISyntaxException {
        final FormDataMultiPart multiPart = new FormDataMultiPart();
        multiPart.field("file",
                new File(this.getClass().getResource("minimal.pkg").toURI()),
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
        Response response = client().target("file")
                .request(MediaType.MULTIPART_FORM_DATA)
                .post(Entity.entity(multiPart, MediaType.MULTIPART_FORM_DATA));
        assertNotNull(response);
    }
}
