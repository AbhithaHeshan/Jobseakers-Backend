package lk.creativelabs.jobseekers.service.impl;

import lk.creativelabs.jobseekers.dto.EmployeeWorksDTO;
import lk.creativelabs.jobseekers.dto.SubmittedWorksDTO;
import lk.creativelabs.jobseekers.dto.utils.EmployeeAndClent;
import lk.creativelabs.jobseekers.dto.utils.Works;
import lk.creativelabs.jobseekers.entity.Client;
import lk.creativelabs.jobseekers.entity.Employee;
import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import lk.creativelabs.jobseekers.repo.*;
import lk.creativelabs.jobseekers.service.WorkService;
import lk.creativelabs.jobseekers.util.Base64Encorder;
import lk.creativelabs.jobseekers.util.UserIdGenerator;
import net.bytebuddy.implementation.bytecode.Throw;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    ClientRepo clientRepo;

    @Autowired
    ModelMapper modalMapper;

    @Override
    public EmployeeWorksDTO giveWorkForTheEmployee(EmployeeWorksDTO employeeWorksDTO) {

       // EmployeeWorks map = modalMapper.map(employeeWorksDTO, EmployeeWorks.class);
        System.out.println("CVVVVVVVVVVVVVVVVVVVVVVVV");
        Employee employeeByUserId = employeeRepo.getEmployeeByUserId(employeeWorksDTO.getEmployeeId()).get();
        Client clientByUserId = clientRepo.getClientByUserId(employeeWorksDTO.getClientId()).get();

        String empId = String.valueOf(employeeByUserId.getEmployeeId());
        String cliId = String.valueOf(clientByUserId.getClientId());

        EmployeeWorks employeeWorks = new EmployeeWorks();
        employeeWorks.setId(UserIdGenerator.generateUserId());
        employeeWorks.setEmployeeId(empId);
        employeeWorks.setClientId(cliId);
        employeeWorks.setGivenDate(employeeWorksDTO.getGivenDate());
        employeeWorks.setDeadline(employeeWorksDTO.getDeadline());
        employeeWorks.setWorkStatus(employeeWorksDTO.getWorkStatus());
        employeeWorks.setJobId(employeeWorksDTO.getJobId());
        employeeWorks.setPostWork(employeeWorksDTO.getWorkInfo());

        EmployeeWorks employeeWorks1 = employeeWorksRepo.save(employeeWorks);

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(employeeByUserId.getEmployeeId());

        Works postWork = employeeWorks1.getPostWork();
        new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl());

        EmployeeWorksDTO resp = new EmployeeWorksDTO(employeeWorks1.getClientId(), employeeWorks1.getEmployeeId(),employeeByEmployeeId.get().getName(), employeeWorks1.getJobId(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl()),employeeWorks1.getGivenDate(),employeeWorks1.getDeadline(), employeeWorks1.getWorkStatus());
        return  resp;
    }

    //submit a work

    @Override
    public SubmittedWorksDTO submittedWorks(SubmittedWorksDTO submittedWorksDTO) {

    //    Employee employeeByUserId = employeeRepo.getEmployeeByUserId(submittedWorksDTO.getEmployeeId()).get();
        Client clientByUserId = clientRepo.getClientByUserId(submittedWorksDTO.getClientId()).orElseThrow();

        Optional<Employee> employeeByUserId1 = employeeRepo.getEmployeeByUserId(submittedWorksDTO.getEmployeeId());
        employeeByUserId1.orElseThrow();

        long employeeId = employeeByUserId1.get().getEmployeeId();
        long clientId = clientByUserId.getClientId();

        EmployeeWorks employeeWorks = employeeWorksRepo.findByJobId(submittedWorksDTO.getJobId());
        employeeWorks.setWorkStatus(submittedWorksDTO.getWorkStatus());
        employeeWorksRepo.save(employeeWorks);

        SubmittedWorks submittedWorks1 = new SubmittedWorks();
        submittedWorks1.setId(UserIdGenerator.generateUserId());
        submittedWorks1.setEmployeeId(String.valueOf(employeeId));
        submittedWorks1.setClientId(String.valueOf(clientId));
        submittedWorks1.setSubmittedDate(submittedWorksDTO.getSubmittedDate());
        submittedWorks1.setWorkStatus(submittedWorksDTO.getWorkStatus());
        submittedWorks1.setJobId(submittedWorksDTO.getJobId());
        submittedWorks1.setCompletedWork(submittedWorksDTO.getWorkInfo());

        SubmittedWorks save = submittedWorksRepo.save(submittedWorks1);

        return modalMapper.map(save,SubmittedWorksDTO.class);

    }

    @Override
    public List<EmployeeWorksDTO> getWorksForWork(String employeeId) {
        Employee employeeByUserId = employeeRepo.getEmployeeByUserId(employeeId).get();
        List<EmployeeWorks> employeeWorks = employeeWorksRepo.findWorks(String.valueOf(employeeByUserId.getEmployeeId()));

        List<EmployeeWorksDTO> data = new ArrayList<>();
        employeeWorks.forEach((empWorks)->{
            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(empWorks.getEmployeeId()));
            Works postWork = empWorks.getPostWork();
            new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),postWork.getDocUrl());
            data.add(new EmployeeWorksDTO(empWorks.getClientId(), empWorks.getEmployeeId(),employeeByEmployeeId.get().getName(), empWorks.getJobId(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(), Base64Encorder.encode(postWork.getDocUrl())),empWorks.getGivenDate(),empWorks.getDeadline(), empWorks.getWorkStatus()));
        });
        return data;

    }

    @Override
    public List<SubmittedWorksDTO> getSubmittedWorks(String clientId) {
        Client clientByUserId = clientRepo.getClientByUserId(clientId).get();
        List<SubmittedWorks> byClientId = submittedWorksRepo.findByClientId(String.valueOf(clientByUserId.getClientId()));
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
        System.out.println("JOBIIDZZZZZZZZZZZZZZZZZZ");
        SubmittedWorks submittedWorks = submittedWorksRepo.findByJobId(jobId);
        submittedWorks.setWorkStatus("Read");

        SubmittedWorks savedData = submittedWorksRepo.save(submittedWorks);

        EmployeeWorks byJobId = employeeWorksRepo.findByJobId(jobId);
        byJobId.setWorkStatus("Read");
        employeeWorksRepo.save(byJobId);

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(savedData.getEmployeeId()));

        return new SubmittedWorksDTO(savedData.getClientId(),savedData.getEmployeeId(),employeeByEmployeeId.get().getName(),new Works( savedData.getCompletedWork().getTitle(), savedData.getCompletedWork().getCategory(), savedData.getCompletedWork().getDescription(), savedData.getCompletedWork().getDocUrl()), savedData.getJobId(), savedData.getSubmittedDate(),savedData.getWorkStatus());
    }

    @Override
    public EmployeeAndClent getDataFiltered(String userId, String catogary, String status) {

        Client clientByUserId = clientRepo.getClientByUserId(userId).get();
        List<EmployeeWorks> allByFilter = null;
        if(catogary.equals("All") || status.equals("All")){
              if(catogary.equals("All") && status.equals("All")){
                  allByFilter   =  employeeWorksRepo.findAllByFilterWhenAll(String.valueOf(clientByUserId.getClientId()));
              }else if (catogary.equals("All")){
                  allByFilter   =  employeeWorksRepo.findAllByFilterWhenCatogaryIsAll(String.valueOf(clientByUserId.getClientId()), status);
              }else if(catogary.equals("All") || status.equals("Submitted")) {

                  System.out.println("XXXXXXXXXXXXXXXXXXX");
                  List<SubmittedWorks> byClientId = submittedWorksRepo.findByClientId(String.valueOf(clientByUserId.getClientId()));
                  List<SubmittedWorksDTO> data = new ArrayList<>();
                  byClientId.forEach((submitted) -> {
                      Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
                      String jobId = submitted.getJobId();

                      EmployeeWorks byJobId = employeeWorksRepo.findByJobId(jobId);
                      System.out.println(byJobId.getPostWork().getDocUrl() + "    sdd");
                      Works postWork = submitted.getCompletedWork();
                      data.add(new SubmittedWorksDTO(submitted.getClientId(), submitted.getEmployeeId(), employeeByEmployeeId.get().getName(), new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), postWork.getDocUrl(), byJobId.getPostWork().getDocUrl()), submitted.getJobId(), submitted.getSubmittedDate(), submitted.getWorkStatus()));

                  });

                  return new EmployeeAndClent<>(data);
              }
              else{
                  allByFilter   =  employeeWorksRepo.findAllByFilterWhenCatogaryIsAll(String.valueOf(clientByUserId.getClientId()), catogary);
              }

        }else if(status.equals("Submitted")){

            System.out.println("XXXXXXXXXXXXXXXXXXX");
            List<SubmittedWorks> byClientId = submittedWorksRepo.findByClientId(String.valueOf(clientByUserId.getClientId()));
            List<SubmittedWorksDTO> data = new ArrayList<>();
            byClientId.forEach((submitted)->{
                Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
                String jobId = submitted.getJobId();

                EmployeeWorks byJobId = employeeWorksRepo.findByJobId(jobId);
                System.out.println(byJobId.getPostWork().getDocUrl() + "    sdd");
                Works postWork = submitted.getCompletedWork();
                data.add(new SubmittedWorksDTO(submitted.getClientId(), submitted.getEmployeeId(),employeeByEmployeeId.get().getName(),new Works(postWork.getTitle(),postWork.getCategory(),postWork.getDescription(),Base64Encorder.encode(postWork.getDocUrl()),Base64Encorder.encode(byJobId.getPostWork().getDocUrl())), submitted.getJobId(),submitted.getSubmittedDate(), submitted.getWorkStatus()));

            });

            return new EmployeeAndClent<>(data);

        }else{
            allByFilter   = employeeWorksRepo.findAllByFilter(String.valueOf(clientByUserId.getClientId()), catogary, status);
        }
        System.out.println("XXXXXXXXCCCCCCCCCCCCCCCXXXXXXXXXXX");
        List<EmployeeWorksDTO> employeeWorksDTOS = new ArrayList<>();

        allByFilter.forEach((submitted)->{
            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
            employeeWorksDTOS.add(new EmployeeWorksDTO(submitted.getClientId(), submitted.getEmployeeId(),employeeByEmployeeId.get().getName(),submitted.getJobId(),new Works(submitted.getPostWork().getTitle(),submitted.getPostWork().getCategory(),submitted.getPostWork().getDescription(),Base64Encorder.encode(submitted.getPostWork().getDocUrl())), submitted.getGivenDate(),submitted.getDeadline(), submitted.getWorkStatus()));
        });

        return new EmployeeAndClent<>(employeeWorksDTOS);
    }


}
