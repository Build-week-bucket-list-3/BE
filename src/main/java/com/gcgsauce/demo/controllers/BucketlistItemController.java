package com.gcgsauce.demo.controllers;

import com.gcgsauce.demo.models.BucketlistItem;
import com.gcgsauce.demo.models.Photo;
import com.gcgsauce.demo.services.BucketlistItemService;
import com.gcgsauce.demo.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/items")
public class BucketlistItemController {

    @Autowired
    BucketlistItemService bucketListItemService;

    @Autowired
    PhotoService photoService;

    @PostMapping(value = "{itemid}/photo", consumes = {"application/json"})
    public ResponseEntity<?> addPhotoToBucketlistItem(@Valid @RequestBody Photo photo, @PathVariable long itemid) {

        BucketlistItem newBucketlistItem = new BucketlistItem();
        newBucketlistItem.getPhotos().add(photo);
        bucketListItemService.update(newBucketlistItem, itemid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
