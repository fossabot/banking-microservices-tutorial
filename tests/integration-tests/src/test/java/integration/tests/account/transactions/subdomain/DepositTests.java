package integration.tests.account.transactions.subdomain;

import integration.tests.utils.RestHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static integration.tests.utils.AssertHelper.assertValueInJsonField;
import static integration.tests.utils.MockHttpConstants.VALID_PERSON_ID;
import static io.restassured.RestAssured.given;

// Requires docker-compose-sub-domain-testing.yml be up.
public class DepositTests {
    private static final RestHelper restHelper = new RestHelper();

    @BeforeAll
    public static void beforeAll() {
        restHelper.onlyUseGateway();
    }

    @AfterAll
    public static void afterAll() {
        restHelper.clearAccounts();
    }

    @Test
    public void givenCorrectInput_whenDepositing_thenAccountIsUpdated() {
        // Arrange
        String accountId1 = restHelper.createAccount(VALID_PERSON_ID);
        String transaction = "{\n"
            + "\t\"accountId\": \"%s\",\n"
            + "\t\"customerId\": \"%s\",\n"
            + "\t\"amount\": 10.0\n"
            + "}";

        // Act
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format(transaction, accountId1, VALID_PERSON_ID))
            .post("/api/v1/transactions/deposit");

        // Assert
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .get("/api/v1/accounts/" + accountId1);

        assertValueInJsonField(response, "balance", 10.0);
    }

    @Test
    public void givenLargeSequentialDeposits_whenDepositing_thenAccountIsUpdatedCorrectly() {
        // Arrange
        String accountId1 = restHelper.createAccount(VALID_PERSON_ID);
        String transaction = "{\n"
            + "\t\"accountId\": \"%s\",\n"
            + "\t\"customerId\": \"%s\",\n"
            + "\t\"amount\": 999999999.24\n"
            + "}";
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format(transaction, accountId1, VALID_PERSON_ID))
            .post("/api/v1/transactions/deposit");

        // Act
        given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format(transaction, accountId1, VALID_PERSON_ID))
            .post("/api/v1/transactions/deposit");

        // Assert
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();
        Response response = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .get("/api/v1/accounts/" + accountId1);

        assertValueInJsonField(response, "balance", 1999999998.48);
    }

    @Test
    public void givenCorrectInput_whenDepositing_thenTransactionIsMarkedSuccessful() {
        // Arrange
        String accountId1 = restHelper.createAccount(VALID_PERSON_ID);
        String transaction = "{\n"
            + "\t\"accountId\": \"%s\",\n"
            + "\t\"customerId\": \"%s\",\n"
            + "\t\"amount\": 100.0\n"
            + "}";
        RestAssured.baseURI = restHelper.getAccountGatewayUrl();

        // Act
        Response transactionResponse = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .body(String.format(transaction, accountId1, VALID_PERSON_ID))
            .post("/api/v1/transactions/deposit");

        // Assert
        String transactionId = transactionResponse.getBody().asString();
        Response response = given().urlEncodingEnabled(true)
            .contentType(ContentType.JSON)
            .get("/api/v1/transactions/id/" + transactionId);

        response.then().statusCode(200);
        assertValueInJsonField(response, "status", "SUCCESSFUL");
    }
}
