package api

import com.github.javafaker.Faker
import groovy.json.JsonSlurper
import io.restassured.RestAssured
import io.restassured.authentication.PreemptiveBasicAuthScheme
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.notNullValue

abstract class ApiTester extends Specification {

    def faker = new Faker()

    def password = Base64.getEncoder().encodeToString(
            faker.internet().password().getBytes()
    )

    def setupSpec() {
        def baseHost = System.getProperty("server.host")
        if (baseHost == null) {
            baseHost = "http://test-api.d6.dev.devcaz.com"
        }
        RestAssured.baseURI = baseHost
    }

    def setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme()
        authScheme.setUserName("front_2d6b0a8391742f5d789d7d915755e09e")
        RestAssured.authentication = authScheme
    }

    def getGuestToken() {
        return given()
                .param("grant_type", "client_credentials")
                .param("scope", "guest:default")

                .when()
                .post("/v2/oauth2/token")

                .then()
                .statusCode(200)
                .body("access_token", notNullValue())
                .extract()
                .body()
                .jsonPath()
                .getString("access_token")
    }

    def getUserToken(username, password = this.password) {
        return given()
                .param("grant_type", "password")
                .param("username", username)
                .param("password", password)

                .when()
                .post("/v2/oauth2/token")

                .then()
                .statusCode(200)
                .body("access_token", notNullValue())
                .extract()
                .body()
                .jsonPath()
                .getString("access_token")
    }

    def createUser() {
        def slurper = new JsonSlurper()
        return slurper.parseText(
                given().auth().oauth2(getGuestToken())
                        .param("username", faker.name().username())
                        .param("password_change", password)
                        .param("password_repeat", password)
                        .param("email", faker.internet().emailAddress())

                        .when()
                        .post("/v2/players")

                        .then()
                        .statusCode(201)
                        .extract()
                        .body()
                        .asString()
        )
    }
}


