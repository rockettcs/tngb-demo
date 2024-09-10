package com.tngb.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class TcpServerRoute extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("netty:tcp://0.0.0.0:5000?textline=false&sync=false")
                .routeId("TCPStreamProcessorRoute")
                .log("Recieved Charactor streem is ${body} ")
                .process("CharacterAccumulatorProcessor");
    }
}
