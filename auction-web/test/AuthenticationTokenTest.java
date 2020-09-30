import model.AuthenticationToken;
import org.junit.Assert;
import org.junit.Test;

public class AuthenticationTokenTest {

    @Test
    public void testExpired() {
        AuthenticationToken token = new AuthenticationToken("user", "token", -1);
        Assert.assertFalse(token.validate("user"));
    }

    @Test
    public void testNotExpired() {
        AuthenticationToken token = new AuthenticationToken("user", "token", 1);
        Assert.assertTrue(token.validate("user"));
    }

    @Test
    public void testValidUser() {
        AuthenticationToken token = new AuthenticationToken("user", "token", 1);
        Assert.assertFalse(token.validate("user1"));
    }

    @Test
    public void testInvalidUser() {
        AuthenticationToken token = new AuthenticationToken("user", "token", 1);
        Assert.assertTrue(token.validate("user"));
    }


}
