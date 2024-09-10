package com.tngb;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component(value = "CharacterAccumulatorProcessor")
public class CharacterAccumulatorProcessor implements Processor {
    private Logger log = LoggerFactory.getLogger(CharacterAccumulatorProcessor.class);
    private StringBuilder stringBuilder = new StringBuilder();
    private StringBuilder stringBuilder2 = new StringBuilder();
    @Override
    public void process(Exchange exchange) throws Exception {
        // Get the received character
        String character = exchange.getIn().getBody(String.class);
        // Append it to the StringBuilder
        stringBuilder.append(character);

        // System.out.println("Current String: " + stringBuilder.toString());
        String output="";
        if("|".equals(character)){
            output = stringBuilder.toString().replaceAll("\n", "").replaceAll("\r", "");
            stringBuilder2.append(output);
            //  stringBuilder.setLength(0);
        }

        processBuffer(stringBuilder2, exchange);
    }

    private void processBuffer(StringBuilder buffer, Exchange exchange) {
        List<String> parts = extractParts(buffer);
        String msg = "";
        if(parts.size() > 0){
            for (int i = 0; i < parts.size(); i++) {
                String streamData = "Msg" + (i + 1);
                exchange.setProperty(streamData, parts.get(i));
                msg = streamData;
            }
           // System.out.println(msg+": "+exchange.getProperty(msg));
            log.info("{}",msg+": "+exchange.getProperty(msg));
        }
    }

    private List<String> extractParts(StringBuilder buffer) {
        List<String> parts = new ArrayList<>();
        while (stringBuilder2.indexOf("|") != -1) {
            int delimiterIndex = stringBuilder2.indexOf("|");
            String part = stringBuilder2.substring(0, delimiterIndex).trim();
            stringBuilder2.delete(0, delimiterIndex + 1);

            if (!part.isEmpty()) {
                parts.add(part);
            }
        }
        return parts;
    }
}
