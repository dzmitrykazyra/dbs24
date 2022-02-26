/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.application.core.service.funcs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author Козыро Дмитрий
 */
public final class TestFuncs {

    static final Random rand = new Random();

    public static final String generateTestString15() {
        return generateTestString(15);
    }

    public static final String generateTestString20() {
        return generateTestString(20);
    }

    public static final String generateTestString(String... values) {
        return values[rand.nextInt(values.length)];
    }

    @Deprecated
    public static final Integer generateTestInteger(Integer... values) {
        return values[rand.nextInt(values.length)];
    }

    public static final <T> T selectFrom(T... values) {
        return values[rand.nextInt(values.length)];
    }

    public static final <T> T selectFromCollection(Collection<T> values) {

        //Assert.isTrue(!values.isEmpty(), "selectFromCollection: Collection is empty".toUpperCase());

        return values.stream().skip(rand.nextInt(values.size())).findAny().orElseThrow();
    }

    public static final <T> T selectFromCollectionNullAllowed(Collection<T> values) {

        //Assert.isTrue(!values.isEmpty(), "selectFromCollection: Collection is empty".toUpperCase());

        return rand.nextBoolean() ? values.stream().skip(rand.nextInt(values.size())).findAny().orElseThrow() : null;
    }

    @Deprecated
    public static Byte generateTestBytes(Byte... values) {
        return values[rand.nextInt(values.length)];
    }

    public static final Integer generateTestRangeInteger(Integer from, Integer to) {
        return rand.nextInt((to - from) + 1) + from;
    }

    public static final Long generateTestLong() {
        return Math.abs(rand.nextLong());
    }

    public static final LocalDateTime generateTestLocalDateTime() {

        final Boolean direct = generateBool();
        final LocalDateTime randLocalDateTime;

        if (direct) {
            randLocalDateTime = LocalDateTime.now().plusDays(generateTestInteger(generateTestRangeInteger(0, 1000))).plusHours(generateTestRangeInteger(0, 1000)).plusMinutes(generateTestRangeInteger(0, 1000));
        } else {
            randLocalDateTime = LocalDateTime.now().minusDays(generateTestInteger(generateTestRangeInteger(0, 1000))).minusHours(generateTestRangeInteger(0, 1000)).minusMinutes(generateTestRangeInteger(0, 1000));
        }

        return randLocalDateTime;
    }

    public static final String generateTestString(int strLength) {
        return UUID.randomUUID().toString().substring(1, strLength);
    }

    public static final Long generateLong() {
        return System.currentTimeMillis();
    }

    public static final Long generateUnsignedLong() {

        return Math.abs(System.currentTimeMillis());
    }

    public static final byte[] generate100Bytes() {

        return generateBytes(100);
    }

    public static final byte[] generate1000Bytes() {

        return generateBytes(1000);
    }

    public static final byte[] generate10Bytes() {

        return generateBytes(10);
    }

    public static final byte[] generateBytes(Integer maxSize) {

        final byte[] randomBytes = new byte[rand.nextInt(maxSize)];

        rand.nextBytes(randomBytes);

        return randomBytes;
    }

    public static final Boolean generateBool() {
        return rand.nextBoolean();
    }

    public static final BigDecimal generateBigDecimal(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static final BigDecimal generateBigDecimal(BigDecimal bigDecimal) {

        //Assert.isTrue(bigDecimal.signum() == 1, "BigDecimal should be > 0");

        return generateBigDecimal(BigDecimal.valueOf(0), bigDecimal);
    }
}
