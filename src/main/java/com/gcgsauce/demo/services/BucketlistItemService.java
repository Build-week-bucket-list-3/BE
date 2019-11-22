package com.gcgsauce.demo.services;

import com.gcgsauce.demo.models.BucketlistItem;

public interface BucketlistItemService {

    BucketlistItem save(BucketlistItem item);
    BucketlistItem findBucketListItemById(long id);

    BucketlistItem update(BucketlistItem newBucketListItem, long id);

    void delete(long id);
}
