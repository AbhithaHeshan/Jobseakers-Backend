package lk.creativelabs.jobseekers.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lk.creativelabs.jobseekers.dto.utils.Works;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmittedWorksDTO {

    private String clientId;
    private String employeeId;
    private String employeeName;
    private Works workInfo;
    private String jobId;
    private LocalDate submittedDate;
    private String workStatus;

}
