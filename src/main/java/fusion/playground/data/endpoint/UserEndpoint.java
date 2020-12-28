package fusion.playground.data.endpoint;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import fusion.playground.data.CrudEndpoint;
import fusion.playground.data.service.UserService;
import fusion.playground.data.entity.User;
import fusion.playground.views.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
public class UserEndpoint extends CrudEndpoint<User, String>
{
    private UserService service;

    public UserEndpoint(@Autowired UserService service) {
        this.service = service;
    }

    @Override
    protected UserService getService() {
        return service;
    }

    public void createUser(UserVO userVO)
    {
        getService().createUser(userVO);
    }

    public User getUserByOktaId(String oktaId)
    {
        return getService().findByOktaUserId(oktaId);
    }

}
