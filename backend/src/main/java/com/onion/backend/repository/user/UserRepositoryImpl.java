package com.onion.backend.repository.user;

import static com.onion.backend.entity.QUser.user;

import com.onion.backend.entity.User;
import com.onion.backend.exception.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {

    public UserRepositoryImpl() {
        super(User.class);
    }

    @Override
    public User getByEmail(String email) {
        return Optional.ofNullable(
            from(user)
                .where(user.email.eq(email))
                .fetchOne()
        ).orElseThrow(()-> new ResourceNotFoundException("회원 정보가 존재하지 않습니다."));
    }
}
