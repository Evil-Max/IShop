package com.test.ishop.config;


import com.google.common.base.Predicate;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.sbrf.ufs.platform.core.spring.context.SimpleTemplateBeanDefinitionParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeParser extends SimpleTemplateBeanDefinitionParser {
    private final static Logger LOGGER = Logger.getLogger(FakeParser.class);

    /**
     * @param template Тело шаблона FreeMarker
     */
    public FakeParser(String template) {
        super(template);
    }

    @Override
    protected void processBody(Element element, Map<String, Object> data) {
        final List<Pair<String, String>> entry = new ArrayList<>();
        data.put("map", entry);

        processElements(element, "message", new Predicate<Element>() {
            @Override
            public boolean apply(Element element) {
                data.put("message", Validate.notNull(element.getAttribute("value")));
                LOGGER.debug("message:"+element.getAttribute("value"));
                return true;
            }
        });
        processElements(element, "map", new Predicate<Element>() {
            @Override
            public boolean apply(Element element) {
                Map<String,String> map = new HashMap<>();
                if (element.hasChildNodes()) {
                    LOGGER.debug("map:");
                    NodeList nodes = element.getChildNodes();
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        if (node instanceof Element) {
                            Element el = (Element) node;
                            if (getName(el).equals("entry")) {
                                entry.add(ImmutablePair.of(
                                        Validate.notEmpty(el.getAttribute("key")),
                                        Validate.notEmpty(el.getAttribute("value"))
                                        ));
                                LOGGER.debug("entry: key:"+el.getAttribute("key")+", value:"+el.getAttribute("value"));
                            }
                        }
                    }
                }
                return true;
            }
        });
    }

/*
    @Override
    protected void processBody(Element element, Map<String, Object> data) {
        processElements(element, new Predicate<Element>() {
            @Override
            public boolean apply(Element element) {
                String name=getName(element);
                LOGGER.debug(name);
                if (element.hasAttributes()) {
                    for (int i = 0; i < element.getAttributes().getLength(); i++) {
                        String attrName = element.getAttributes().item(i).getNodeName();
                        String attrStringValue = element.getAttributes().item(i).getNodeValue();
                        LOGGER.debug(name+": name:"+attrName+", value:"+attrStringValue);
                    }
                }
                if (element.hasChildNodes()) {
                    NodeList nodes = element.getChildNodes();
                    for (int i = 0; i < nodes.getLength(); i++) {
                        Node node = nodes.item(i);
                        if (node instanceof Element) {
                            Element el = (Element) node;
                            LOGGER.debug(name+": elementname:"+getName(el));
                            if (getName(el).equals("entry")) {
                                LOGGER.debug(name+": key:"+el.getAttribute("key")+", value:"+el.getAttribute("value"));
                            }
                        }
                    }
                }
                return true;
            }
        });
    }
*/

}
