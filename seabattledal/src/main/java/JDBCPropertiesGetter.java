import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JDBCPropertiesGetter {


    public static Properties getPropertiesFile(){
        FileInputStream input = null;
        // load a properties file
        try{
           input = new FileInputStream("db.prop");
            Properties prop = new Properties();
            try {
                prop.load(input);
                return prop;

            } catch (IOException ex) {
                Logger.getLogger(UserDataAccess.class.getName()).log(Level.SEVERE, null, ex);   // TODO: Look into how log is made in Java. Interesting.
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
