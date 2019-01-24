package com.bjss.assignment.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class XmlProcessor {
    private static final Map<Class, JAXBContext> jaxbContextMap = new ConcurrentHashMap<>();

    /**
     * private constructor to restrict instantiation
     */
    private XmlProcessor() {

    }

    private static JAXBContext getJAXBContext(Class clazz) throws JAXBException {
        JAXBContext jaxbContext = jaxbContextMap.get(clazz);
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(clazz);
            jaxbContextMap.put(clazz, jaxbContext);
        }
        return jaxbContext;
    }

    public static Object unmarshal(Class type, String inputXML) throws JAXBException {
        JAXBContext jaxbContext = getJAXBContext(type);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        return um.unmarshal(new StringReader(inputXML));
    }
}
