package lk.creativelabs.jobseekers.service.impl;

import lk.creativelabs.jobseekers.dto.EmployeeWorksDTO;
import lk.creativelabs.jobseekers.dto.SubmittedWorksDTO;
import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import lk.creativelabs.jobseekers.repo.EmployeeWorksRepo;
import lk.creativelabs.jobseekers.repo.RegisteredEmployeeRepo;
import lk.creativelabs.jobseekers.repo.SubmittedWorksRepo;
import lk.creativelabs.jobseekers.service.WorkService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkServiceImpl implements WorkService {

    @Autowired
    RegisteredEmployeeRepo registeredEmployeeRepo;

    @Autowired
    SubmittedWorksRepo submittedWorksRepo;

    @Autowired
    EmployeeWorksRepo  employeeWorksRepo;

    @Autowired
    ModelMapper modalMapper;

    @Override
    public EmployeeWorks giveWorkForTheEmployee(EmployeeWorksDTO employeeWorksDTO) {

        return null;
    }

    @Override
    public SubmittedWorks submittedWorks(SubmittedWorksDTO submittedWorksDTO) {

        return null;
    }

    @Override
    public EmployeeWorks getWorksForEmployees(String employeeId) {

       return null;
    }

}
