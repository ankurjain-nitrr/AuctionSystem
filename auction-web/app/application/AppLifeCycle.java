package application;

import dao.IAuctionDAO;
import dao.IAuthenticationTokenDAO;
import dao.IUserDAO;
import dao.impl.AuctionDAOMongoImpl;
import dao.impl.AuthenticationTokenDAOMongoImpl;
import dao.impl.UserDAOMongoImpl;
import lombok.extern.slf4j.Slf4j;
import model.AuthenticationToken;
import model.User;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Singleton
@Slf4j
public class AppLifeCycle {


    @Inject
    public AppLifeCycle(ApplicationLifecycle applicationLifecycle) {
        onStart();
        applicationLifecycle.addStopHook(() -> {
            onStop();
            return CompletableFuture.completedFuture(null);
        });
    }

    public void onStart() {
    }

    public void onStop() {
        log.info("Stopping Service");
    }

}
