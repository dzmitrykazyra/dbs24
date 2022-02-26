package org.dbs24.card.pmt.rest.payment.validator;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;
import org.dbs24.card.pmt.dao.PaymentDao;
import org.dbs24.card.pmt.entity.ApplePayment;
import org.dbs24.card.pmt.entity.GooglePayment;
import org.dbs24.card.pmt.rest.payment.wa.api.cancel.CancelWaPaymentInfo;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.validator.AbstractValidatorService;
import org.dbs24.validator.EntityInfoValidator;
import org.dbs24.validator.ErrorInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

import static java.lang.String.format;
import static org.dbs24.card.pmt.consts.CardPaymentConsts.ErrMsg.INVALID_FIELD_VALUE;
import static org.dbs24.stmt.StmtProcessor.*;
import static org.dbs24.validator.Error.INVALID_ENTITY_ATTR;
import static org.dbs24.validator.ErrorInfo.create;
import static org.dbs24.validator.Field.APPLY_ORIGINAL_TRANSACTION_ID;
import static org.dbs24.validator.Field.GOOGLE_PURCHASE_TOKEN;

@Log4j2
@Component
@EqualsAndHashCode(callSuper = true)
public class CancelWaPaymentInfoWalidator extends AbstractValidatorService<CancelWaPaymentInfo> implements EntityInfoValidator<CancelWaPaymentInfo> {

    final PaymentDao paymentDao;

    public CancelWaPaymentInfoWalidator(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    @Override
    public Collection<ErrorInfo> validate(CancelWaPaymentInfo cancelWaPaymentInfo) {
        return StmtProcessor.<ErrorInfo>fillCollection(buildErrorCollection(), errors -> {

            // paymentId
            ifTrue(notNull(cancelWaPaymentInfo.getPaymentId()), () -> {

                final Optional<ApplePayment> byApplePayment = paymentDao.findApplePaymentById(cancelWaPaymentInfo.getPaymentId());
                final Optional<GooglePayment> byGooglePayment = paymentDao.findGooglePaymentById(cancelWaPaymentInfo.getPaymentId());

                ifTrue(byApplePayment.isEmpty() && byGooglePayment.isEmpty(),
                        () -> errors.add(create(INVALID_ENTITY_ATTR, APPLY_ORIGINAL_TRANSACTION_ID, INVALID_FIELD_VALUE.concat(
                                format("Payment not found (paymentId - %d)", cancelWaPaymentInfo.getPaymentId()))))
                );
            });

            ifNotNull(cancelWaPaymentInfo.getAppleTransactionId(), () -> {

                final Optional<ApplePayment> byApplePayment = paymentDao.findAppleTransactionIdPayment(cancelWaPaymentInfo.getAppleTransactionId());

                // find ApplePayment by
                byApplePayment.ifPresentOrElse(applePayment -> log.debug("found apple payment ({})", applePayment.getPaymentId()),
                        () -> errors.add(create(INVALID_ENTITY_ATTR, APPLY_ORIGINAL_TRANSACTION_ID, INVALID_FIELD_VALUE.concat(
                                format("ApplePayment not found (appleTransactionId - %s)", cancelWaPaymentInfo.getAppleTransactionId()))))
                );
            });

            ifNotNull(cancelWaPaymentInfo.getGoogleOrderId(), () -> {

                final Optional<GooglePayment> byGooglePayment = paymentDao.findGoogleOrderIdPayment(cancelWaPaymentInfo.getGoogleOrderId());

                // find GooglePayment by
                byGooglePayment.ifPresentOrElse(googlePayment -> log.debug("found google payment ({})", googlePayment.getPaymentId()),
                        () -> errors.add(create(INVALID_ENTITY_ATTR, GOOGLE_PURCHASE_TOKEN, INVALID_FIELD_VALUE.concat(
                                format("GooglePayment not found (googleOrderId - %s)", cancelWaPaymentInfo.getGoogleOrderId()))))
                );
            });
        });
    }
}
