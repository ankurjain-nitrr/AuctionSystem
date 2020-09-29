package application;

import lombok.extern.slf4j.Slf4j;
import play.inject.ApplicationLifecycle;

import javax.inject.Inject;
import javax.inject.Singleton;
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
        log.info("Starting Service");
    }

    public void onStop() {
        log.info("Stopping Service");
    }

}
