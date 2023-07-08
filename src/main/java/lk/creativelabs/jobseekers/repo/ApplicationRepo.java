package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepo extends JpaRepository<Application,String> {
       // get applications
       // get application by job id
       // get application by client id
       // get application by employee Id
       // create application

      List<Application> getApplicationByClient_ClientId(long clientId);

}
