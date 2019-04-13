package utils;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;


public class Tester {
  public static void main(String[] args) {
    Properties props = new Properties(); 
                  
//      props.put("javax.persistence.jdbc.driver","com.mysql.jdbc.Driver");
//      props.put("javax.persistence.jdbc.url" ,"jdbc:mysql://localhost:3306/seed?zeroDateTimeBehavior=convertToNull");
//      props.put("javax.persistence.jdbc.user","root");
//      props.put("javax.persistence.jdbc.password","test");
//      props.put("javax.persistence.schema-generation.database.action","create");
//      //Persistence.generateSchema("pu", props);
      
      
      
      props.put("javax.persistence.jdbc.driver","com.mysql.jdbc.Driver");
      props.put("javax.persistence.jdbc.url","jdbc:mysql://localhost:3306/test-dev");
      props.put("javax.persistence.jdbc.user","root");
      props.put("javax.persistence.jdbc.password","test");
      props.put("javax.persistence.schema-generation.database.action","create");
      EntityManager em = Persistence.createEntityManagerFactory("pu", props).createEntityManager();
  }
  
}
