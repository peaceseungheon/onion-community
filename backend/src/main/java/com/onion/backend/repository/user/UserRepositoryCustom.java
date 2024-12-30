package com.onion.backend.repository.user;

import com.onion.backend.entity.User;

public interface UserRepositoryCustom {

    User getByEmail(String email);

}
