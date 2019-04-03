import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CredentialsInput {
    @XmlElement public String username;
    @XmlElement public String password;
}
