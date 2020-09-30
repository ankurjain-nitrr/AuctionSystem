package service.lock;

import com.google.inject.ImplementedBy;
import service.lock.impl.LockServiceStripedImpl;

@ImplementedBy(LockServiceStripedImpl.class)
public interface ILockService {

    ILock getLock(String key);

    void releaseLock(ILock lock);
}
