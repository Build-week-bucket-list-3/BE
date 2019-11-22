package com.gcgsauce.demo.controllers;

import com.gcgsauce.demo.models.Photo;
import com.gcgsauce.demo.services.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/photos")
public class PhotoController {

    @Autowired
    PhotoService photoService;

//    @PostMapping(value = "/photo", consumes = {"application/json"})
//    public ResponseEntity<?> addPhotoToBucketlistItem(@Valid @RequestBody){
//
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }



    @PutMapping(value = "/photo/{photoid}")
    public ResponseEntity<?> updatePhoto(@RequestBody Photo photo, @PathVariable long photoid) {
        photoService.updatePhoto(photo, photoid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/photo/{photoid}")
    public ResponseEntity<?> deletePhoto(@PathVariable long photoid) {
        photoService.deletePhoto(photoid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
