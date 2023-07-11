package lk.creativelabs.jobseekers.service.impl;

import lk.creativelabs.jobseekers.dto.EmployeeWorksDTO;
import lk.creativelabs.jobseekers.dto.SubmittedWorksDTO;
import lk.creativelabs.jobseekers.dto.utils.Works;
import lk.creativelabs.jobseekers.entity.Employee;
import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import lk.creativelabs.jobseekers.repo.EmployeeRepo;
import lk.creativelabs.jobseekers.repo.EmployeeWorksRepo;
import lk.creativelabs.jobseekers.repo.RegisteredEmployeeRepo;
import lk.creativelabs.jobseekers.repo.SubmittedWorksRepo;
import lk.creativelabs.jobseekers.service.WorkService;
import lk.creativelabs.jobseekers.util.UserIdGenerator;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkServiceImpl implements WorkService {

    @Autowired
    RegisteredEmployeeRepo registeredEmployeeRepo;

    @Autowired
    SubmittedWorksRepo submittedWorksRepo;

    @Autowired
    EmployeeWorksRepo  employeeWorksRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    ModelMapper modalMapper;

    @Override
    public EmployeeWorksDTO giveWorkForTheEmployee(EmployeeWorksDTO employeeWorksDTO) {

       // EmployeeWorks map = modalMapper.map(employeeWorksDTO, EmployeeWorks.class);

        System.out.println(employeeWorksDTO.toString());

        EmployeeWorks employeeWorks = new EmployeeWorks();
        employeeWorks.setId(UserIdGenerator.generateUserId());
        employeeWorks.setEmployeeId(employeeWorksDTO.getEmployeeId());
        employeeWorks.setClientId(employeeWorksDTO.getClientId());
        employeeWorks.setGivenDate(employeeWorksDTO.getGivenDate());
        employeeWorks.setDeadline(employeeWorksDTO.getDeadline());
        employeeWorks.setWorkStatus(employeeWorksDTO.getWorkStatus());
        employeeWorks.setJobId(employeeWorksDTO.getJobId());
        employeeWorks.setPostWork(employeeWorksDTO.getWorkInfo());

        EmployeeWorks employeeWorks1 = employeeWorksRepo.save(employeeWorks);

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(employeeWorksDTO.getEmployeeId()));

        Works postWork = employeeWorks1.getPostWork();
        new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl());

        EmployeeWorksDTO resp = new EmployeeWorksDTO(employeeWorks1.getClientId(), employeeWorks1.getEmployeeId(),employeeByEmployeeId.get().getName(), employeeWorks1.getJobId(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl()),employeeWorks1.getGivenDate(),employeeWorks1.getDeadline(), employeeWorks1.getWorkStatus());
        return  resp;
    }

    //submit a work

    @Override
    public SubmittedWorksDTO submittedWorks(SubmittedWorksDTO submittedWorksDTO) {

        EmployeeWorks employeeWorks = employeeWorksRepo.findByJobId(submittedWorksDTO.getJobId());
        employeeWorks.setWorkStatus(submittedWorksDTO.getWorkStatus());
        employeeWorksRepo.save(employeeWorks);

        SubmittedWorks submittedWorks1 = new SubmittedWorks();
        submittedWorks1.setId(UserIdGenerator.generateUserId());
        submittedWorks1.setEmployeeId(submittedWorksDTO.getEmployeeId());
        submittedWorks1.setClientId(submittedWorksDTO.getClientId());
        submittedWorks1.setSubmittedDate(submittedWorksDTO.getSubmittedDate());
        submittedWorks1.setWorkStatus(submittedWorksDTO.getWorkStatus());
        submittedWorks1.setJobId(submittedWorksDTO.getJobId());
        submittedWorks1.setCompletedWork(submittedWorksDTO.getWorkInfo());

        SubmittedWorks save = submittedWorksRepo.save(submittedWorks1);

        return modalMapper.map(save,SubmittedWorksDTO.class);

    }

    @Override
    public List<EmployeeWorksDTO> getWorksForWork(String employeeId) {

        List<EmployeeWorks> employeeWorks = employeeWorksRepo.findWorks(employeeId);



        List<EmployeeWorksDTO> data = new ArrayList<>();
        employeeWorks.forEach((empWorks)->{
            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(empWorks.getEmployeeId()));
            Works postWork = empWorks.getPostWork();
            new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl());
            data.add(new EmployeeWorksDTO(empWorks.getClientId(), empWorks.getEmployeeId(),employeeByEmployeeId.get().getName(), empWorks.getJobId(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl()),empWorks.getGivenDate(),empWorks.getDeadline(), empWorks.getWorkStatus()));
        });
        return data;

    }

    @Override
    public List<SubmittedWorksDTO> getSubmittedWorks(String clientId) {

        List<SubmittedWorks> byClientId = submittedWorksRepo.findByClientId(clientId);
        List<SubmittedWorksDTO> data = new ArrayList<>();

        byClientId.forEach((submitted)->{
            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
            Works postWork = submitted.getCompletedWork();
            new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl());
            data.add(new SubmittedWorksDTO(submitted.getClientId(), submitted.getEmployeeId(),employeeByEmployeeId.get().getName(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl()), submitted.getJobId(),submitted.getSubmittedDate(), submitted.getWorkStatus()));

        });

        return data;
    }

    @Override
    public EmployeeWorksDTO markWork(String jobId,String status) {

        EmployeeWorks employeeWorks = employeeWorksRepo.findByJobId(jobId);
        employeeWorks.setWorkStatus(status);
        EmployeeWorks submitted = employeeWorksRepo.save(employeeWorks);


        Works postWork = submitted.getPostWork();

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));

        return new EmployeeWorksDTO(submitted.getClientId(), submitted.getEmployeeId(),employeeByEmployeeId.get().getName(),submitted.getJobId(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl()), submitted.getGivenDate(),submitted.getDeadline(), submitted.getWorkStatus());

    }

    @Override
    public SubmittedWorksDTO markWorkAsRead(String jobId) {
        SubmittedWorks submittedWorks = submittedWorksRepo.findByJobId(jobId);
        submittedWorks.setWorkStatus("Read");

        SubmittedWorks savedData = submittedWorksRepo.save(submittedWorks);

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(savedData.getEmployeeId()));

        return new SubmittedWorksDTO(savedData.getClientId(),savedData.getEmployeeId(),employeeByEmployeeId.get().getName(),new Works( savedData.getCompletedWork().getTitle(), savedData.getCompletedWork().getCategory(), savedData.getCompletedWork().getDescription(), savedData.getCompletedWork().getDocUrl()), savedData.getJobId(), savedData.getSubmittedDate(),savedData.getWorkStatus());
    }


}
