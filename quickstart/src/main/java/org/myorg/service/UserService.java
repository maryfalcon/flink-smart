package org.myorg.service;

import org.myorg.model.User;
import org.myorg.persistor.Persistor;

import javax.servlet.http.HttpServletRequest;

/**
 * Author: s.kiselyov
 * Date: 21.01.2016
 */
public class UserService {

    private Persistor persistor = new Persistor();

    public boolean handleLogin(String hash) {
        User user = persistor.checkUserByHash(hash);
        return user != null;
    }

    public boolean loginOrRegister(HttpServletRequest request) {
        String userLoginHash = request.getHeader("Authorization");
        if (userLoginHash != null) {
            userLoginHash = userLoginHash.substring(6);
        }
        User user = persistor.checkUserByHash(userLoginHash);
        if (user == null) {
            user = new User();
            user.setLoginhash(userLoginHash.getBytes());
            user = persistor.insertUser(user);
        }
        return user != null;
    }
}
