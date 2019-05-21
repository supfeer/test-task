package api.v2.oauth2

import api.ApiTester

import static io.restassured.RestAssured.given
import static org.hamcrest.CoreMatchers.notNullValue

class TokenSpec extends ApiTester {

    def "Make shure that guest can get token"() {
        expect:
        given()
                .param("grant_type", "client_credentials")
                .param("scope", "guest:default")

                .when()
                .post("/v2/oauth2/token")

                .then()
                .statusCode(200)
                .body("access_token", notNullValue())
    }

    def "Make sure that existing user can get his token"() {
        expect:
        def user = createUser()
        getUserToken(user.username)
    }
}
