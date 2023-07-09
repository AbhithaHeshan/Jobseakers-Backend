package lk.creativelabs.jobseekers.dto;

import lk.creativelabs.jobseekers.dto.utils.Works;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SubmittedWorksDTO {

    private String clientId;
    private String employeeId;
    private Works postWork;
    private LocalDate submittedDate;

}
