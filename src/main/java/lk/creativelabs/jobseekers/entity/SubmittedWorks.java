package lk.creativelabs.jobseekers.entity;


import lk.creativelabs.jobseekers.dto.utils.Works;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "submitted_works")
public class SubmittedWorks {

    @Id
    private long id;
    private String clientId;
    private String employeeId;
    private Works completedWork;
    private LocalDate submittedDate;

}
