package service.lock.impl;

import service.lock.ILock;

import java.util.concurrent.locks.Lock;

public class SimpleJavaLock implements ILock {

    private Lock lock;
    private String lockKey;

    public SimpleJavaLock(String lockKey, Lock lock) {
        this.lock = lock;
        this.lockKey = lockKey;
    }

    public Lock getLock() {
        return lock;
    }

    @Override
    public String getKey() {
        return lockKey;
    }
}
