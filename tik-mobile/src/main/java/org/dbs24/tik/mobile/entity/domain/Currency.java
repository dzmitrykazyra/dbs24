package org.dbs24.tik.mobile.entity.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "tm_currencies_ref")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    @Id
    @NotNull
    @Column(name = "currency_iso", updatable = false)
    private String currencyIso;

    @NotNull
    @Column(name = "currency_id")
    private String currencyId;

    @NotNull
    @Column(name = "currency_name")
    private String currencyName;
}
