package lk.creativelabs.jobseekers.repo;

import lk.creativelabs.jobseekers.entity.EmployeeWorks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

public interface EmployeeWorksRepo extends MongoRepository<EmployeeWorks,String> , CustomEmployeeWorksRepo{

              EmployeeWorks findByJobId(String jobId);
              List<EmployeeWorks> findByWorkStatusAndEmployeeId(String workStatus, String employeeId);

              @org.springframework.data.mongodb.repository.Query(value = "{'clientId': ?0 ,'workStatus': ?1 , 'postWork.category' : ?2 }")
              List<EmployeeWorks> getAll(String clientId, String status, String catogary);

              @org.springframework.data.mongodb.repository.Query(value = "{'clientId': ?0 ,'postWork.category': ?1}")
              List<EmployeeWorks> findAllByFilterWhenCatogaryIsAny(String clientId, String workStatus);

              @org.springframework.data.mongodb.repository.Query(value = "{'clientId': ?0 ,'workStatus': ?1}")
              List<EmployeeWorks> findAllByFilterWhenStatusIsAny(String clientId, String catogary);

}

interface CustomEmployeeWorksRepo{
       List<EmployeeWorks> findWorks(String employeeId);

       List<EmployeeWorks>  findAllByFilter(String clientId,String catogary,String status);


      // List<EmployeeWorks>  findAllByFilterWhenStatusIsAll(String clientId,String catogary);
       List<EmployeeWorks>  findAllByFilterWhenAll(String clientId);

       List<EmployeeWorks> findDocUriById(String jobId);


}

@Component
class EmployeeWorksRepoImpl implements CustomEmployeeWorksRepo{

    @Autowired
    public MongoTemplate mongoTemplate;

    @Override
    public List<EmployeeWorks> findWorks(String employeeId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("workStatus").is("pending").and("employeeId").is(employeeId));
        return mongoTemplate.find(query, EmployeeWorks.class);

    }

    @Override
    public List<EmployeeWorks> findAllByFilter(String clientId,String category,String workStatus) {
        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId).and("postWork.category").is(category));
       return mongoTemplate.find(query, EmployeeWorks.class);
    }



//    @Override
//    public List<EmployeeWorks> findAllByFilterWhenStatusIsAll(String clientId, String category) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("clientId").is(clientId).and("postWork.category").is(category));
//
//        return mongoTemplate.find(query, EmployeeWorks.class);
//    }

    @Override
    public List<EmployeeWorks> findAllByFilterWhenAll(String clientId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("clientId").is(clientId));

        return mongoTemplate.find(query, EmployeeWorks.class);
    }

    @Override
    public List<EmployeeWorks> findDocUriById(String jobId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("postWork.docUrl").is(jobId));

        return Collections.singletonList(mongoTemplate.findOne(query, EmployeeWorks.class));
    }



}
