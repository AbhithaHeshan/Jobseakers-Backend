package lk.creativelabs.jobseekers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.creativelabs.jobseekers.dto.utils.Works;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeWorksDTO {

        private String clientId;
        private String employeeId;
        private String employeeName;
        private String jobId;           // jobId is identify unique job
        private Works workInfo;
        private LocalDate givenDate;
        private LocalDate deadline;
        private String workStatus;

}
