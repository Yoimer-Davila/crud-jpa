package com.crudjpa.client;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public interface IHealthClient {
    @RequestMapping(value = "healthcheck", method = RequestMethod.HEAD)
    ResponseEntity<Void> isOk();
}
