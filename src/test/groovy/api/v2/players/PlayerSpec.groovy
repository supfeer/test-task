package api.v2.players

import api.ApiTester

import static io.restassured.RestAssured.given
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath

class PlayerSpec extends ApiTester {

    def "Make sure that ordinary user can register"() {
        given:
        def token = getGuestToken()
        expect:
        given().auth().oauth2(token)
                .param("username", faker.name().username())
                .param("password_change", password)
                .param("password_repeat", password)
                .param("email", faker.internet().emailAddress())
                .param("name", faker.name().name())
                .param("surname", faker.name().lastName())
                .param("currency_code", faker.currency().code())

                .when()
                .post("/v2/players")

                .then()
                .statusCode(201)
                .assertThat().body(matchesJsonSchemaInClasspath("player-schema.json"))
    }

    def "Make sure that existing user can get his credentials"() {
        given:
        def user = createUser()
        def userToken = getUserToken(user.username)

        expect:
        given().auth().oauth2(userToken)

                .when()
                .get("/v2/players/" + user.id)

                .then()
                .statusCode(200)
                .assertThat().body(matchesJsonSchemaInClasspath("player-schema.json"))
    }

    def "Make sure that user gets 404 when trying to get another id"() {
        given:
        def currentUser = createUser()
        def currentUserToken = getUserToken(currentUser.username)
        def anotherUser = createUser()

        expect:
        given().auth().oauth2(currentUserToken)

                .when()
                .get("/v2/players/" + anotherUser.id)

                .then()
                .statusCode(404)
    }
}
