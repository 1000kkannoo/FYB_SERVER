package school.bonobono.fyb.domain.shop.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import school.bonobono.fyb.domain.shop.Dto.ShopDto;
import school.bonobono.fyb.domain.shop.Service.ShopService;
import school.bonobono.fyb.global.Model.CustomResponseEntity;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("main")
public class ShopController {

    private final ShopService shopService;

    // Main 홈 페이지
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomResponseEntity<List<ShopDto.DetailListDto>> getAllShopAndUserInfo(
    ) {
        return CustomResponseEntity.success(shopService.getAllShopAndUserInfo());
    }

    // 사용자 최다조회수 API
    @GetMapping("rank/all")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomResponseEntity<List<ShopDto.DetailListDto>> getMostViewed(
    ) {
        return CustomResponseEntity.success(shopService.getMostViewed());
    }

    // 사용자 나이대별 최다조회수 API
    @GetMapping("rank/age")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomResponseEntity<List<ShopDto.DetailListDto>> getAgeViewed(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return CustomResponseEntity.success(shopService.getAgeViewed(userDetails));
    }

    // 사용자 성별 최다조회수 API
    @GetMapping("rank/gender")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomResponseEntity<List<ShopDto.DetailListDto>> getGenderViewed(
            @AuthenticationPrincipal final UserDetails userDetails
    ) {
        return CustomResponseEntity.success(shopService.getGenderViewed(userDetails));
    }

    // 쇼핑몰 클릭시 쇼핑몰 이용자의 빅데이터 분석
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomResponseEntity<ShopDto.SaveDto> saveShopData(
            @RequestBody ShopDto.SaveDto request,
            @AuthenticationPrincipal final UserDetails userDetails
            ) {
        return CustomResponseEntity.success(shopService.saveShopData(request, userDetails));
    }

    // Search 페이지 Post
    @PostMapping("shop")
    public CustomResponseEntity<List<ShopDto.DetailListDto>> getSearchShop(
            @RequestBody final ShopDto.SearchDto request
    ) {
        return CustomResponseEntity.success(shopService.getSearchShop(request));
    }
}
