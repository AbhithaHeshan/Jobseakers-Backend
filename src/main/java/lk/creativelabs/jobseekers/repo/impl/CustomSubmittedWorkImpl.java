package lk.creativelabs.jobseekers.repo.impl;

import lk.creativelabs.jobseekers.entity.SubmittedWorks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public class CustomSubmittedWorkImpl {


    private MongoTemplate mongoTemplate;

    public List<SubmittedWorks> getSubmittedAllWorks(String clientId, String status) {
        Query query = new Query();
        query.addCriteria(Criteria.where("workStatus").is(status).and("clientId").is(clientId));
        return mongoTemplate.find(query, SubmittedWorks.class);
    }
}
