package com.gcgsauce.demo.controllers;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.BucketlistItem;
import com.gcgsauce.demo.services.BucketlistItemService;
import com.gcgsauce.demo.services.BucketlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bucketlists")
public class BucketlistController {

    @Autowired
    private BucketlistService bucketListService;

    @Autowired
    private BucketlistItemService bucketListItemService;

    @GetMapping(value = "{bucketlistid}/items/{namelike}")
    public ResponseEntity<?> findItemsWithNameLike(@PathVariable long bucketlistid,
                                                   @PathVariable String namelike){
        List<BucketlistItem> list = bucketListService.findAllItemsWIthNameLike(bucketlistid, namelike);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{bucketlistid}/items/withphotos")
    public ResponseEntity<?> findItemsWithPhotos(@PathVariable long id){
        List<BucketlistItem> list = bucketListService.findAllItemsWithPhotos(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
            {
               "name": "Having fun",
               "description": "party at my place"
            }
    */ //photos updated in a different controller
    @PostMapping(value = "/bucketlist/{bucketlistid}/item",
                consumes = {"application/json"})
    public ResponseEntity<?> addBucketListItemToBucketList(@Valid @RequestBody
                                                            BucketlistItem newBucketListItem, @PathVariable long bucketlistid) {
        Bucketlist bucketList = new Bucketlist();
        bucketList.addBucketListItem(newBucketListItem);
        bucketListService.update(bucketList, bucketlistid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
        {
           "bucketlistName": "30 days before I die",
           "shareable": "false"
        }
     */
    @Transactional
    @PutMapping(value = "/bucketlist/{bucketlistid}")
    public ResponseEntity<?> updateBucketList(@Valid @RequestBody Bucketlist bucketList,
                                              @PathVariable long bucketlistid){
        bucketListService.update(bucketList, bucketlistid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
        {
           "itemName": "30 days before I die",
           "description": "false"
        }
     */
//    @Transactional
//    @PutMapping(value = "/{bucketlistname}/shareable/{shareable/item/{bucketlistitemname}",
//            consumes = {"application/json"})
//    public ResponseEntity<?> updateBucketListItemByName(@RequestBody BucketlistItem bucketListItem, @PathVariable String bucketlistitemname,
//                                                        @PathVariable String bucketlistname, @PathVariable boolean shareable){
//        bucketListItemService.update(bucketListItem, bucketlistitemname, bucketlistname, shareable);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
//
//    @DeleteMapping(value = "/{bucketlistname}/shareable/{shareable}/items/{bucketlistitemname}")
//    public ResponseEntity<?> deleteBucketlistItemByName(@PathVariable String bucketlistitemname,
//                                                        @PathVariable String bucketlistname, @PathVariable boolean shareable) {
//        bucketListService.deleteBucketlistItem(bucketlistitemname, bucketlistname, shareable);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @DeleteMapping(value = "/bucketlist/{bucketlistid}")
    public ResponseEntity<?> deleteBucketlist(@PathVariable long bucketlistid) {
        bucketListService.deleteBucketlist(bucketlistid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}