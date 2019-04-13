package utils;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.config.PersistenceUnitProperties;

// Returns an EntityManagerFactory using the provided PU_NAME)
public class PuSelector {

  private static String PU_NAME;
  private static EntityManagerFactory emf;
  public static final String FILE_EXTENSION = ".properties";
   
 
  public static Properties loadProperties(String propertyFile) {
    Properties props = new Properties();
    String propertyFileName = propertyFile+FILE_EXTENSION;
    try {
     // System.out.println("File: +PuSelector.class.getResource("/META-INF"+propertyFileName));
      props.load(PuSelector.class.getResourceAsStream("/META-INF/"+propertyFileName));
    } catch (Exception ex) {
      throw new RuntimeException("Could not load properies for :"+propertyFileName);
    }
    return props;
  }
  
  public static EntityManagerFactory getEntityManagerFactory(String persistenceUnitName){

    //This ensures that only ONE factory will ever be used. If a test has set to a test db, this will be used also forexample from the login end-point
    if (emf != null) {
      System.out.println("--- Returned am EntityManagerFactory for  --> " + emf.getProperties().get("javax.persistence.jdbc.url"));
      return emf;
    }
    
    PU_NAME = persistenceUnitName;
    System.out.println("PU_NAME ---> "+PU_NAME);
    
    //You can override the given PU_NAME from maven like this: mvn -DPU_NAME=pu-test-on-travis verify
    String puVal = System.getProperty("PU_NAME");
    if (puVal != null) {
      PU_NAME = puVal;
    }   

    //System.out.println("Persistence Unit Name: "+PU_NAME);
    
    Properties props = loadProperties(PU_NAME);
    //Only reason to give persistence file another name is that it must NOT be git-ignored, which i what we usually do with persistence.xml
    props.setProperty(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML, "META-INF/persistence-for-all.xml");
    if(props != null){
      System.out.println("Props ---> "+ props.size());
    } else{
      System.out.println("Props could not be read");
    }

   /*    
    boolean isDeployed = (System.getenv("PRODUCTION") != null && System.getenv("PRODUCTION").equals("DIGITALOCEAN"));
    //https://stackoverflow.com/questions/18583881/changing-persistence-unit-dynamically-jpa
    
     */
    
    emf = Persistence.createEntityManagerFactory("DO_NOT_RENAME_ME", props);

    return emf;
  }
}
