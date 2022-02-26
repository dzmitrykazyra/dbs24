package org.dbs24.tik.assist.entity.dto.proportion;

import lombok.Data;
import org.dbs24.stmt.StmtProcessor;
import org.dbs24.tik.assist.entity.domain.DiscountToAccountsQuantity;

@Data
public class AccountsProportionDto {

    private Integer discountId;
    private Integer upToAccountsQuantity;
    private Integer discount;

    public static AccountsProportionDto toAccountsProportionDto(DiscountToAccountsQuantity discountToAccountsQuantity) {

        return StmtProcessor.create(
                AccountsProportionDto.class,
                accountsProportionDto -> {
                    accountsProportionDto.setDiscountId(discountToAccountsQuantity.getDiscountId());
                    accountsProportionDto.setUpToAccountsQuantity(discountToAccountsQuantity.getUpToAccountsQuantity());
                    accountsProportionDto.setDiscount(discountToAccountsQuantity.getDiscount());
                }
        );
    }

    public static DiscountToAccountsQuantity toDiscountToAccountsQuantity(AccountsProportionDto accountsProportionDto) {

        return DiscountToAccountsQuantity.builder()
                .discountId(accountsProportionDto.getDiscountId())
                .upToAccountsQuantity(accountsProportionDto.getUpToAccountsQuantity())
                .discount(accountsProportionDto.getDiscount())
                .build();
    }
}
