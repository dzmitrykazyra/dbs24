/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.time.Duration;
import javax.ws.rs.core.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;
import org.dbs24.config.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import reactor.core.publisher.Mono;
import org.dbs24.entity.*;
import org.dbs24.entity.classic.*;
import static org.dbs24.consts.WorldChessConst.*;

/**
 *
 * @author Козыро Дмитрий
 */
@ExtendWith(SpringExtension.class)
//@WebFluxTest
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@EnableAutoConfiguration(exclude = ReactiveSecurityAutoConfiguration.class)
@Import({ChessSecurityConfig.class})
public class PlayersTests<G extends ClassicGame, P extends AbstractPlayer> extends TestUtil4Chess {

    @Autowired
    private WebTestClient webTestClient;

    @Value("${reactive.rest.timeout:5000}")
    private Integer timeoutDefault = 50000;

    @Value("${webflux.security.uid}")
    private String uid = "no_uid";

    @Value("${webflux.security.pwd}")
    private String pwd = "no_pwd";

    @BeforeEach
    public void setUp() {

        webTestClient = webTestClient
                .mutate()
                .responseTimeout(Duration.ofMillis(timeoutDefault))
                //                .defaultHeaders(header
                //                        -> header.setBasicAuth(this.getEnvironment().getProperty("webflux.security.uid"),
                //                        this.getEnvironment().getProperty("webflux.security.pwd")))
                //.defaultHeaders(header -> header.setBasicAuth(this.uid, this.pwd, StandardCharsets.UTF_8))
                //.defaultHeaders(header -> header.setBasicAuth(this.uid, this.pwd))
                .filter(basicAuthentication(this.uid, this.pwd))
                .defaultHeader(HttpHeaders.USER_AGENT, "WebTestClient")
                //            .defaultHeader(HttpHeaders.AUTHORIZATION, passwordEncoder.encode(this.uid + ":" + this.pwd))
                .build();
    }

    //==========================================================================
    private final <T extends Player> T buildTestPlayer() {
        final T chessPlayer = (T) this.generateTestPlayer();
        //final Class<Player> classPlayer = (Class<Player>) WorldChessConst.CHESS_PLAYER_CLASS;
        final Mono<Player> monoPlayer = Mono.just(chessPlayer);

        final T createdPlayer
                = (T) webTestClient
                        //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                        .post()
                        .uri(URI_CREATE_CHESS_PLAYER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        //.header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", pwd4msg))
                        .body(monoPlayer, CHESS_PLAYER_CLASS)
                        .exchange()
                        //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                        // and use the dedicated DSL to test assertions against the response
                        .expectStatus()
                        .is2xxSuccessful()
                        .expectBody(CHESS_PLAYER_CLASS)
                        .returnResult()
                        .getResponseBody();

//        //======================================================================        
//        // действие авторизация игрока
        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(URI_EXECUTE_ACTION)
                        .queryParam("actionId", ACT_AUTHORIZE_PLAYER)
                        .queryParam("entityId", createdPlayer.entityId())
                        .queryParam("entClass", createdPlayer.getClass().getCanonicalName())
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.body(createdRetailLoanContract, classRetailLoanContract)
                .exchange()
                //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                // and use the dedicated DSL to test assertions against the response
                .expectStatus()
                .isOk();

        return createdPlayer;
    }

    //==========================================================================
    private final G buildTestGame(final P chessPlayer1, final P chessPlayer2) {

        final G chessGame = (G) this.generateTestGame(chessPlayer1, chessPlayer2);
        //final Class<Player> classPlayer = (Class<Player>) WorldChessConst.CHESS_PLAYER_CLASS;
        final Mono<G> monoGame = Mono.just(chessGame);

        final G reloadedChessGame = (G) webTestClient
                //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                .post()
                .uri(URI_CREATE_CHESS_GAME)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", pwd4msg))
                .body(monoGame, CHESS_GAME_CLASS)
                .exchange()
                //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                // and use the dedicated DSL to test assertions against the response
                .expectStatus()
                .isOk()
                .expectBody(CHESS_GAME_CLASS)
                .returnResult()
                .getResponseBody();

//        //======================================================================        
//        // действие авторизация партии
        webTestClient
                .post()
                .uri(uriBuilder
                        -> uriBuilder
                        .path(URI_EXECUTE_ACTION)
                        .queryParam("actionId", ACT_AUTHORIZE_GAME)
                        .queryParam("entityId", reloadedChessGame.entityId())
                        .queryParam("entClass", reloadedChessGame.getClass().getCanonicalName())
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.body(createdRetailLoanContract, classRetailLoanContract)
                .exchange()
                //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                // and use the dedicated DSL to test assertions against the response
                .expectStatus()
                .isOk();

        return reloadedChessGame;
    }
    //==========================================================================

    private void saveTestGame(final G classicChessGame) {

        final Mono<G> monoGame = Mono.just(classicChessGame);

        webTestClient
                //                .mutate().filter(basicAuthentication(this.uid, this.pwd)).build()
                .post()
                .uri(URI_CREATE_CHESS_GAME)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                //.header(HttpHeaders.AUTHORIZATION, String.format("Basic %s", pwd4msg))
                .body(monoGame, CHESS_GAME_CLASS)
                .exchange()
                //.onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new Pizza5xxException()))
                // and use the dedicated DSL to test assertions against the response
                .expectStatus()
                .isOk();

    }

    //==========================================================================
//    private void buildTestMove(final Player chessPlayer1, final ClassicGame chessGame) {
//        this.generateTestMove(chessGame, chessPlayer1, MoveNotice.findMoveNotice("!"));
//    }
    //==========================================================================
    @Test
    public void testCreatePlayer() {

        // Игрок 1
        final P chessPlayer1 = (P) this.<AbstractPlayer>buildTestPlayer();
        // Игрок 2
        final P chessPlayer2 = (P) this.<AbstractPlayer>buildTestPlayer();

        // создаем партию
        final G chessGame = this.<ClassicGame, AbstractPlayer>buildTestGame(chessPlayer1, chessPlayer2);

        // создаем ходы
//        this.buildTestMove(chessPlayer1, chessGame);
//        this.buildTestMove(chessPlayer2, chessGame);

        // сохраняем партию
        this.saveTestGame(chessGame);

    }
}
