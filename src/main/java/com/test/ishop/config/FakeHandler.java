package com.test.ishop.config;

import org.apache.log4j.Logger;
import ru.sbrf.ufs.platform.core.spring.context.PlatformNamespaceHandler;


public class FakeHandler extends PlatformNamespaceHandler {
    private final static Logger LOGGER = Logger.getLogger(FakeHandler.class);

    @Override
    protected String getTemplatesPath() {
        LOGGER.debug("FakeHandler.getTemplatesPath");
        return String.format("/%s/", getClass().getPackage().getName().replace('.', '/'));
    }

    @Override
    public void init() {
        LOGGER.debug("FakeHandler.init");
        this.registerParser(FakeParser.class, "fakeData");
    }
}
