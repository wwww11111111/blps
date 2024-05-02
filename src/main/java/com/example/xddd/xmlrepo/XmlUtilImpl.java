package com.example.xddd.xmlrepo;

import com.example.xddd.entities.User;
import com.example.xddd.security.Role;
import com.thoughtworks.xstream.XStream;
import org.springframework.stereotype.Service;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;

@Service
public class XmlUtilImpl {
    private final XStream xstream;
    public XmlUtilImpl(){
        this.xstream = new XStream();
        xstream.alias("user", User.class);
        xstream.alias("users", Users.class);
        xstream.alias("role", Role.class);
        xstream.addImplicitCollection(Users.class, "user");
        xstream.addImplicitCollection(User.class, "role");
    }

    public <T> Object getEntity(Class<T> convertClass, String aliasName, String xmlPath) {



        xstream.alias(aliasName, convertClass);
        try {
            File file = new File(xmlPath);
            FileReader reader = new FileReader(file);
            if (reader.read() > 0) {
                JAXBContext jaxbContext = JAXBContext.newInstance(convertClass);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                return jaxbUnmarshaller.unmarshal(file);
            }
        } catch (JAXBException | IOException e) {
            //System.err.println("Failed to load entity from "+xmlPath);
            e.printStackTrace();
        }
        return null;
    }

    public void saveEntity(Users usersToSave, String xmlPath) {
        xstream.alias("user", User.class);
        try {
            xstream.toXML(usersToSave, new FileWriter(xmlPath, false));
        } catch (IOException e) {
            //System.err.println("Failed to save entity into file "+xmlPath);
            e.printStackTrace();
        }
    }
}
