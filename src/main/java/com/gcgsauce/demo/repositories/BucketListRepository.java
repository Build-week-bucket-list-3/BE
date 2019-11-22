package com.gcgsauce.demo.repositories;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.BucketlistItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BucketListRepository extends CrudRepository<Bucketlist, Long> {

    Bucketlist findByBucketlistName(String name);
}