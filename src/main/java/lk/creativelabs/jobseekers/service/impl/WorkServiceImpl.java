package lk.creativelabs.jobseekers.service.impl;

import lk.creativelabs.jobseekers.dto.ClientCommenDTO;
import lk.creativelabs.jobseekers.dto.ClientDTO;
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
import lk.creativelabs.jobseekers.util.FileServer;
import lk.creativelabs.jobseekers.util.UserIdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class WorkServiceImpl implements WorkService {

    @Autowired
    RegisteredEmployeeRepo registeredEmployeeRepo;

    @Autowired
    SubmittedWorksRepo savedData;

    @Autowired
    EmployeeWorksRepo employeeWorksRepo;

    @Autowired
    EmployeeRepo employeeRepo;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    ModelMapper modalMapper;

    @Autowired
    FileServer fileServer;

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
        new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), postWork.getDocUrl());

        EmployeeWorksDTO resp = new EmployeeWorksDTO(employeeWorks1.getClientId(), employeeWorks1.getEmployeeId(), employeeByEmployeeId.get().getName(), employeeWorks1.getJobId(), new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), postWork.getDocUrl()), employeeWorks1.getGivenDate(), employeeWorks1.getDeadline(), employeeWorks1.getWorkStatus());
        return resp;
    }

    //submit a work

    @Override
    public EmployeeWorksDTO submittedWorks(EmployeeWorksDTO submittedWorksDTO) {

        //    Employee employeeByUserId = employeeRepo.getEmployeeByUserId(submittedWorksDTO.getEmployeeId()).get();
        Client clientByUserId = clientRepo.getClientByClientId(Long.parseLong(submittedWorksDTO.getClientId())).orElseThrow();

        Optional<Employee> employeeByUserId1 = employeeRepo.getEmployeeByUserId(submittedWorksDTO.getEmployeeId());
        employeeByUserId1.orElseThrow();

        long employeeId = employeeByUserId1.get().getEmployeeId();
        long clientId = clientByUserId.getClientId();

        EmployeeWorks employeeWorks = employeeWorksRepo.findByJobId(submittedWorksDTO.getJobId());
        employeeWorks.setWorkStatus(submittedWorksDTO.getWorkStatus());
        employeeWorks.setSubmittedDate(submittedWorksDTO.getSubmittedDate());
        employeeWorks.getPostWork().setDocUrl2(submittedWorksDTO.getWorkInfo().getDocUrl2());

        EmployeeWorks save = employeeWorksRepo.save(employeeWorks);

        return modalMapper.map(save, EmployeeWorksDTO.class);

    }

    @Override
    public List<EmployeeWorksDTO> getWorksForWork(String employeeId,String clientId) {
        Employee employeeByUserId = employeeRepo.getEmployeeByUserId(employeeId).get();
        System.out.println(employeeByUserId.getEmployeeId() + " " + clientId);
        List<EmployeeWorks> employeeWorks = employeeWorksRepo.findWorks(String.valueOf(employeeByUserId.getEmployeeId()),clientId);

        List<EmployeeWorksDTO> data = new ArrayList<>();
        employeeWorks.forEach((empWorks) -> {
            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(empWorks.getEmployeeId()));
            Works postWork = empWorks.getPostWork();
            String cvUri =  fileServer.createFileLink(fileServer.uriToFileName(postWork.getDocUrl()),"works");
            data.add(new EmployeeWorksDTO(empWorks.getClientId(), empWorks.getEmployeeId(), employeeByEmployeeId.get().getName(), empWorks.getJobId(), new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), cvUri,""),empWorks.getGivenDate(),empWorks.getDeadline(),empWorks.getWorkStatus()));

        });
        return data;
    }

    @Override
    public List<ClientCommenDTO> getClientsForEachEmp(String employeeUserId) {
        System.out.println(employeeUserId + " vvvvvvvvvvv bbbb");
        Optional<Employee> employee = employeeRepo.getEmployeeByUserId(employeeUserId);



        List<ClientCommenDTO> clientsByIds = new ArrayList<>();
        if(employee.isPresent()){
            List<Long> allClientIds = employeeWorksRepo.getAllClientIdsByUsingEmployeeId(String.valueOf(employee.get().getEmployeeId()));

            allClientIds.forEach((clientId)->{
                Optional<Client> clientByClientId = clientRepo.getClientByClientId(clientId);
                if(clientByClientId.isPresent()){
                    Client client = clientByClientId.get();
                    String cvUri =  fileServer.createFileLink(fileServer.uriToFileName(client.getProfileImageUri()),"client");
                    clientsByIds.add(new ClientCommenDTO(client.getClientId(),client.getOwner(),client.getAddress(),client.getBusinessName(),client.getBusinessType(),client.getEmail(),client.getTel(),cvUri));
                }
            });

        }

        return clientsByIds;
    }


    @Override
    public List<SubmittedWorksDTO> getSubmittedWorks(String clientId) {

        Client clientByUserId = clientRepo.getClientByUserId(clientId).get();
        List<SubmittedWorks> byClientId = savedData.getSubmittedAllWorks(String.valueOf(clientByUserId.getClientId()), "Submitted");
        // List<SubmittedWorks> byClientId = new CustomSubmittedWorkImpl().getSubmittedAllWorks(String.valueOf(clientByUserId.getClientId()), "Submitted");
        List<SubmittedWorksDTO> data = new ArrayList<>();

        byClientId.forEach((submitted) -> {

            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
            Works postWork = submitted.getCompletedWork();
            new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), postWork.getDocUrl(), postWork.getDocUrl2());
            String cvUri =  fileServer.createFileLink(fileServer.uriToFileName(postWork.getDocUrl()),"works");
            String cvUri2 =  fileServer.createFileLink(fileServer.uriToFileName(postWork.getDocUrl2()),"works/submitted");
            System.out.println();
            System.out.println();
            System.out.println(cvUri + " " + cvUri2);
            System.out.println();
            System.out.println();
            data.add(new SubmittedWorksDTO(submitted.getClientId(), submitted.getEmployeeId(), employeeByEmployeeId.get().getName(), new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), cvUri, cvUri2), submitted.getJobId(), submitted.getSubmittedDate(), submitted.getWorkStatus()));

        });

        return data;

    }

    @Override
    public EmployeeWorksDTO markWork(String jobId, String status) {

        EmployeeWorks employeeWorks = employeeWorksRepo.findByJobId(jobId);
        employeeWorks.setWorkStatus(status);
        EmployeeWorks submitted = employeeWorksRepo.save(employeeWorks);


        Works postWork = submitted.getPostWork();

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
        String cvUri =  fileServer.createFileLink(fileServer.uriToFileName(postWork.getDocUrl()),"works");
        String cvUri2 =  fileServer.createFileLink(fileServer.uriToFileName(postWork.getDocUrl2()),"works/submitted");
        return new EmployeeWorksDTO(submitted.getClientId(), submitted.getEmployeeId(), employeeByEmployeeId.get().getName(), submitted.getJobId(), new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), cvUri, cvUri2), submitted.getGivenDate(), submitted.getDeadline(), submitted.getWorkStatus(), submitted.getSubmittedDate());

    }

    @Override
    public EmployeeWorksDTO markWorkAsRead(String jobId) {
        System.out.println("JOBIIDZZZZZZZZZZZZZZZZZZ");

        EmployeeWorks savedData = employeeWorksRepo.findByJobId(jobId);
        savedData.setWorkStatus("Read");
        employeeWorksRepo.save(savedData);

        Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(savedData.getEmployeeId()));
        return new EmployeeWorksDTO(savedData.getClientId(), savedData.getEmployeeId(), employeeByEmployeeId.get().getName(), savedData.getJobId(), new Works(savedData.getPostWork().getTitle(), savedData.getPostWork().getCategory(), savedData.getPostWork().getDescription(), Base64Encorder.encode(savedData.getPostWork().getDocUrl()), Base64Encorder.encode(savedData.getPostWork().getDocUrl2())), savedData.getGivenDate(), savedData.getDeadline(), savedData.getWorkStatus(), savedData.getSubmittedDate());
    }

    @Override
    public EmployeeAndClent getDataFiltered(String userId, String catogary, String status) {

        Optional<Client> clientByUserId = clientRepo.getClientByUserId(userId);

        String clientid = "";
        if (clientByUserId.isPresent()) {
            clientid = String.valueOf(clientByUserId.get().getClientId());
        }


        List<EmployeeWorks> allByFilter = null;
        if (catogary.equals("All") && status.equals("All")) {

            allByFilter = employeeWorksRepo.findAllByFilterWhenAll(clientid);

        } else if (!catogary.equals("All") && !status.equals("All")){

             System.out.println("RRRRRRRRRRRRR");

             allByFilter = employeeWorksRepo.getAll(clientid,status,catogary);


        } else if (catogary.equals("All")) {

            System.out.println("WWWWWWWWWWWWWWWW");

            allByFilter = employeeWorksRepo.findAllByFilterWhenStatusIsAny(clientid, status);
//            List<EmployeeWorks> byClientId = employeeWorksRepo.getSubmittedAllWorks(clientid, "Submitted");
//            // List<SubmittedWorks> byClientId = new CustomSubmittedWorkImpl().getSubmittedAllWorks(clientid, "Submitted");
//            List<EmployeeWorksDTO> data = new ArrayList<>();
//            byClientId.forEach((submitted) -> {
//
//                Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));
//
//                String jobId = submitted.getJobId();
//                EmployeeWorks byJobId = employeeWorksRepo.findByJobId(jobId);
//                System.out.println(submitted.toString() + "    sdd ");
//                Works postWork = submitted.getPostWork();
//                data.add(new EmployeeWorksDTO(submitted.getClientId(), submitted.getEmployeeId(), employeeByEmployeeId.get().getName(), submitted.getJobId(), new Works(postWork.getTitle(), postWork.getCategory(), postWork.getDescription(), Base64Encorder.encode(postWork.getDocUrl()), Base64Encorder.encode(postWork.getDocUrl2())), submitted.getGivenDate(), submitted.getDeadline(), submitted.getWorkStatus(), submitted.getSubmittedDate()));
//
//            });
//
//            return new EmployeeAndClent<>(data);




        } else if(status.equals("All") ){
            System.out.println("XXXXXXXXXXXXXXXXXX");
            allByFilter = employeeWorksRepo.findAllByFilterWhenCatogaryIsAny(clientid, catogary);
        }

//        } else if ( !status.equals("Submitted") && catogary.equals("All") ) {
//            System.out.println("ssssssssssssssAAA");
//            allByFilter = employeeWorksRepo.findAllByFilterWhenStatusIsAll(clientid, status);
//        }


//        } else if (status.equals("Submitted") && !catogary.equals("All")) {
//            System.out.println("ssssssssssssssBBB");
//            allByFilter = employeeWorksRepo.findAllByFilterWhenCatogaryIsAll(clientid, catogary,"Submitted");
//
//        } else {
//            System.out.println(">>>>>>>>>>>>>>>>>");
//            allByFilter = employeeWorksRepo.findAllByFilter(clientid, catogary, status);
//        }

        System.out.println("XXXXXXXXCCCCCCCCCCCCCCCXXXXXXXXXXX");
        List<EmployeeWorksDTO> employeeWorksDTOS = new ArrayList<>();

        allByFilter.forEach((submitted) -> {

            Optional<Employee> employeeByEmployeeId = employeeRepo.getEmployeeByEmployeeId(Long.parseLong(submitted.getEmployeeId()));

            System.out.println(submitted.getPostWork().getDocUrl2() == null);


            String docUrl;
            String docUrl2;
            if (submitted.getPostWork().getDocUrl() != null) {
                docUrl = fileServer.createFileLink(fileServer.uriToFileName(submitted.getPostWork().getDocUrl()),"works");;
            } else {
                docUrl = "";
            }


            if (submitted.getPostWork().getDocUrl2() != null) {
                docUrl2 = fileServer.createFileLink(fileServer.uriToFileName(submitted.getPostWork().getDocUrl2()),"works/submitted");

            } else {
                docUrl2 = "";
            }

            Works works = new Works(submitted.getPostWork().getTitle(), submitted.getPostWork().getCategory(), submitted.getPostWork().getDescription(), docUrl, docUrl2);

            employeeWorksDTOS.add(new EmployeeWorksDTO(submitted.getClientId(), submitted.getEmployeeId(), employeeByEmployeeId.get().getName(), submitted.getJobId(), works, submitted.getGivenDate(), submitted.getDeadline(), submitted.getWorkStatus(), submitted.getSubmittedDate()));


        });

        return new EmployeeAndClent<>(employeeWorksDTOS);

    }


}
