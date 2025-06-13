package com.ejemplo.alojamiento.config;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenBlacklist {

    private Set<String> blacklist = ConcurrentHashMap.newKeySet();

    public void add(String token) {
        blacklist.add(token);
    }

    public boolean contains(String token) {
        return blacklist.contains(token);
    }
}

