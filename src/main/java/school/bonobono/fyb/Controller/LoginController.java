package school.bonobono.fyb.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.bonobono.fyb.Dto.UserLoginDto;
import school.bonobono.fyb.Entity.userToken;
import school.bonobono.fyb.Exception.CustomErrorCode;
import school.bonobono.fyb.Exception.CustomException;
import school.bonobono.fyb.Jwt.TokenProvider;
import school.bonobono.fyb.Model.StatusTrue;
import school.bonobono.fyb.Repository.TokenRepository;
import school.bonobono.fyb.Repository.UserRepository;

import javax.validation.Valid;

import static school.bonobono.fyb.Exception.CustomErrorCode.NOT_EMAIL_FORM;
import static school.bonobono.fyb.Exception.CustomErrorCode.NOT_SOCIAL_LOGIN;
import static school.bonobono.fyb.Model.Model.AUTHORIZATION_HEADER;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class LoginController {
    public static final CustomErrorCode LOGIN_FALSE = CustomErrorCode.LOGIN_FALSE;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/login")
    public ResponseEntity<StatusTrue> authorize(@Valid @RequestBody UserLoginDto.Request request) {

        LOGIN_VALIDATION(request);

        // 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPw());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        // 토큰 유효성 검증을 위한 데이터 저장 (로그아웃을 위한 장치)
        tokenRepository.save(userToken.builder()
                .token("Bearer " + jwt)
                .build());

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(StatusTrue.LOGIN_STATUS_TRUE, httpHeaders, HttpStatus.OK);
    }

    private void LOGIN_VALIDATION(UserLoginDto.Request request) {
        if (request.getPw().equals("google"))
            throw new CustomException(NOT_SOCIAL_LOGIN);

        if (!request.getEmail().contains("@"))
            throw new CustomException(NOT_EMAIL_FORM);

        userRepository.findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new CustomException(LOGIN_FALSE)
                );

        if (!passwordEncoder.matches(
                request.getPw(),
                userRepository.findByEmail(request.getEmail())
                        .get()
                        .getPw()
        )
        ) {
            throw new CustomException(LOGIN_FALSE);
        }
    }
}
