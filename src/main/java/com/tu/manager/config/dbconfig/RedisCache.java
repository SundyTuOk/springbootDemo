package com.tu.manager.config.dbconfig;

import org.apache.ibatis.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;

@Component
public class RedisCache implements Cache {
    @Override
    public String getId() {
        return null;
    }

    @Override
    public void putObject(Object o, Object o1) {

    }

    @Override
    public Object getObject(Object o) {
        return null;
    }

    @Override
    public Object removeObject(Object o) {
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}
