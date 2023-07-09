package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmployeeWorksRepo extends MongoRepository<EmployeeWorks,String> {

}
