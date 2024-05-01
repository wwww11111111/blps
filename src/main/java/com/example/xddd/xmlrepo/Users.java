package com.example.xddd.xmlrepo;

import com.example.xddd.entities.User;
import lombok.Data;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Data
public class Users {
    private List<User> user;

    public Users(){
        user = new ArrayList<>();
    }
}
