package com.ajay.ailms.security;

import com.ajay.ailms.config.SecurityConfig;
import com.ajay.ailms.entity.User;
import com.ajay.ailms.repo.UserRepository;
import com.ajay.ailms.util.AuthUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final AuthUtil authUtil;
    private final UserRepository userRepo;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("incoming request{}",request.getRequestURI());

        final String requestTokenHeader=request.getHeader("Authorization");
        if(requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }

        String token=requestTokenHeader.substring(7);
        if(!authUtil.isTokenValid(token)){
            filterChain.doFilter(request,response);
            return;
        }

        String email=authUtil.getUsernameFromToken(token);
        String role=authUtil.getRoleFromToken(token);

        if(email != null && SecurityContextHolder.getContext().getAuthentication()==null){
            var authority=new SimpleGrantedAuthority("ROLE_"+role);

            User user=userRepo.findByEmail(email).orElseThrow();
            UsernamePasswordAuthenticationToken authToken=
                    new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            List.of(authority)
                    );
            SecurityContextHolder.getContext().setAuthentication(authToken);

        }
        filterChain.doFilter(request,response);
    }
}
