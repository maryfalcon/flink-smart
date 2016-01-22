package org.myorg.service;

import org.myorg.model.User;
import org.myorg.persistor.Persistor;

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

}
