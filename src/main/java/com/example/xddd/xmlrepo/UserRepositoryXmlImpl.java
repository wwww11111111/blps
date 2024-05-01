package com.example.xddd.xmlrepo;


import com.example.xddd.entities.User;
import com.example.xddd.security.ERole;
import com.example.xddd.security.Role;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class UserRepositoryXmlImpl implements UserRepositoryInterface {
    private final XmlUtilImpl xmlUtil;
    private final String xmlPath = "C:\\Users\\Stepan Myts\\Desktop\\xddd\\src\\main\\resources\\users.xml";

    public UserRepositoryXmlImpl(XmlUtilImpl xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    @Override
    public User findByUsername(String login) {
        Users users = (Users)xmlUtil.getEntity(Users.class, "userList", xmlPath);
        if (users==null || users.getUser()==null) return null;
        List<User> userEntities = users.getUser();
        for (User cur: userEntities) {
            if (cur.getLogin().equals(login)) return cur;
        }
        return null;
    }

    @Override
    public void save(User user) {
//        Users users = (Users)xmlUtil.getEntity(Users.class, "users", xmlPath);
//
//        if (users==null) users = new Users();
//        users.getUser().add(user);
//        System.out.println(users.getUser());
//        System.out.println(users.getUser().size());
//        xmlUtil.saveEntity(users.getUser(), xmlPath);
        List<List<Role>> list = new ArrayList<>();
        list.add(user.getRoles());
        xmlUtil.saveEntity(list, xmlPath);
    }
}
