package sales.application.sales.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SlipDto {
    int id = 0;
    String name;
    Boolean isArchived;
}
