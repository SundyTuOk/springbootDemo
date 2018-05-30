package com.tu.common.config.dbconfig;

import org.apache.ibatis.cache.Cache;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * 这个数据库缓存暂时未用到，考虑到以后真的碰到数据库压力问题再来
 * 弄这个
 */
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
