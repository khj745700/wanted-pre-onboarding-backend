package com.wanted.preonboarding.backend.utils;

import com.wanted.preonboarding.backend.entity.Member;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextHolder {

    public static Member getSession() {
        try {
            return (Member) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (ClassCastException e) {
            return null;
        }
    }

}
