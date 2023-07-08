package lk.creativelabs.jobseekers.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lk.creativelabs.jobseekers.dto.ApplicationDTO;
import lk.creativelabs.jobseekers.dto.EmployeeDTO;
import lk.creativelabs.jobseekers.service.ApplicationService;
import lk.creativelabs.jobseekers.util.FileServer;
import lk.creativelabs.jobseekers.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;


@RestController
@RequestMapping("jobApplication")
public class JobApplicationController {

    @Autowired
    ApplicationService applicationService;

     @PostMapping("/create/new")
     public ResponseUtil createNewApplication(@RequestParam String application, @RequestHeader String clientUserId, @RequestHeader String employeeUserId, @RequestParam MultipartFile cv) throws Exception {
         JsonMapper jsonMapper = new JsonMapper();
         jsonMapper.registerModule(new JavaTimeModule());
         ApplicationDTO applicationDto = jsonMapper.readValue(application, ApplicationDTO.class);

         String rootFolder = "./assets";
         String assetsFolderPath = rootFolder;
         String applicationFolderPath = rootFolder + "/applications";
         FileServer.createDrictory(assetsFolderPath);

         //saved file locations
         String profilePath = FileServer.createDrictoryAndSaveFile(applicationFolderPath, cv);
         applicationDto.setCvUri(profilePath);
         applicationDto.setUserId(employeeUserId);

         return new ResponseUtil(200,"appllication save success",applicationService.createNewApplication(clientUserId,employeeUserId,applicationDto));
     }

    @PostMapping("/update/status")
    public ResponseUtil updateApplicationStatus(@RequestHeader String applicationId , @RequestBody Map<String, String> data){

         return  new ResponseUtil(200,"status update",applicationService.updateApplicationApproval(applicationId,data.get("status")));

    }

    @GetMapping("/get/byId")
    public ResponseUtil getApplication(@RequestHeader String userId){//clint user id
          return new ResponseUtil(200,"get user data",applicationService.getAllOfEachClient(userId));
    }

    @PostMapping("/approval/status")
    public ResponseUtil updateApprovalStatus(){
       return null;
    }


    @PostMapping("/getAll")
    public void getAllApplications(){

    }

}
