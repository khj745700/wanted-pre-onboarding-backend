package com.wanted.preonboarding.backend.security.provider;
import com.wanted.preonboarding.backend.security.service.MemberContext;
import com.wanted.preonboarding.backend.security.token.JsonAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;




public class JsonAuthenticationProvider implements AuthenticationProvider {


    private UserDetailsService userDetailsService;

    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String loginId = (String)authentication.getPrincipal();
        String password = (String)authentication.getCredentials();


            //id 검증
        MemberContext memberContext = (MemberContext) userDetailsService.loadUserByUsername(loginId);

            //pw 검증
        if(!passwordEncoder.matches(password, memberContext.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }

            /* 여기서 추가 검증 절차 진행 가능 */

            // 인증 토큰 생성 반환 (Token은 Auth의 Child)
        return new JsonAuthenticationToken(memberContext.getMember(), null, memberContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(JsonAuthenticationToken.class);
    }

    public void setUserDetailsService(UserDetailsService service) {
        this.userDetailsService = service;
    }

    public void setbCryptPasswordEncoder(PasswordEncoder encoder) {
        this.passwordEncoder = encoder;
    }
}
