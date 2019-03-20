package com.bjss.assignment.util;

import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class XmlProcessor {
    private static final Map<Class, JAXBContext> jaxbContextMap = new ConcurrentHashMap<>();
    private static Logger logger = Logger.getLogger(XmlProcessor.class);

    /**
     * private constructor to restrict instantiation. Should be accessed in static way.
     */
    private XmlProcessor() {

    }

    private static JAXBContext getJAXBContext(Class clazz) {
        return jaxbContextMap.computeIfAbsent(clazz, XmlProcessor::createNewJaxbConetxt);
    }

    private static JAXBContext createNewJaxbConetxt(Class clazz) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(clazz);
        } catch (JAXBException e) {
            logger.error(e.getMessage(), e);
        }
        return jaxbContext;
    }

    public static Object unmarshal(Class type, String inputXML) throws JAXBException {
        JAXBContext jaxbContext = getJAXBContext(type);
        Unmarshaller um = jaxbContext.createUnmarshaller();
        return um.unmarshal(new StringReader(inputXML));
    }
}
