package application;

import dao.IAuctionDAO;
import dao.IAuthenticationTokenDAO;
import dao.IUserDAO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class SystemInitializer {

    private IUserDAO userDAO;
    private IAuthenticationTokenDAO authenticationTokenDAO;
    private IAuctionDAO auctionDAO;

    @Inject
    public SystemInitializer(IUserDAO userDAO, IAuthenticationTokenDAO authenticationTokenDAO, IAuctionDAO auctionDAO) {
        this.userDAO = userDAO;
        this.authenticationTokenDAO = authenticationTokenDAO;
        this.auctionDAO = auctionDAO;
        init();
    }

    void init() {
        log.info("Initializing the system. Creating Users. Generating token. Creating Indexes");
        // TODO generate random users, tokens and auctions
    }

}
