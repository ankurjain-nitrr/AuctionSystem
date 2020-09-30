package dao;

import com.google.inject.ImplementedBy;
import dao.impl.AuthenticationTokenDAOMongoImpl;
import model.AuthenticationToken;

@ImplementedBy(AuthenticationTokenDAOMongoImpl.class)
public interface IAuthenticationTokenDAO {

    AuthenticationToken get(String token);

    void create(AuthenticationToken authenticationToken);

}
