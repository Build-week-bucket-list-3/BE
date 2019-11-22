package com.gcgsauce.demo.services;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.BucketlistItem;

import java.util.List;

public interface BucketlistService {

    Bucketlist findById(long id);

    //in the current user, find all bucket list items with names of 'bucketlistName' name and privacy 'bucketlistIsPrivate' with a name like 'likename'
    List<BucketlistItem> findAllItemsWIthNameLike(long bucketlistid, String likename);

    //in the current user, find all bucket list items with names of 'bucketlistName' name and privacy 'bucketlistIsPrivate' with photos
    List<BucketlistItem> findAllItemsWithPhotos(long id);

    Bucketlist update(Bucketlist bucketList, long bucketlistid);
    Bucketlist save(Bucketlist bucketList);

//    BucketlistItem updateBucketlistItem(BucketlistItem bucketlistItem, String bucketListItemName, String bucketListName, boolean shareable);
//    void deleteBucketlistItem(String bucketListItemName, String bucketListName, boolean shareable);

    void deleteBucketlist(long id);
}
