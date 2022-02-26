package org.dbs24.tik.mobile.service.impl;

import lombok.extern.log4j.Log4j2;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.mobile.entity.dto.tiktok.TiktokPostIdentifierListDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.TiktokUserDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchInfoBySecUserIdDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchLatestPostsRequestDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSpecificPostDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchAccountResponseDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchLatestPostsResponseDto;
import org.dbs24.tik.mobile.entity.dto.tiktok.viewer.SearchSinglePostDto;
import org.dbs24.tik.mobile.service.TiktokAccountService;
import org.dbs24.tik.mobile.service.exception.http.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Component
public class TiktokAccountServiceImpl implements TiktokAccountService {

    private WebClient tiktokViewerWebClient;

    /**
     * Tiktok viewer (non-authorized search client) uri to get tiktok user data
     */
    @Value("${api.viewer.url}")
    private String tikSearchUri;

    /**
     * Viewer endpoint relative path to search
     * user post
     * by required parameters (for example, weblink)
     */
    @Value("${api.viewer.endpoint.post}")
    private String postUrl;

    /**
     * Viewer endpoint relative path to search
     * user data and user's videos short data
     * by username and posts quantity
     */
    @Value("${api.viewer.endpoint.search}")
    private String searchUrl;

    /**
     * Viewer endpoint relative path to search
     * user data and user's videos short data
     * by sec_user_id and posts quantity
     */
    @Value("${api.viewer.endpoint.search-by-sid}")
    private String searchBySidUrl;

    /**
     * Viewer endpoint relative path to search
     * user data and user's videos full data
     * by username and posts quantity
     */
    @Value("${api.viewer.endpoint.search-full}")
    private String searchFullUrl;

    public TiktokAccountServiceImpl() {
    }

    @PostConstruct/**/
    public void initWebClient() throws SSLException {
        tiktokViewerWebClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .baseUrl(tikSearchUri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    @Override
    public Mono<TiktokUserDto> searchTiktokUserBySecUserId(String secUserId) {

        return tiktokViewerWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(searchBySidUrl)
                        .build()
                )
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(
                        Mono.just(
                                StmtProcessor.create(
                                        SearchInfoBySecUserIdDto.class,
                                        searchInfoBySecUserIdDto -> {
                                            searchInfoBySecUserIdDto.setSecUserId(secUserId);
                                            searchInfoBySecUserIdDto.setPostsQuantity(0);
                                        })
                        ),
                        SearchInfoBySecUserIdDto.class
                )
                .retrieve()
                .bodyToMono(SearchLatestPostsResponseDto.class)
                .map(SearchLatestPostsResponseDto::getUser);
    }

    @Override
    public Mono<TiktokUserDto> searchTiktokUserByUsername(String username) {

        return tiktokViewerWebClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(searchUrl)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(
                        createSearchFullRequestMono(username, 0),
                        SearchLatestPostsRequestDto.class
                )
                .retrieve()
                .bodyToMono(SearchAccountResponseDto.class)
                .doOnNext(dto -> {
                    log.info("RESULT SEARCH TIKTOK USER = {}", dto);
                    if (dto.getUser().getName() == null) {
                        throw new BadRequestException();
                    }
                })
                .map(SearchAccountResponseDto::getUser);
    }

    @Override
    public Mono<TiktokPostIdentifierListDto> searchLatestUserPostsByQuantity(String tiktokUsername, int latestPostsQuantity) {

        return tiktokViewerWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(searchFullUrl)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(
                        createSearchFullRequestMono(tiktokUsername, latestPostsQuantity),
                        SearchLatestPostsRequestDto.class
                )
                .retrieve()
                .bodyToMono(SearchLatestPostsResponseDto.class)
                .map(TiktokPostIdentifierListDto::of);
    }

    private Mono<SearchLatestPostsRequestDto> createSearchFullRequestMono(String username, int postsQuantity) {

        return Mono.just(
                StmtProcessor.create(
                        SearchLatestPostsRequestDto.class,
                        searchLatestPostsRequestDto -> {
                            searchLatestPostsRequestDto.setUsername(username);
                            searchLatestPostsRequestDto.setPostNumbers(postsQuantity);
                        }
                )
        );
    }

    @Override
    public Mono<SearchSinglePostDto> searchSinglePostByLink(String link) {
        int i = 0;
        while (i < 30){
            Mono<SearchSinglePostDto> searchSinglePostDtoMono = requestToSearchPostByLink(link);
            SearchSinglePostDto post = searchSinglePostDtoMono.toProcessor().block();
            if(post != null && post.getTiktokUserPostDto().getCover() != null){
                return Mono.just(post);
            }
            i++;
        }
        throw new BadRequestException();
    }

    private Mono<SearchSinglePostDto> requestToSearchPostByLink(String link){
        return tiktokViewerWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(postUrl)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(
                        Mono.just(receiveSearchRequestByLink(link)),
                        SearchSpecificPostDto.class
                )
                .retrieve()
                .bodyToMono(SearchSinglePostDto.class);
    }


    private SearchSpecificPostDto receiveSearchRequestByLink(String link) {
        return StmtProcessor.create(SearchSpecificPostDto.class,
                searchSpecificPostDto -> {
                    if (link.matches("https://m[.].+")) {
                        searchSpecificPostDto.setShareLink(link);
                    } else if (link.matches("https://vm[.].+")) {
                        searchSpecificPostDto.setShortLink(link);
                    } else {
                        searchSpecificPostDto.setWebLink(link);
                    }
                }
        );
    }
}