package lk.creativelabs.jobseekers.repo;




import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmittedWorksRepo extends MongoRepository<SubmittedWorks, String> {
    SubmittedWorks findByJobId(String jobId);


    @Query(value = "{'clientId': ?0 ,'workStatus': ?1 }")
    List<SubmittedWorks> getSubmittedAllWorks(String clientId, String status);



}

//interface CustomSubmittedWorksRepo {
//    List<SubmittedWorks> getSubmittedAllWorks(String clientId,String status);
//}
//
//
//class CustomSubmittedWorkImpl implements CustomSubmittedWorksRepo {
//
//    private MongoTemplate mongoTemplate;
//
//    @Override
//    public List<SubmittedWorks> getSubmittedAllWorks(String clientId,String status) {
//        Query query = new Query();
//        query.addCriteria(Criteria.where("workStatus").is(status).and("clientId").is(clientId));
//        return mongoTemplate.find(query, SubmittedWorks.class);
//    }
//}