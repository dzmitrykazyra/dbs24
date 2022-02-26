/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.action;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.extern.log4j.Log4j2;
import static org.dbs24.insta.reg.consts.InstaConsts.ActionCodes.ACT_GET_VALIDATION_CODE_FROM_EMAIL;
import org.dbs24.stmt.StmtProcessor;
import org.springframework.beans.factory.annotation.Value;

@Log4j2
@ActionCodeId(ACT_GET_VALIDATION_CODE_FROM_EMAIL)
public class ActGetValidationCodeFromMail extends AbstractAction {

    @Value("${config.account.creation-time:250000}")
    private Long creationTime;    
    
    @Override
    public void perform() {

        
        final LocalDateTime start = this.getAccount().getCreateDate();
        final LocalDateTime finish = LocalDateTime.now();

        final Long necessaryDelay = creationTime - ChronoUnit.MILLIS.between(start, finish);

        if (necessaryDelay > 0) {
            log.info("perform necessary delay: {}/{}", necessaryDelay, creationTime);
            StmtProcessor.sleep(necessaryDelay.intValue());
        }        
        
        final ValidationCode validationCode = getEmailService().getValidateCodeFromMail(getAccount().getEmail(), start);

        getAccount().setValidationCode(validationCode.getValidationCode());
        this.setActionCode(validationCode.getCode());
        this.setActionNote(validationCode.getMessage());
        
        setActionNote(String.format("%d: ActGetValidationCodeFromMail: %s [%s]",
                validationCode.getCode(),
                validationCode.getValidationCode(),
                validationCode.getMessage())
        );
    }
}
