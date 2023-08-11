package lk.creativelabs.jobseekers.service.impl;

import lk.creativelabs.jobseekers.dto.ApplicationDTO;
import lk.creativelabs.jobseekers.entity.Application;
import lk.creativelabs.jobseekers.entity.Client;
import lk.creativelabs.jobseekers.entity.Employee;
import lk.creativelabs.jobseekers.entity.RegisteredEmployee;
import lk.creativelabs.jobseekers.entity.enums.ApprovalStatus;
import lk.creativelabs.jobseekers.repo.ApplicationRepo;
import lk.creativelabs.jobseekers.repo.ClientRepo;
import lk.creativelabs.jobseekers.repo.EmployeeRepo;
import lk.creativelabs.jobseekers.repo.RegisteredEmployeeRepo;
import lk.creativelabs.jobseekers.service.ApplicationService;
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

@Transactional
@Service
public class ApplicationStatusImpl implements ApplicationService {

    @Autowired
    ApplicationRepo applicationRepo;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    EmployeeRepo employeeRepo;



    @Autowired
    ModelMapper modalMapper;

    @Autowired
    RegisteredEmployeeRepo registeredEmployeeRepo;


    @Override
    public ApplicationDTO createNewApplication(String clientId,String employeeId, ApplicationDTO applicationDTO) {

        Optional<Client> client = clientRepo.getClientByUserId(clientId);
        Optional<Employee> employeeByUserId = employeeRepo.getEmployeeByUserId(employeeId);


        Application application = new Application();
        application.setApplicationId(UserIdGenerator.generateUserId());
        application.setClient(client.get());
        application.setEmployee(employeeByUserId.get());
        application.setName(applicationDTO.getName());
        application.setAddress(applicationDTO.getAddress());
        application.setEmail(applicationDTO.getEmail());
        application.setCvUri(applicationDTO.getCvUri());
        application.setDateOfBirth(applicationDTO.getDateOfBirth());
        application.setJobCatogary(applicationDTO.getJobCatogary());
        application.setJobRoleType(applicationDTO.getJobRoleType());
        application.setTelOne(applicationDTO.getTelOne());
        application.setUserId(applicationDTO.getUserId());
        application.setAdditionalQualifications(applicationDTO.getAdditionalQualifications());
        application.setWorkingType(applicationDTO.getWorkingType());
        application.setApprovalStatus("New");

        applicationRepo.save(application);
        return  modalMapper.map( modalMapper.map(applicationRepo.save(application),Application.class),ApplicationDTO.class);

    }

    @Override
    public ArrayList<ApplicationDTO> getAllOfEachClient(String clientId) {

        Client clientByUserId = clientRepo.getClientByUserId(clientId).get();
        List<Application> applicationList = applicationRepo.getApplicationByClient_ClientId(clientByUserId.getClientId());
        TypeToken<ArrayList<ApplicationDTO>> typeToken = new TypeToken<ArrayList<ApplicationDTO>>() {};
        return modalMapper.map(applicationList,typeToken.getType());

    }

    @Override
    public ApplicationDTO updateApplicationApproval(String applicationId, String status) {
        if(status.equals("Registered")) {
            Application application = applicationRepo.findById(applicationId).get();
            application.setApprovalStatus(status);

            RegisteredEmployee registeredEmployee = new RegisteredEmployee();

            long clientId = application.getClient().getClientId();
            long employeeId = application.getEmployee().getEmployeeId();

            Optional<Client> clientDetails = clientRepo.getClientByClientId(clientId);
            Optional<Employee> employeeDetails = employeeRepo.getEmployeeByEmployeeId(employeeId);

            registeredEmployee.setEmployee(employeeDetails.get());
            registeredEmployee.setClient(clientDetails.get());
            registeredEmployee.setRegistrationStatus(status);

            registeredEmployeeRepo.save(registeredEmployee);

            return modalMapper.map(applicationRepo.save(application), ApplicationDTO.class);
        }

        return null;
    }

    @Override
    public ArrayList<ApplicationDTO> getAllOfEachClientFilterBy(String clientId, String jobType, String jobRoleType,String status){

        System.out.println(clientId + " " + jobType + " "+ jobRoleType + " "+ status);
        Client clientByUserId = clientRepo.getClientByUserId(clientId).get();
        List<Application> details ;
         if(!status.equals("null")){
             details = applicationRepo.getApplicationByClient_ClientIdAndJobCatogaryAndJobRoleTypeAndApprovalStatus(clientByUserId.getClientId(), jobType, jobRoleType,status);
             System.out.println("VVVVVVVVVVVVVVVVVV" + clientByUserId.getClientId());

             System.out.println(details.size());

         }else{
             details = applicationRepo.getApplicationByClient_ClientIdAndJobCatogaryAndJobRoleTypeOrApprovalStatus(clientByUserId.getClientId(), jobType, jobRoleType,status);
         }

        for (Application s: details
             ) {
            System.out.println(s.getApprovalStatus()+ " ffff ");
        }

        return details.stream().map(application -> modalMapper.map(application, ApplicationDTO.class)).collect(Collectors.toCollection(ArrayList::new));
    }


}
