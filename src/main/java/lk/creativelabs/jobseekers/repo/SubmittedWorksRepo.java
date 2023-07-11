package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface SubmittedWorksRepo extends MongoRepository<SubmittedWorks, String> ,  CustomSubmittedWorksRepo {
    SubmittedWorks findByJobId(String jobId);

}

interface CustomSubmittedWorksRepo {
    List<SubmittedWorks> findByClientId(String clientId);
}



class CustomSubmittedWorkImpl implements  CustomSubmittedWorksRepo{


    public MongoTemplate mongoTemplate;

    @Override
    public List<SubmittedWorks> findByClientId(String clientId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("workStatus").is("submitted").and("clientId").is(clientId));
        return mongoTemplate.find(query, SubmittedWorks.class);

    }
}