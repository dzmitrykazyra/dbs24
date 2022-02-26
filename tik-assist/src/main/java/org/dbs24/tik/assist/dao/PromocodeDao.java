package org.dbs24.tik.assist.dao;

import org.dbs24.tik.assist.entity.domain.Promocode;

public interface PromocodeDao {

    Promocode activatePromocode(Integer userId, String promocodeValue);

    boolean verifyPromocodeForUser(Promocode promocode, Integer userId);

    boolean verifyPromocodeValueForUser(String promocodeValue, Integer userId);

    void invalidatePromocode(Promocode promocode);

    Promocode save(Promocode promocode);
}
