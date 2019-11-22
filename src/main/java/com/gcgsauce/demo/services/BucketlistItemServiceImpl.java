package com.gcgsauce.demo.services;

import com.gcgsauce.demo.exceptions.ResourceFoundException;
import com.gcgsauce.demo.exceptions.ResourceNotFoundException;
import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.BucketlistItem;
import com.gcgsauce.demo.models.Photo;
import com.gcgsauce.demo.repositories.BucketListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service(value = "bucketListItemService")
public class BucketlistItemServiceImpl implements BucketlistItemService {

    @Autowired
    BucketListItemRepository bucketlistitemrepos;

    @Autowired
    UserService userService;

    @Override
    public BucketlistItem save(BucketlistItem item) {
        if(item != null){
            return bucketlistitemrepos.save(item);
        } return null;
    }

    @Override
    public BucketlistItem findBucketListItemById(long id) {
        return bucketlistitemrepos.findById(id).orElseThrow(()-> new ResourceNotFoundException("Bucket List Item " + id + " not found"));
    }

    @Transactional
    @Override
    public BucketlistItem update(BucketlistItem newBucketListItem, long id) {

        BucketlistItem currentBucketlistItem = findBucketListItemById(id);

        if (newBucketListItem.getItemdescription() != null) {
            currentBucketlistItem.setItemdescription(newBucketListItem.getItemdescription());
        }

        if (newBucketListItem.getItemname() != null) {
            currentBucketlistItem.setItemname(newBucketListItem.getItemname());
        }

        if(newBucketListItem.getBucketlist() != null){
            currentBucketlistItem.setBucketlist(newBucketListItem.getBucketlist());
        }

        if(newBucketListItem.getPhotos().size() > 0){
            for(Photo p: newBucketListItem.getPhotos()){
                Photo newPhoto = new Photo(p.getPhotolink(), p.getBucketlistitem());

                currentBucketlistItem.getPhotos().add(newPhoto);
            }
        }

        return bucketlistitemrepos.save(currentBucketlistItem);
    }

    @Override
    public void delete(long id) {
        bucketlistitemrepos.deleteById(id);
    }
}
