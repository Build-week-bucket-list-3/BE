package com.gcgsauce.demo.repositories;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.BucketlistItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BucketListItemRepository extends CrudRepository<BucketlistItem, Long> {

    List<BucketlistItem> findBucketListItemsByItemnameContains(String name);

    //we have the current bucketlist. find and return the bucketlist item in this bucket list that has the same item name
    @Query("SELECT bi FROM BucketlistItem bi WHERE bi.bucketlist = :b AND bi.itemname = :itemname")
    BucketlistItem findBucketlistItemByItemname(Bucketlist b, String itemname);
}
