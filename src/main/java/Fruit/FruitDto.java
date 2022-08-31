package Fruit;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
public class FruitDto {
    private Long id;
    @NotNull
    @NotEmpty(message = "Fruit name may not to be empty")
    private String name;
}
