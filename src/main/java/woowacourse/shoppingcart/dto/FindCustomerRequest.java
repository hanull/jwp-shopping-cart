package woowacourse.shoppingcart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class FindCustomerRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    private final String name;

    public FindCustomerRequest(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name == null) {
            throw new NullPointerException("payload 가 존재하지 않습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
