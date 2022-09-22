package school.bonobono.fyb.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import school.bonobono.fyb.Config.KakaoOAuth;
import school.bonobono.fyb.Model.StatusTrue;
import school.bonobono.fyb.Service.OAuthService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class KakaoLoginController {

    private final OAuthService oAuthService;
    private final KakaoOAuth kakaoOAuth;

    @GetMapping("/kakao")
    public void getKakakoAuthUrl(HttpServletResponse response) throws IOException {
        response.sendRedirect(kakaoOAuth.responseUrl());
    }

    @GetMapping("login/kakao")
    public ResponseEntity<StatusTrue> kakaoLogin(
            @RequestParam(name = "code") String code) throws IOException {
        log.info("카카오 API 서버 code : " + code);
        return oAuthService.kakaoLogin(code);
    }
}
