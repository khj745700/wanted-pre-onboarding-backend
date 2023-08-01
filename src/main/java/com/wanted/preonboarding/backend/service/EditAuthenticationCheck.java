package com.wanted.preonboarding.backend.service;

import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.exception.EditAuthenticationException;

public interface EditAuthenticationCheck {
    Member getMember();

    default void authenticationCheck(Member session) {
        if(!getMember().equals(session)){
            throw new EditAuthenticationException();
        }
    }
}
