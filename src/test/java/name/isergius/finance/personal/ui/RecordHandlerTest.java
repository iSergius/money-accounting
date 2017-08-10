package name.isergius.finance.personal.ui;

import name.isergius.finance.personal.ui.dto.RecordIdResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Sergey Kondratyev
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordHandlerTest {

    private static final String URL_RECORD_GENERATE_ID = "/record/generateId";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGenerateId_success() throws Exception {
        ResponseEntity<RecordIdResource> responseEntity = successRequest();
        RecordIdResource actual = responseEntity.getBody();
        assertNotNull(actual.getId());
        assertNotNull(actual.getTtl());
    }

    @Test
    public void testGenerateId_ttlMustBeTwoSeconds() throws Exception {
        ResponseEntity<RecordIdResource> responseEntity = successRequest();
        assertTrue(responseEntity.getBody().getTtl() == TimeUnit.SECONDS.toMillis(2));
    }

    private ResponseEntity<RecordIdResource> successRequest() {
        ResponseEntity<RecordIdResource> responseEntity = restTemplate.getForEntity(URL_RECORD_GENERATE_ID, RecordIdResource.class);
        if (!responseEntity.hasBody() && !responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            fail("Response body must be");
        }
        return responseEntity;
    }
}