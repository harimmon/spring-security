package com.metacoding.securityapp1.domain.user;

import com.metacoding.securityapp1.controller.dto.UserRequest;
import com.metacoding.securityapp1.core.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void 회원가입(UserRequest.Join reqDTO) {
        String encPassword = bCryptPasswordEncoder.encode(reqDTO.getPassword());
        String roles = "USER";
        userRepository.save(roles, reqDTO.getUsername(), encPassword, reqDTO.getEmail());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public String 로그인(UserRequest.Login reqDTO) {
        User user = userRepository.findByUsername(reqDTO.getUsername());

        if (user == null) throw new RuntimeException("유저네임을 찾을 수 없습니다");

        if (!bCryptPasswordEncoder.matches(reqDTO.getPassword(), user.getPassword()))
            throw new RuntimeException("비밀번호가 틀렸습니다");

        // 4. JWT 토큰 생성
        String jwtToken = JwtUtil.create(user);

        return jwtToken;
    }
}