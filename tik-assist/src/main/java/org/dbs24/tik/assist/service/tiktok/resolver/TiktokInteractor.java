package org.dbs24.tik.assist.service.tiktok.resolver;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.nullsafe.StopWatcher;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.dao.TiktokAccountDao;
import org.dbs24.tik.assist.entity.domain.TiktokAccount;
import org.dbs24.tik.assist.entity.domain.User;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokPostIdentifierDto;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserDto;
import org.dbs24.tik.assist.entity.dto.tiktok.TiktokUserPostDto;
import org.dbs24.tik.assist.entity.dto.tiktok.request.SearchInfoBySecUserIdDto;
import org.dbs24.tik.assist.entity.dto.tiktok.request.SearchLatestPostsRequestDto;
import org.dbs24.tik.assist.entity.dto.tiktok.request.SearchSpecificPostDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.SearchAccountResponseDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.SearchLatestPostsResponseDto;
import org.dbs24.tik.assist.entity.dto.tiktok.response.SearchSinglePostDto;
import org.dbs24.tik.assist.service.exception.NoSuchTiktokAccountException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Log4j2
@Service
public class TiktokInteractor {

    private WebClient openApiWebClient;

    private final TiktokAccountDao tiktokAccountDao;

    @Value("${config.tik-search.uri}")
    private String tikSearchUri;

    @Value("${config.tik-search.api-search-full}")
    private String searchFullApi;

    @Value("${config.tik-search.api-search-by-sid}")
    private String searchBySidApi;

    @Value("${config.tik-search.api-post}")
    private String postApi;

    @Value("${config.tik-search.api-search}")
    private String searchApi;

    private final int postsQuantityParamToPostsApiIgnore = 0;

    public TiktokInteractor(TiktokAccountDao tiktokAccountDao) {

        this.tiktokAccountDao = tiktokAccountDao;
    }

    public TiktokAccount getTiktokAccount(String tiktokUsername, User user) {

        return tiktokAccountDao.findOptionalByTiktokUsernameAndUser(tiktokUsername, user)
                .orElseGet(
                        () -> tiktokAccountDao.save(
                                TiktokAccount.builder()
                                        .secUserId(searchTiktokUserByUsername(tiktokUsername).getSid())
                                        .accountUsername(tiktokUsername)
                                        .user(user)
                                        .build()
                        )
                );
    }

    /**
     * @param tiktokUsernames tiktok account usernames bounded on one User
     * @param user to bound many tiktok accounts to make User tiktok accounts pool
     */
    public List<TiktokAccount> createOrGetExistingTiktokAccounts(String[] tiktokUsernames, User user) {

        return Arrays.stream(tiktokUsernames)
                .map(tiktokUsername -> getTiktokAccount(tiktokUsername, user))
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void initWebClient() throws SSLException {

/*        final SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        final ReactorClientHttpConnector reactorClientHttpConnector = new ReactorClientHttpConnector(
                HttpClient
                        .create()
                        .secure(contextSpec -> contextSpec.sslContext(sslContext))
        );*/

        openApiWebClient = WebClient.builder()
            /*    .clientConnector(reactorClientHttpConnector)*/
                .baseUrl(tikSearchUri)
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(16 * 1024 * 1024))
                        .build())
                .build();
    }

    public TiktokUserDto searchTiktokUserBySecUserId(String secUserId) {

        Mono<SearchInfoBySecUserIdDto> searchBySecUserIdMono = Mono.just(
                StmtProcessor.create(
                        SearchInfoBySecUserIdDto.class,
                        searchInfoBySecUserIdDto -> {
                            searchInfoBySecUserIdDto.setSecUserId(secUserId);
                            searchInfoBySecUserIdDto.setPostsQuantity(0);
                        }
                )
        );

        return openApiWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(searchBySidApi)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(searchBySecUserIdMono, SearchInfoBySecUserIdDto.class)
                .retrieve()
                .bodyToMono(SearchLatestPostsResponseDto.class)
                .toProcessor()
                .block()
                .getUser();
    }

    public TiktokUserDto searchTiktokUserByUsername(String username) {

        Mono<SearchLatestPostsRequestDto> requestMono = createSearchFullRequestMono(username, postsQuantityParamToPostsApiIgnore);

        TiktokUserDto tiktokUserDto = openApiWebClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(searchApi)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(requestMono, SearchLatestPostsRequestDto.class)
                .retrieve()
                .bodyToMono(SearchAccountResponseDto.class)
                .toProcessor().block().getUser();

        if (tiktokUserDto.getName() == null) {
            throw new NoSuchTiktokAccountException(HttpStatus.BAD_REQUEST);
        }

        return tiktokUserDto;
    }

    public List<TiktokPostIdentifierDto> searchLatestUserPosts(int latestPostsQuantity, TiktokAccount tiktokAccount) {

        Mono<SearchLatestPostsRequestDto> requestMono = createSearchFullRequestMono(tiktokAccount.getAccountUsername(), latestPostsQuantity);

        SearchLatestPostsResponseDto latestAccountPosts = openApiWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(searchFullApi)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(requestMono, SearchLatestPostsRequestDto.class)
                .retrieve()
                .bodyToMono(SearchLatestPostsResponseDto.class)
                .toProcessor().block();

        return Arrays.stream(latestAccountPosts.getPosts())
                .map(
                        latestAccountPost -> StmtProcessor.create(
                                TiktokPostIdentifierDto.class,
                                tiktokPostIdentifierDto -> {
                                    tiktokPostIdentifierDto.setSecUserId(latestAccountPosts.getUser().getSid());
                                    tiktokPostIdentifierDto.setAwemeId(latestAccountPost.getAwemeId());
                                }))
                .collect(Collectors.toList());
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

    public SearchSinglePostDto searchPostByWebLink(String postWeblink) {

        Mono<SearchSpecificPostDto> searchPostMonoRequest = Mono.just(
                StmtProcessor.create(
                        SearchSpecificPostDto.class,
                        searchSpecificPostDto -> searchSpecificPostDto.setWebLink(postWeblink)
                )
        );

        return openApiWebClient
                .post()
                .uri(uriBuilder -> uriBuilder
                        .path(postApi)
                        .build())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(searchPostMonoRequest, SearchSpecificPostDto.class)
                .retrieve()
                .bodyToMono(SearchSinglePostDto.class)
                .toProcessor().block();
    }
}
