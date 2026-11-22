package com.sheasepherd.nescue.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.UUID;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NetNotFoundException extends RuntimeException {
    public NetNotFoundException(UUID netId) {
        super("Das Netz konnte nicht gefunden werden:" + netId);
    }
}
