package lk.creativelabs.jobseekers.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lk.creativelabs.jobseekers.dto.ApplicationDTO;
import lk.creativelabs.jobseekers.dto.ClientDTO;
import lk.creativelabs.jobseekers.dto.EmployeeWorksDTO;
import lk.creativelabs.jobseekers.dto.SubmittedWorksDTO;
import lk.creativelabs.jobseekers.dto.utils.Works;
import lk.creativelabs.jobseekers.service.WorkService;
import lk.creativelabs.jobseekers.util.FileServer;
import lk.creativelabs.jobseekers.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("works")
@CrossOrigin
public class WorksController {

         @Autowired
         WorkService workService;


         @PostMapping("create/new/works")
         public ResponseUtil giveWorksToTheRegisteredEmployee(@RequestParam String details,@RequestParam MultipartFile doc) throws Exception {

                         System.out.println("CCCCCCCCCCCCCC");

                         JsonMapper jsonMapper = new JsonMapper();
                         jsonMapper.registerModule(new JavaTimeModule());
                         EmployeeWorksDTO employeeWorksDTO = jsonMapper.readValue(details, EmployeeWorksDTO.class);
                         employeeWorksDTO.setJobId(UUID.randomUUID().toString());

                         String rootFolder = "./assets";

                         String assetsFolderPath = rootFolder;
                         String employeeFolderPath = rootFolder + "/works";

                         FileServer.createDrictory(assetsFolderPath);      // create assets folder if not exists

                         //saved file locations
                         String profilePath = FileServer.createDrictoryAndSaveFile(employeeFolderPath, doc);
                         employeeWorksDTO.getWorkInfo().setDocUrl(profilePath);


             return new ResponseUtil(200," give works ",workService.giveWorkForTheEmployee(employeeWorksDTO));  //workService.giveWorkForTheEmployee(employeeWorksDTO)
         }



         @GetMapping("/get/works")
         public ResponseUtil getWorksForEachEmployee(@RequestHeader String employeeId){
            return new ResponseUtil(200," get Work ",workService.getWorksForWork(employeeId));
         }



         @PostMapping("/submitted/works")
         public ResponseUtil postCompletedWorks(@RequestParam String submitWorks, @RequestParam MultipartFile doc) throws Exception {

             JsonMapper jsonMapper = new JsonMapper();
             jsonMapper.registerModule(new JavaTimeModule());
             SubmittedWorksDTO submittedWorksDTO = jsonMapper.readValue(submitWorks, SubmittedWorksDTO.class);

             String rootFolder = "./assets";

             String assetsFolderPath = rootFolder;
             String employeeFolderPath = rootFolder + "/works/submitted";

             FileServer.createDrictory(assetsFolderPath);      // create assets folder if not exists

             //saved file locations
             String profilePath = FileServer.createDrictoryAndSaveFile(employeeFolderPath, doc);
             System.out.println(profilePath);


             submittedWorksDTO.getWorkInfo().setDocUrl(profilePath);

             System.out.println(submittedWorksDTO.toString());
             return new ResponseUtil(200,"submit success",workService.submittedWorks(submittedWorksDTO));

         }



         @GetMapping("/getSubmittedWorks")
         public ResponseUtil getAllSubmittedWorks(@RequestHeader String clientId){
              return new ResponseUtil(200,"get submitted works", workService.getSubmittedWorks(clientId));
         }


        @PostMapping ("/mark/works")   // mark as read completerd works
        public ResponseUtil worksMarkAsRead(@RequestHeader String jobId,@RequestHeader String status){

             return new ResponseUtil(200,"mark as "+status,workService.markWork(jobId,status));

        }


        @PostMapping ("/mark/works/read")   // mark as read completerd works by client
        public ResponseUtil submittedWorksMarkAsRead(@RequestHeader String jobId){

            return new ResponseUtil(200,"mark as read",workService.markWorkAsRead(jobId));

        }


        //get all accepted
        @PostMapping ("get/all/accept/works")
        public ResponseUtil getAllAcceptWorks(@RequestHeader String jobId){

            return new ResponseUtil(200,"get all accepted",null);

        }


        //get all mark as read
        @PostMapping ("get/all/read/works")
        public ResponseUtil getAllReadWorks(@RequestHeader String jobId){

            return new ResponseUtil(200,"get all reads",null);

        }


}
