import application.AppLifeCycle;
import com.google.inject.AbstractModule;

public class Module extends AbstractModule {

    @Override
    public void configure() {
        bind(AppLifeCycle.class).asEagerSingleton();
    }
}
