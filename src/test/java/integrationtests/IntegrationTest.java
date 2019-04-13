package integrationtests;

import org.junit.BeforeClass;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.MalformedURLException;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import static org.hamcrest.Matchers.*;
import org.junit.AfterClass;
import org.junit.Test;
import test.utils.EmbeddedTomcat;
import testutils.TestUtils;
import utils.PuSelector;

public class IntegrationTest {

  private static final int SERVER_PORT = 7777;
  private static final String APP_CONTEXT = "/jwtbackend";

  public IntegrationTest() {
  }
  
  
  private static EmbeddedTomcat tomcat;
  @BeforeClass
  public static void setUpBeforeAll() throws ServletException, MalformedURLException, LifecycleException {
    //Setup the database for testing
    TestUtils.setupTestUsers(PuSelector.getEntityManagerFactory("pu_local_integration_test"));
    
    //Setup and start Embedded Tomcat for testing
    tomcat = new EmbeddedTomcat();
    tomcat.start(SERVER_PORT, APP_CONTEXT);
    
    //Setup RestAssured
    RestAssured.baseURI = "http://localhost";
    RestAssured.port = SERVER_PORT;
    RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;
  }
  
  @AfterClass
  public static void tearDownAfterAll(){
    tomcat.stop();
  }

  
  private static String securityToken;

  @Test
  public void serverIsRunning() {
    System.out.println("Testing is server UP");
    given().when().get().then().statusCode(200);
  }

  //Utility method to login and set the securityToken
  private static void login(String role, String password) {
    String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
    securityToken = given()
            .contentType("application/json")
            .body(json)
            .when().post("/api/login")
            .then()
            .extract().path("token");
  }

  private void logOut() {
    securityToken = null;
  }

  @Test
  public void testRestNoAuthenticationRequired() {
    given()
            .contentType("application/json")
            .when()
            .get("/api/info").then()
            .statusCode(200)
            .body("msg", equalTo("Hello anonymous"));
  }

  @Test
  public void testRestForAdmin() {
    login("admin", "test");
    given()
            .contentType("application/json")
            .accept(ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .get("/api/info/admin").then()
            .statusCode(200)
            .body("msg", equalTo("Hello to (admin) User: admin"));
      }

  @Test
  public void testRestForUser() {
    login("user", "test");
    given()
            .contentType("application/json")
            .header("x-access-token", securityToken)
            .when()
            .get("/api/info/user").then()
            .statusCode(200)
            .body("msg", equalTo("Hello to User: user"));
  }

  @Test
  public void userNotAuthenticated() {
    logOut();
    given()
            .contentType("application/json")
            .when()
            .get("/api/info/user").then()
            .statusCode(403);
  }

  @Test
  public void adminNotAuthenticated() {
    logOut();
    given()
            .contentType("application/json")
            .when()
            .get("/api/info/user").then()
            .statusCode(403);
  }

}
