package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "tm_hearts_price")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeartPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tm_hearts_price")
    @SequenceGenerator(name = "seq_tm_hearts_price", sequenceName = "seq_tm_hearts_price", allocationSize = 1)
    @NotNull
    @Column(name = "hearts_price_id")
    private Integer heartsPriceId;

    @NotNull
    @Column(name = "hearts_amount")
    private Integer heartsAmount;

    @NotNull
    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_iso")
    @NotNull
    private Currency currency;

    @NotNull
    @Column(name = "hearts_additional")
    private Integer heartAdditional;
}
