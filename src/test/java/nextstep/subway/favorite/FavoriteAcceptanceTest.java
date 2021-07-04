package nextstep.subway.favorite;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_요청;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록_요청;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("즐겨찾기 관련 기능")
public class FavoriteAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "limdingdong@test.com";
    private static final String PASSWORD = "passowrd";
    private static TokenResponse tokenResponse;

    private static final String EMAIL_MAX = "max@test.com";
    private static TokenResponse tokenResponse_max;

    private StationResponse 강남역;
    private StationResponse 정자역;
    private StationResponse 양재역;
    private StationResponse 판교역;


    @BeforeEach
    public void setUp() {
        super.setUp();

        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        정자역 = 지하철역_등록되어_있음("정자역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);

        LineResponse 신분당선 = 지하철_노선_등록되어_있음("신분당선", "red", 강남역.getId(), 정자역.getId(), 35).as(LineResponse.class);
        지하철_노선에_지하철역_등록_요청(신분당선, 강남역, 양재역, 5);
        지하철_노선에_지하철역_등록_요청(신분당선, 양재역, 판교역, 20);

        회원_생성을_요청(EMAIL, PASSWORD, 20);
        tokenResponse = 로그인_요청(EMAIL, PASSWORD).as(TokenResponse.class);

        회원_생성을_요청(EMAIL_MAX, EMAIL_MAX, 25);
        tokenResponse_max = 로그인_요청(EMAIL_MAX, EMAIL_MAX).as(TokenResponse.class);
    }

    @DisplayName("즐겨찾기 관리")
    @Test
    void favorite() {
        // when
        FavoriteRequest favoriteRequest = new FavoriteRequest(판교역.getId(), 강남역.getId());
        FavoriteRequest favoriteRequest2 = new FavoriteRequest(정자역.getId(), 양재역.getId());
        ExtractableResponse<Response> createResponse = 즐겨찾기_생성_요청(favoriteRequest);
        ExtractableResponse<Response> createResponse2 = 즐겨찾기_생성_요청(favoriteRequest2);
        // then
        즐겨찾기_생성됨(createResponse);
        즐겨찾기_생성됨(createResponse2);
        
        // when
        ExtractableResponse<Response> findAllResponse = 즐겨찾기_목록조회();
        // then
        즐겨찾기_조회됨(findAllResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 즐겨찾기_삭제(createResponse, tokenResponse_max);
        // then
        즐겨찾기_삭제되지_않음(deleteResponse);

        // when
        deleteResponse = 즐겨찾기_삭제(createResponse, tokenResponse);
        // then
        즐겨찾기_삭제됨(deleteResponse);
    }

    public static ExtractableResponse<Response> 즐겨찾기_생성_요청(FavoriteRequest request) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static void 즐겨찾기_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_목록조회() {
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> 즐겨찾기_삭제(ExtractableResponse<Response> response, TokenResponse tokenResponse) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(tokenResponse.getAccessToken())
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    private void 즐겨찾기_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private void 즐겨찾기_삭제되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}