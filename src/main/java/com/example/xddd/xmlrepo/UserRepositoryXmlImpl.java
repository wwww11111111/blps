package com.example.xddd.xmlrepo;


import com.example.xddd.entities.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class UserRepositoryXmlImpl implements UserRepositoryInterface {
    private final XmlUtilImpl xmlUtil;
    private final String xmlPath = "C:\\Users\\Stepan Myts\\Downloads\\Telegram Desktop\\xddd\\xddd\\src\\main\\resources\\users.xml";

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


        for (int i = 0; i < users.getUser().size(); i++) {
            if (users.getUser().get(i).getId().equals(user.getId())) {
                users.getUser().remove(i);
            }
        }

        users.getUser().add(user);
        xmlUtil.saveEntity(users, xmlPath);
        List<User> list = new ArrayList<>();
        list.add(user);
    }
}
