package accepttests;

import integrationtests.IntegrationTest;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import static org.hamcrest.Matchers.equalTo;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import test.utils.EmbeddedTomcat;
import testutils.TestUtils;
import utils.PuSelector;

public class AcceptTestDemo extends IntegrationTest {

  @BeforeClass
  public static void setUpBeforeAll() {
    System.out.println("ACCEPT TEST");

    String dbDriver = "com.mysql.jdbc.Driver";
    String dbURL = "jdbc:mysql://207.154.192.117:3306/testdatabase";

    String dbUsername = "testuser";
    String dbPassword = "test";
    try {
      Class.forName(dbDriver);
      Connection con = (Connection) DriverManager.getConnection(dbURL, dbUsername, dbPassword);
      Statement st = con.createStatement();
      st.executeUpdate("DELETE FROM user_roles");
      st.executeUpdate("DELETE FROM roles");
      st.executeUpdate("DELETE FROM users");

      st.executeUpdate("INSERT INTO users (user_name, user_pass) VALUES ('user', 'test')");
      st.executeUpdate("INSERT INTO users (user_name, user_pass) VALUES ('admin', 'test')");

      st.executeUpdate("INSERT INTO roles (role_name) VALUES ('user')");
      st.executeUpdate("INSERT INTO roles (role_name) VALUES ('admin')");

      st.executeUpdate("INSERT INTO user_roles (user_name, role_name) VALUES ('user', 'user')");
      st.executeUpdate("INSERT INTO user_roles (user_name, role_name) VALUES ('admin', 'admin')");

      con.close();
    } catch (Exception e) {
      throw new RuntimeException(e.toString());
    }

    SERVER_PORT = 443;
    APP_CONTEXT = "/jwtbackend";
    SERVER_URL = "https://sem3.mydemos.dk";

    //Setup RestAssured
    RestAssured.baseURI = SERVER_URL;
    RestAssured.port = SERVER_PORT;
    RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;
  }

  @AfterClass
  public static void tearDownAfterAll() {
    //Must be here to override version in base   
  }

  @Test
  @Override
  public void serverIsRunning() {
    System.out.println("Testing is server UP");
    //This method is overriden since the line below courses problems when Nginx is used
    //given().when().get("/").then().statusCode(200);
    given().contentType("application/json").when().get("/api/info").then().statusCode(200);

  }

//This will just run all the test from IntegrationTest
}


/*
public class AcceptTestDemo {
  private static final String APP_CONTEXT = "/jwtbackend";  //IMPORTANT--> this should reflect the value in META-INF/context.xml
  private static EntityManagerFactory emf;
  

  @Test
  public void serverIsRunning() {
    System.out.println("TESTING IF SERVER IS RUNNING");
    given().when().get().then().statusCode(200); 
  }

 
  @BeforeClass
  public static void setUpBeforeAll() throws LifecycleException, ServletException, MalformedURLException {
     RestAssured.baseURI = "https://deploy.mydemos.dk/";
    //RestAssured.port = 80;
    //RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;
  }

  @AfterClass
  public static void tearDownAfterAll() throws LifecycleException {
   
  }
}

 */
