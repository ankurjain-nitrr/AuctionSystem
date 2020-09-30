package service.lock.impl;

import com.google.common.util.concurrent.Striped;
import service.ConfigurationService;
import service.lock.ILock;
import service.lock.ILockService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.locks.Lock;

@Singleton
public class LockServiceStripedImpl implements ILockService {

    private static final String CONF_KEY_LOCK_SERVICE_GUICE_STRIPE_COUNT = "application.service.lock.striped.count";

    private Striped<Lock> locks;

    @Inject
    public LockServiceStripedImpl(ConfigurationService configurationService) {
        int stripes = configurationService.getInt(CONF_KEY_LOCK_SERVICE_GUICE_STRIPE_COUNT);
        this.locks = Striped.lock(stripes);
    }

    @Override
    public ILock getLock(String key) {
        Lock lock = locks.get(key);
        lock.lock();
        return new SimpleJavaLock(key, lock);
    }

    @Override
    public void releaseLock(ILock lock) {
        SimpleJavaLock simpleJavaLock = (SimpleJavaLock) lock;
        simpleJavaLock.getLock().unlock();
    }
}
