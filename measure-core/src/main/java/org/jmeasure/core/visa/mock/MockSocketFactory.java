package org.jmeasure.core.visa.mock;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jmeasure.core.device.ISocket;
import org.jmeasure.core.visa.factory.ISocketFactory;

/**
 * MockSocketFactory
 */
public class MockSocketFactory implements ISocketFactory {

    private final static Pattern pattern = Pattern.compile("MOCK::(?<class>.*)(?<name>::.*)?(::INSTR)?");

    @Override
    public boolean supports(String resourceURI) {
        return pattern.matcher(resourceURI).matches();
    }

    @Override
    public ISocket create(String resourceURI) {
        Matcher matcher = pattern.matcher(resourceURI);
        if(matcher.matches()) {
            String className = matcher.group("class");
            String instrumentName = matcher.group("name");
            return new MockSocket(className, instrumentName);
        }
        throw new IllegalArgumentException();
    }

    
}