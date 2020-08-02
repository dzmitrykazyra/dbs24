package com.kdg.fs24.entity.tariff;

/**
 *
 * @author Козыро Дмитрий
 */
import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Data;

//import com.kdg.fs24.application.core.api.ObjectRoot;
@Data
@Entity
@Table(name = "TariffRates_2")
@IdClass(TariffRate_2PK.class)
public class TariffRate_2 extends TariffRecordAbstract {

    // диапазонный тариф 
    @Id
    private BigDecimal min_sum;
    private BigDecimal max_sum;
    private BigDecimal rateValue;

}
