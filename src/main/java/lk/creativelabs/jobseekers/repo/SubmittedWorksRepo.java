package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SubmittedWorksRepo extends MongoRepository<SubmittedWorks,String> {

}
