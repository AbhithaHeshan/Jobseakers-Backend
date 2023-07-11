package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface EmployeeWorksRepo extends MongoRepository<EmployeeWorks,String> , CustomEmployeeWorksRepo{

              EmployeeWorks findByJobId(String jobId);
              List<EmployeeWorks> findByWorkStatusAndEmployeeId(String workStatus, String employeeId);

}


interface CustomEmployeeWorksRepo{
       List<EmployeeWorks> findWorks(String employeeId);
}




class EmployeeWorksRepoImpl implements CustomEmployeeWorksRepo{

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    public List<EmployeeWorks> findWorks(String employeeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("workStatus").is("pending").and("employeeId").is(employeeId));
        return mongoTemplate.find(query, EmployeeWorks.class);

    }
}
