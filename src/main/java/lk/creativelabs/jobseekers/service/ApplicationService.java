package lk.creativelabs.jobseekers.service;

import lk.creativelabs.jobseekers.dto.ApplicationDTO;
import lk.creativelabs.jobseekers.dto.ApprovalDTO;

import java.util.ArrayList;
import java.util.List;

public interface ApplicationService {

    ApplicationDTO createNewApplication(String clientId,String employeeId, ApplicationDTO applicationDTO);

    ArrayList<ApplicationDTO> getAllOfEachClient(String clientId);
    ApplicationDTO updateApplicationApproval(String applicationId, String status);


}
