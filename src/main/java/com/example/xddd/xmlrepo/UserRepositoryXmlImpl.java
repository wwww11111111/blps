package com.example.xddd.xmlrepo;


import com.example.xddd.entities.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryXmlImpl implements UserRepositoryInterface {
    private final XmlUtilImpl xmlUtil;
    private final String xmlPath = "C:\\Users\\Stepan Myts\\Desktop\\xddd\\src\\main\\resources\\users.xml";

    public UserRepositoryXmlImpl(XmlUtilImpl xmlUtil) {
        this.xmlUtil = xmlUtil;
    }

    public Long getNextId() {
        Users users = (Users) xmlUtil.getEntity(Users.class, "users", xmlPath);
        if (users.getUser().isEmpty()) {
            return 0L;
        } else {
            return users.getUser().get(users.getUser().size() - 1).getId() + 1;
        }
    }

    public User findByLogin(String login) {
        Users users = (Users) xmlUtil.getEntity(Users.class, "userList", xmlPath);



        if (users == null || users.getUser() == null) return null;
        List<User> userEntities = users.getUser();
        for (User cur : userEntities) {
            if (cur.getLogin().equals(login)) return cur;
        }
        return null;
    }

    @Override
    public void save(User user) {
        Users users = (Users) xmlUtil.getEntity(Users.class, "users", xmlPath);


        try {
            System.out.println(users.getUser().get(0).getRole().getClass());
        } catch (Exception ignored) {

        }


        System.out.println(users);
        System.out.println(users.getUser().size());
        users.getUser().add(user);
        System.out.println(users.getUser().size());
        xmlUtil.saveEntity(users, xmlPath);
        List<User> list = new ArrayList<>();
        list.add(user);
    }
}
