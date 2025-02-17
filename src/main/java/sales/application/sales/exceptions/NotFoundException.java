package sales.application.sales.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotFoundException extends RuntimeException{
    String message;

    public NotFoundException(String message) {
        super(message);
        this.message= message;
    }

}
