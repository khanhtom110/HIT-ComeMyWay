package com.hit.comemyway.security;

import com.hit.comemyway.constant.ErrorMessage;
import com.hit.comemyway.entity.User;
import com.hit.comemyway.exception.extended.AppException;
import com.hit.comemyway.exception.extended.ResourceNotFoundException;
import com.hit.comemyway.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(400, ErrorMessage.User.USER_NOT_EXISTED));
        return new CustomUserDetails(user);
    }
}
