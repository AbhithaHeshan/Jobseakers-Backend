package lk.creativelabs.jobseekers.dto;

import lk.creativelabs.jobseekers.dto.utils.Works;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmployeeWorksDTO {

        private String clientId;
        private String employeeId;
        private Works postWork;
        private LocalDate givenDate;

}
