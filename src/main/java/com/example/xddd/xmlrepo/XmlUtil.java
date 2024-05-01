package com.example.xddd.xmlrepo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface XmlUtil {
    void saveEntity(List<?> saveEntity, String xmlPath);
    <T> Object getEntity(Class<T> convertClass, String aliasName, String xmlPath);
}