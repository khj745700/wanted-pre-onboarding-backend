package com.wanted.preonboarding.backend.security.service;


import com.wanted.preonboarding.backend.entity.Member;
import com.wanted.preonboarding.backend.entity.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;


@AllArgsConstructor
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Member> byUsername = memberRepository.findByUsername(username);

        // username 찾지 못하는 예외
        if(byUsername.isEmpty()){
            throw new UsernameNotFoundException("UsernameNotFoundException");
        }

        return new MemberContext(byUsername.get());
    }
}
