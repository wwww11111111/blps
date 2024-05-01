package com.example.xddd.xmlrepo;

import com.example.xddd.entities.User;
import com.example.xddd.security.Role;
import com.thoughtworks.xstream.XStream;
import org.springframework.stereotype.Service;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Service
public class XmlUtilImpl implements XmlUtil {
    private final XStream xstream;
    public XmlUtilImpl(){
        this.xstream = new XStream();
//        xstream.alias("user", User.class);
//        xstream.alias("users", List.class);
    }
    @Override
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

    @Override
    public void saveEntity(List<?> saveEntity, String xmlPath) {
//        xstream.alias("user", User.class);
//        xstream.alias("users", List.class);
//        try {
//            List<Role> listRole = (List<Role>) saveEntity.get(0);
////            xstream.toXML(saveEntity, new FileWriter(xmlPath, false));
//            xstream.toXML(listRole.get(0), new FileWriter(xmlPath, false));
//        } catch (IOException e) {
//            //System.err.println("Failed to save entity into file "+xmlPath);
//            e.printStackTrace();
//        }
    }
}
