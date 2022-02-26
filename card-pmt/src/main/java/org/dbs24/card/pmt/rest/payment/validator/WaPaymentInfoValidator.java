/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.card.pmt.rest.payment.validator;

import org.dbs24.card.pmt.component.RefsService;
import org.dbs24.card.pmt.dao.PaymentDao;
import org.dbs24.card.pmt.entity.Payer;
import org.dbs24.card.pmt.rest.payment.wa.api.WaPaymentInfo;
import org.dbs24.consts.SysConst;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.AbstractValidatorService;
import org.dbs24.validator.EntityInfoValidator;
import org.dbs24.validator.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static org.dbs24.card.pmt.consts.CardPaymentConsts.ErrMsg.INVALID_FIELD_VALUE;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.PS_APPLE_PAY;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.PaymentServiceEnum.PS_GOOGLE_PAY;
import static org.dbs24.consts.SysConst.STRING_NULL_VALUE;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.Field.*;

@Component
public class WaPaymentInfoValidator extends AbstractValidatorService<WaPaymentInfo> implements EntityInfoValidator<WaPaymentInfo> {

    final RefsService refsService;
    final PaymentDao paymentDao;

    public WaPaymentInfoValidator(RefsService refsService, PaymentDao paymentDao) {
        this.refsService = refsService;
        this.paymentDao = paymentDao;
    }

    //==========================================================================
    @Override
    public Collection<ErrorInfo> validate(WaPaymentInfo paymentInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(),
                errors -> {

                    StmtProcessor.ifNull(paymentInfo.getPaymentDate(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PAYMENT_DATE, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE))),
                            () -> {
                                if (paymentInfo.getPaymentDate() < 0) {
                                    errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PAYMENT_DATE, INVALID_FIELD_VALUE.concat(paymentInfo.getPaymentDate().toString())));
                                }
                            });

                    // contract type id
                    StmtProcessor.ifNull(paymentInfo.getContractTypeId(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CONTRACT_TYPE, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)))
                    );

                    // subscripton amount
                    StmtProcessor.ifNull(paymentInfo.getSubsriptionAmount(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, SUBSRIPTION_AMOUNT, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)))
                    );
                    // finish date
                    StmtProcessor.ifNull(paymentInfo.getEndDate(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, END_DATE, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)))
                    );

                    StmtProcessor.ifNull(paymentInfo.getPackageName(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PACKAGE, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)))
                    );

                    StmtProcessor.ifNull(paymentInfo.getPlatform(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PLATFORM, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)))
                    );

                    final String loginToken = paymentInfo.getLoginToken();
                    final Boolean loginTokenIsEmpty = StmtProcessor.isNull(loginToken);

                    StmtProcessor.ifNull(paymentInfo.getPaymentServiceId(),
                            () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PAYMENT_SERVICE, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE))),
                            () -> {

                                // apple payment
                                StmtProcessor.ifTrue(paymentInfo.getPaymentServiceId().equals(PS_APPLE_PAY.getCode()), () -> {

                                    StmtProcessor.ifTrue((StmtProcessor.isNull(paymentInfo.getAppleOriginalTransactionId()) && loginTokenIsEmpty),
                                            () -> {
                                                errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)));
                                                errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, APPLY_ORIGINAL_TRANSACTION_ID, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)));
                                            });

                                    StmtProcessor.ifTrue((StmtProcessor.notNull(paymentInfo.getAppleOriginalTransactionId()) && loginTokenIsEmpty), () -> {

                                        final Optional<Payer> optPayer = findPayerByApplePayment(paymentInfo.getAppleOriginalTransactionId());

                                        // find loginToken by old ApplePayment
                                        optPayer.ifPresentOrElse(payer -> paymentInfo.setLoginToken(payer.getLoginToken()),
                                                () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, APPLY_ORIGINAL_TRANSACTION_ID, INVALID_FIELD_VALUE.concat(
                                                        String.format("Payer not found (appleOriginalTransactionId - %s)", paymentInfo.getAppleOriginalTransactionId()))))
                                        );
                                    });
                                });

                                // google payment
                                StmtProcessor.ifTrue(paymentInfo.getPaymentServiceId().equals(PS_GOOGLE_PAY.getCode()), () -> {

                                    StmtProcessor.ifTrue((StmtProcessor.isNull(paymentInfo.getGooglePurchaseToken()) && loginTokenIsEmpty),
                                            () -> {
                                                errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)));
                                                errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, GOOGLE_PURCHASE_TOKEN, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE)));
                                            });

                                    StmtProcessor.ifTrue((StmtProcessor.notNull(paymentInfo.getGooglePurchaseToken()) && loginTokenIsEmpty), () -> {

                                        final Optional<Payer> optPayer = findPayerByGooglePayment(paymentInfo.getGooglePurchaseToken());

                                        // find loginToken by old ApplePayment
                                        optPayer.ifPresentOrElse(payer -> paymentInfo.setLoginToken(payer.getLoginToken()),
                                                () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, GOOGLE_PURCHASE_TOKEN, INVALID_FIELD_VALUE.concat(
                                                        String.format("Payer not found (googlePurchaseToken - %s)", paymentInfo.getGooglePurchaseToken()))))
                                        );
                                    });
                                });
                            });

                    // no login token
                    StmtProcessor.ifNull(paymentInfo.getLoginToken(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, LOGIN_TOKEN, INVALID_FIELD_VALUE.concat(": not defined"))));

                    // invalid payment summ
                    StmtProcessor.ifNull(paymentInfo.getPaymentSumm(), ()
                            -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PAYMENT_SUMM, INVALID_FIELD_VALUE.concat(STRING_NULL_VALUE))),
                            () -> StmtProcessor.ifTrue((paymentInfo.getPaymentSumm().compareTo(SysConst.LONG_ZERO) <= 0),
                                    () -> errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PAYMENT_SUMM, INVALID_FIELD_VALUE.concat(paymentInfo.getPaymentSumm().toString()))))
                    );

                    try {
                        refsService.findCountry(StmtProcessor.notNull(paymentInfo.getCountryCode()) ? paymentInfo.getCountryCode() : "BY");
                    } catch (Throwable throwable) {
                        errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, COUNTRY, throwable.getMessage()));
                    }

                    try {
                        refsService.findCurrency(paymentInfo.getCurrencyIso());
                    } catch (Throwable throwable) {
                        errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, CURRENCY, throwable.getMessage()));
                    }

                    try {
                        refsService.findPaymentService(paymentInfo.getPaymentServiceId());
                    } catch (Throwable throwable) {
                        errors.add(ErrorInfo.create(INVALID_ENTITY_ATTR, PAYMENT_SERVICE, throwable.getMessage()));
                    }
                }
        );
    }

    //==========================================================================
    private Optional<Payer> findPayerByApplePayment(String appleOriginalTransactionId) {
        return paymentDao.findPayerApplePayment(appleOriginalTransactionId);
    }

    //==========================================================================
    private Optional<Payer> findPayerByGooglePayment(String googlePurchaseToken) {
        return paymentDao.findPayerGooglePayment(googlePurchaseToken);
    }
}
