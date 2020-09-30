import application.AppLifeCycle;
import application.SystemInitializer;
import com.google.inject.AbstractModule;
import dao.IAuctionBidDAO;
import dao.IAuctionDAO;
import dao.IAuthenticationTokenDAO;
import dao.IUserDAO;
import dao.impl.AuctionBidDAOMongoImpl;
import dao.impl.AuctionDAOMongoImpl;
import dao.impl.AuthenticationTokenDAOMongoImpl;
import dao.impl.UserDAOMongoImpl;
import service.lock.ILock;
import service.lock.ILockService;
import service.lock.impl.LockServiceStripedImpl;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(AppLifeCycle.class).asEagerSingleton();
        bind(SystemInitializer.class).asEagerSingleton();
    }
}
