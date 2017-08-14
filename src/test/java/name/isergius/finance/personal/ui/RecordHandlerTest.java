package name.isergius.finance.personal.ui;

import name.isergius.finance.personal.ui.dto.RecordIdResource;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Sergey Kondratyev
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordHandlerTest {

    private static final String URL_RECORD_GENERATE_ID = "/record/generateId";
    private static final String URL_RECORD_ID = "/record/{id}";

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private WebTestClient webClient;

    @Test
    public void testGenerateId_success() throws Exception {
        ResponseEntity<RecordIdResource> responseEntity = successRequest();
        RecordIdResource actual = responseEntity.getBody();
        assertNotNull(actual.getId());
        assertNotNull(actual.getTtl());
    }

//    @Test
//    public void testGenerateId_ttlMustBeTwoSeconds() throws Exception {
//        ResponseEntity<RecordIdResource> responseEntity = successRequest();
//        assertTrue(responseEntity.getBody().getTtl() == TimeUnit.SECONDS.toMillis(2));
//    }

    private ResponseEntity<RecordIdResource> successRequest() {
        ResponseEntity<RecordIdResource> responseEntity = restTemplate.getForEntity(URL_RECORD_GENERATE_ID, RecordIdResource.class);
        if (!responseEntity.hasBody() && !responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            fail("Response body must be");
        }
        return responseEntity;
    }

    @Test
    public void testSave_success() throws Exception {
        JSONObject body = new JSONObject()
                .put("amount", "10")
                .put("currency", "USD")
                .put("date", Instant.now().toEpochMilli());
        UUID id = successRequest().getBody().getId();
        webClient.put()
                .uri(URL_RECORD_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body.toString())
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void testSave_wrongRequestBody() throws Exception {
        JSONObject body = new JSONObject()
                .put("amoun", "10")
                .put("currency", "USD")
                .put("date", LocalDate.now().toEpochDay());
        UUID id = UUID.randomUUID();
        webClient.put()
                .uri(URL_RECORD_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body.toString())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testSave_errorOnDelay() throws Exception {
        JSONObject body = new JSONObject()
                .put("amount", "10")
                .put("currency", "USD")
                .put("date", Instant.now().toEpochMilli());
        RecordIdResource idResource = successRequest().getBody();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webClient.put()
                .uri(URL_RECORD_ID, idResource.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body.toString())
                .exchange()
                .expectStatus().isBadRequest();
    }
}