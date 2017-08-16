package name.isergius.finance.personal.ui;

import name.isergius.finance.personal.ui.dto.RecordIdResource;
import name.isergius.finance.personal.ui.dto.RecordResource;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertNotNull;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.springSecurity;

/**
 * Sergey Kondratyev
 */
@WithMockUser
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringJUnit4ClassRunner.class)
public class RecordHandlerTest {

    private static final String URL_RECORD_GENERATE_ID = "/record/generateId";
    private static final String URL_RECORD_ID = "/record/{id}";
    private static final String URL_ALL_RECORD = "/record/";

    private static final String PROPERTY_RECORD_AMOUNT = "amount";
    private static final String PROPERTY_RECORD_WRONG_AMOUNT = "amoun";
    private static final String PROPERTY_RECORD_CURRENCY = "currency";
    private static final String PROPERTY_RECORD_DATE = "date";
    private static final String VALUE_RECORD_AMOUNT = "10";
    private static final String VALUE_RECORD_CURRENCY = "USD";
    private static final int VALUE_RECORD_DATE = 1000;

    @Autowired
    private ApplicationContext context;

    private WebTestClient webClient;

    @Before
    public void setUp() throws Exception {
        webClient = WebTestClient
                .bindToApplicationContext(this.context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testGenerateId_success() throws Exception {
        FluxExchangeResult<RecordIdResource> result = successRequest();
        RecordIdResource actual = result.getResponseBody()
                .blockFirst();
        assertNotNull(actual.getId());
        assertNotNull(actual.getTtl());
    }

    private FluxExchangeResult<RecordIdResource> successRequest() {
        return webClient.get()
                .uri(URL_RECORD_GENERATE_ID)
                .exchange()
                .returnResult(RecordIdResource.class);
    }

    @Test
    public void testSave_success() throws Exception {
        JSONObject body = new JSONObject()
                .put(PROPERTY_RECORD_AMOUNT, VALUE_RECORD_AMOUNT)
                .put(PROPERTY_RECORD_CURRENCY, VALUE_RECORD_CURRENCY)
                .put(PROPERTY_RECORD_DATE, Instant.now().toEpochMilli());
        UUID id = successRequest().getResponseBody()
                .blockFirst()
                .getId();
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
                .put(PROPERTY_RECORD_WRONG_AMOUNT, VALUE_RECORD_AMOUNT)
                .put(PROPERTY_RECORD_CURRENCY, VALUE_RECORD_CURRENCY)
                .put(PROPERTY_RECORD_DATE, LocalDate.now().toEpochDay());
        UUID id = successRequest().getResponseBody()
                .blockFirst()
                .getId();
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
                .put(PROPERTY_RECORD_AMOUNT, VALUE_RECORD_AMOUNT)
                .put(PROPERTY_RECORD_CURRENCY, VALUE_RECORD_CURRENCY)
                .put(PROPERTY_RECORD_DATE, Instant.now().toEpochMilli());
        UUID id = successRequest().getResponseBody()
                .blockFirst()
                .getId();
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webClient.put()
                .uri(URL_RECORD_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body.toString())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testSave_withoutReservedId() throws Exception {
        JSONObject body = new JSONObject()
                .put(PROPERTY_RECORD_AMOUNT, VALUE_RECORD_AMOUNT)
                .put(PROPERTY_RECORD_CURRENCY, VALUE_RECORD_AMOUNT)
                .put(PROPERTY_RECORD_DATE, LocalDate.now().toEpochDay());
        UUID id = UUID.randomUUID();
        webClient.put()
                .uri(URL_RECORD_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body.toString())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    public void testGet_success() throws Exception {
        UUID id = createRecord();
        webClient.get()
                .uri(URL_RECORD_ID, id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RecordResource.class)
                .isEqualTo(new RecordResource(id, VALUE_RECORD_AMOUNT, VALUE_RECORD_CURRENCY, VALUE_RECORD_DATE));
    }

    @Test
    public void testGet_notContainId() throws Exception {
        UUID id = UUID.randomUUID();
        webClient.get()
                .uri(URL_RECORD_ID, id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    private UUID createRecord() throws JSONException {
        JSONObject body = new JSONObject()
                .put(PROPERTY_RECORD_AMOUNT, VALUE_RECORD_AMOUNT)
                .put(PROPERTY_RECORD_CURRENCY, VALUE_RECORD_CURRENCY)
                .put(PROPERTY_RECORD_DATE, VALUE_RECORD_DATE);
        UUID id = successRequest().getResponseBody()
                .blockFirst()
                .getId();
        webClient.put()
                .uri(URL_RECORD_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody(body.toString())
                .exchange()
                .expectStatus().isCreated();
        return id;
    }

    @Test
    public void testGetAll_success() throws Exception {
        RecordResource[] resources = Stream.of(createRecord(), createRecord())
                .map(id -> new RecordResource(id, VALUE_RECORD_AMOUNT, VALUE_RECORD_CURRENCY, VALUE_RECORD_DATE))
                .collect(toList())
                .toArray(new RecordResource[2]);
        webClient.get()
                .uri(URL_ALL_RECORD)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(RecordResource[].class)
                .isEqualTo(resources);

    }
}