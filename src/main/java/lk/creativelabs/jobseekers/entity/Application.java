package lk.creativelabs.jobseekers.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Application {
    @Id
    String applicationId;
    String name;
    String address;
    String dateOfBirth;
    String email;
    String telOne;
    String telTwo;
    String workingType;
    String cvUri;
    String additionalQualifications;
    String userId;
    String jobCatogary;
    String approvalStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    Employee employee;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    Client client;


}
