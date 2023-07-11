package lk.creativelabs.jobseekers.entity;


import lk.creativelabs.jobseekers.dto.utils.Works;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Document(collection = "employee_works")
public class EmployeeWorks {

    @Id
    private String id;
    private String clientId;
    private String employeeId;
    private String jobId;
    private Works postWork;
    private LocalDate givenDate;
    private LocalDate deadline;
    private String workStatus; // submitted / pending


}
