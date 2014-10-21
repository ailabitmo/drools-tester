package ru.ifmo.ailab.drools.tester;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ifmo.ailab.drools.tester.models.AssignmentResult;
import ru.ifmo.ailab.drools.tester.runner.TestRunner;
import ru.ifmo.ailab.drools.tester.runner.TestRunnerFactory;

@Path("/")
public class TestResource {

    private static final Logger logger = LoggerFactory.getLogger(
            TestResource.class);
    private static final String DROOLS_PACKAGE_POSTFIX = ".pkg";
    private static EntityManagerFactory emf;
    private static TestRunnerFactory runnerFactory;
    private final EntityManager em;

    public TestResource() throws FileNotFoundException, IOException,
            JSONException {
        synchronized (TestResource.class) {
            if (emf == null) {
                TestResource.emf = Persistence
                        .createEntityManagerFactory("ru.ifmo.ailab.drools.tester");
            }
            if (runnerFactory == null) {
                TestResource.runnerFactory = new TestRunnerFactory();
            }
        }
        this.em = emf.createEntityManager();
    }

    @Path("results")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AssignmentResult> getResults() {
        return allAssignmentResults();
    }

    @Path("file")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response upload(@FormDataParam("assignment-number") String assignment,
            @FormDataParam("email") String email,
            @FormDataParam("file") FormDataBodyPart body) {
        try {
            if (!runnerFactory.isTestsExist(assignment)) {
                logger.error("There are no tests for {}! Tried to find at {}",
                        assignment, runnerFactory.getTestsAssigmentLocation(assignment));
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
            if (!ServiceHelper.isValidEmail(email)) {
                return Response.ok(ServiceHelper.createResponse(Response.Status.BAD_REQUEST, 3))
                        .build();
            }
            final InputStream stream = body.getValueAs(InputStream.class);
            final String fileName = body.getContentDisposition().getFileName();
            final String packageName;
            if (fileName.endsWith(DROOLS_PACKAGE_POSTFIX)) {
                packageName = fileName.replace(DROOLS_PACKAGE_POSTFIX, "");
            } else {
                return Response.ok(ServiceHelper.createResponse(
                        Response.Status.BAD_REQUEST, 1)).build();
            }
            final byte[] bytes = IOUtils.toByteArray(stream);
            AssignmentResult result = findOrCreateAssignmentResult(email,
                    assignment);
            if(isSameSolutionExists(email, bytes)) {
                result.setCheater(true);
                result.setResultMessage("Попробовал(а) смухлевать.");
                saveAssignmentResult(result);
                return Response.ok(ServiceHelper.createResponse(
                        Response.Status.BAD_REQUEST, 4)).build();
            }
            try {
                TestRunner runner = runnerFactory.createTestRunner(assignment,
                        packageName, bytes);
                runner.run();
                result.setResultMessage("Решение верно.");
                result.setChecksum(ServiceHelper.calculateChecksum(bytes));
                saveAssignmentResult(result);
            } catch (AssertionError error) {
                result.setResultMessage(error.getLocalizedMessage());
                saveAssignmentResult(result);
                return Response.ok(ServiceHelper.createResponse(
                        Response.Status.BAD_REQUEST, 2, error.getLocalizedMessage()))
                        .build();
            }
            return Response.ok(ServiceHelper.createResponse(Response.Status.OK))
                    .build();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            return Response.ok(ServiceHelper.createResponse(
                    Response.Status.INTERNAL_SERVER_ERROR,
                    5, ex.getMessage())).build();
        }
    }

    private AssignmentResult findOrCreateAssignmentResult(final String email,
            final String assignment) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AssignmentResult> query = builder.createQuery(
                AssignmentResult.class);
        Root root = query.from(AssignmentResult.class);
        query.where(builder.and(
                builder.equal(root.get("email"), email),
                builder.equal(root.get("assignment"), assignment)));
        List<AssignmentResult> results = em.createQuery(query).getResultList();
        AssignmentResult result;
        if (results == null || results.isEmpty()) {
            result = new AssignmentResult();
            result.setEmail(email);
            result.setAssignment(assignment);
        } else {
            result = results.get(0);
        }
        result.setSubmitDate(new Date());
        return result;
    }

    private boolean isSameSolutionExists(final String email,
            final byte[] bytes) {
        final String checksum = ServiceHelper.calculateChecksum(bytes);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<AssignmentResult> query = builder.createQuery(
                AssignmentResult.class);
        Root root = query.from(AssignmentResult.class);
        query.where(builder.and(
                builder.notEqual(root.get("email"), email),
                builder.equal(root.get("checksum"), checksum)));
        List<AssignmentResult> results = em.createQuery(query).getResultList();
        return !results.isEmpty();
    }

    private List<AssignmentResult> allAssignmentResults() {
        List<AssignmentResult> results = null;
        try {
            em.getTransaction().begin();
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<AssignmentResult> query = builder.createQuery(
                    AssignmentResult.class);
            Root root = query.from(AssignmentResult.class);
            CriteriaQuery<AssignmentResult> all = query.select(root);
            results = em.createQuery(all).getResultList();
            em.getTransaction().commit();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            em.getTransaction().rollback();
        }
        if (results == null) {
            results = new ArrayList<AssignmentResult>();
        }
        return results;
    }

    private void saveAssignmentResult(final AssignmentResult result) {
        try {
            em.getTransaction().begin();
            em.persist(result);
            em.getTransaction().commit();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            em.getTransaction().rollback();
        }
    }

}
