package lk.creativelabs.jobseekers.service;

import lk.creativelabs.jobseekers.dto.EmployeeWorksDTO;
import lk.creativelabs.jobseekers.dto.SubmittedWorksDTO;
import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;

public interface WorkService {


        EmployeeWorks giveWorkForTheEmployee(EmployeeWorksDTO employeeWorksDTO);


        SubmittedWorks submittedWorks(SubmittedWorksDTO submittedWorksDTO);


        EmployeeWorks getWorksForEmployees(String employeeId);



}
