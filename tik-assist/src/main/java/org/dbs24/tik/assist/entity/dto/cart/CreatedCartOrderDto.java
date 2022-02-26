package org.dbs24.tik.assist.entity.dto.cart;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CreatedCartOrderDto {

    private Map<String, List<Integer>> entityTitleToCreatedIds;

    public static CreatedCartOrderDtoBuilder builder() {

        return new CreatedCartOrderDtoBuilder();
    }
}

