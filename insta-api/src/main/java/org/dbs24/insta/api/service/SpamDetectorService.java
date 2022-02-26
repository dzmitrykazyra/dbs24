/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.api.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.dbs24.application.core.service.funcs.ServiceFuncs;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class SpamDetectorService extends AbstractApplicationService {

    private String blackListKw = "studio, forex, instrument, fuck, tourism, history, бижутерия, реклама, услуг, психология, территория, ресторан, строитель, педагог, питомник, стоматолог, ювелир, студия, корпорац, корпорат, фабрика, цветы, растения, типография, botox, botex, онлайн, online, маркет, юрист, школа, курсы, имидж, украшени, эпиляция, почта, лаборатор, лазер, компания, мастер, biznes, busines, limousin, prokat, dostavka, доставка, отделка, салон, обувь, белье, мебель, уборка, территория, заказ, работа, ссылк, $, корчма, #, |, \\, /,coin, funny, _, подарк, extra, shop, пицца, дизайн, декор, •, блог, ремонт, Аксессуары, ЦЕНТР, Лофт, Медицинск, Компьютер, Двери, Грузоперевоз, Фотосалон, Подарки, Йога, БАННЕР, Авторск, Наращив, Интернет, СПА-центр, Агент, СЧАСТЛИВ, Страстн, Bitcoin, Магазин, Клининг, Маникюр, экскурс, семейн, фотограф, ТЕЛЕФОН, бизнес, нлп, терапия, реконструкц, худеем, худеть, парикмахер, косметика, косметолог, .by, .ru, .com, .org, club, заработок, ставки, betting, рассрочка, потолки, покупк, ручн, инструктор, лайфхак, заработ, инвестиции, инвестор, крипто, карты таро";

    final Collection<String> comonBlackListKw;

    public SpamDetectorService() {
        comonBlackListKw = ServiceFuncs.createConcurencyCollection();
    }

    @PostConstruct
    public void initializeService() {
        comonBlackListKw.addAll(new ArrayList<String>(Arrays.asList(blackListKw.replaceAll(" ", "").toLowerCase().split(","))));
        log.debug("comonBlackListKw = {}", comonBlackListKw);
    }

    public Boolean isSpam(String value4Test) {

        return StmtProcessor.isNull(value4Test) ? false : isSpamInternal(value4Test.toLowerCase());

    }

    private Boolean isSpamInternal(String value4Test) {

        return (comonBlackListKw.stream().anyMatch(value4Test::contains));

    }
}
