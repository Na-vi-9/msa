package com.spart.msa.auth;

import com.spart.msa.auth.dto.SignUpRequestDto;
import com.spart.msa.auth.entity.UserRoleEnum;
import com.spart.msa.auth.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class AuthService {

    @Value("${spring.application.name}")
    private String issuer;

    @Value("${service.jwt.access-expiration}")
    private Long accessExpiration;

    private final SecretKey secretKey;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(@Value("${service.jwt.secret-key}") String secretKey,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(secretKey));
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User signUp(SignUpRequestDto signUpRequestDto) {
        String encodedPassword = passwordEncoder.encode(signUpRequestDto.getPassword());

        User user = new User();
        user.setUsername(signUpRequestDto.getUsername());
        user.setPassword(encodedPassword);
        user.setName(signUpRequestDto.getUsername());
        user.setEmail(signUpRequestDto.getEmail());
        user.setSlackId(signUpRequestDto.getSlackId());

        if(signUpRequestDto.getRole() == null){
            user.setRole(UserRoleEnum.NORMAL);
        }
        return userRepository.save(user);
    }


    public String createAccessToken(String username, String password) {
        return Jwts.builder()
                .claim("username", username)
                .claim("password", password)
                .issuer(issuer)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String signIn(String username, String password) {
        User user = userRepository.findByusername(username)
                .orElseThrow(()-> new IllegalArgumentException("Invalid username or password."));

        if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IllegalArgumentException("Invalid password.");
        }
        return createAccessToken(user.getUsername(), user.getRole().toString());
    }
}
