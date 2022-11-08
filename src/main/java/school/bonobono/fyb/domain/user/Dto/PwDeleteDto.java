package school.bonobono.fyb.domain.user.Dto;

import lombok.*;

public class PwDeleteDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request{
        String pw;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request2{
        String token;
    }
}
