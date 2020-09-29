import application.AppLifeCycle;
import com.google.inject.AbstractModule;
import dao.IUserDAO;
import dao.impl.UserDAOMongoImpl;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(AppLifeCycle.class).asEagerSingleton();
        bind(IUserDAO.class).to(UserDAOMongoImpl.class);
    }
}
