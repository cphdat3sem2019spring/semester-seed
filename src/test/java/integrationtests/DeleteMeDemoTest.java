package integrationtests;

import org.junit.BeforeClass;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.parsing.Parser;
import java.net.MalformedURLException;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.junit.AfterClass;
import org.junit.Ignore;
import org.junit.Test;
import test.utils.EmbeddedTomcat;

@Ignore
public class DeleteMeDemoTest {

  private static final int SERVER_PORT = 7777;
  private static final String APP_CONTEXT = "/jwtbackend";  //IMPORTANT--> this should reflect the value in META-INF/context.xml
  private static EntityManagerFactory emf;
  
  public DeleteMeDemoTest() throws NamingException {}

  @Test
  public void serverIsRunning() {
    System.out.println("TESTING IF SERVER IS RUNNING");
    given().when().get().then().statusCode(200); 
  }

  
  private static EmbeddedTomcat tomcat;
  @BeforeClass
  public static void setUpBeforeAll() throws LifecycleException, ServletException, MalformedURLException {
     RestAssured.baseURI = "http://localhost";
    RestAssured.port = SERVER_PORT;
    RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;

    
     tomcat = new EmbeddedTomcat();
    tomcat.start(SERVER_PORT, APP_CONTEXT);
  }

  @AfterClass
  public static void setUpAfterAll() throws LifecycleException {
   tomcat.stop();
    //emf.close();
  }
  

}
