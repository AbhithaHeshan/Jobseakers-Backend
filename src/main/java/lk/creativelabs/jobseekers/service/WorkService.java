package lk.creativelabs.jobseekers.service;

import lk.creativelabs.jobseekers.dto.EmployeeWorksDTO;
import lk.creativelabs.jobseekers.dto.SubmittedWorksDTO;
import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;

import java.util.List;

public interface WorkService {


        EmployeeWorksDTO giveWorkForTheEmployee(EmployeeWorksDTO employeeWorksDTO);


        SubmittedWorksDTO submittedWorks(SubmittedWorksDTO submittedWorksDTO);


        List<EmployeeWorksDTO> getWorksForWork(String employeeId);


        List<SubmittedWorksDTO> getSubmittedWorks(String clientId);


        EmployeeWorksDTO markWork(String jobId,String status);

        SubmittedWorksDTO markWorkAsRead(String jobId);



}