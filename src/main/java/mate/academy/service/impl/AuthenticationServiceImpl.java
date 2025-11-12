package mate.academy.service.impl;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User userFromDb = userService.findByEmail(email).orElse(null);
        if (userFromDb == null || userFromDb.getPassword()
                .equals(HashUtil.hashPassword(password, userFromDb.getSalt()))) {
            throw new AuthenticationException("Can't authenticate user: "
                    + "wrong email or password, email: " + email);
        }
        return userFromDb;
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        if (userService.findByEmail(email).isPresent()) {
            throw new RegistrationException("Can't register user: "
                    + "user with email " + email + "already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        return userService.add(user);
    }
}
