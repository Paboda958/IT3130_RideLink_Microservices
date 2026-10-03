package com.ridelink.payment.service;

import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class SequenceGenerator {

    private final MongoTemplate mongoTemplate;

    public SequenceGenerator(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public long nextId(String sequenceName) {
        Document counter = mongoTemplate.findAndModify(
                Query.query(Criteria.where("_id").is(sequenceName)),
                new Update().inc("sequence", 1L),
                FindAndModifyOptions.options().upsert(true).returnNew(true),
                Document.class,
                "sequences"
        );
        return ((Number) counter.get("sequence")).longValue();
    }
}