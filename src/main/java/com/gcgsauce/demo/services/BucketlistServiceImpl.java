package com.gcgsauce.demo.services;

import com.gcgsauce.demo.exceptions.ResourceFoundException;
import com.gcgsauce.demo.exceptions.ResourceNotFoundException;
import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.BucketlistItem;
import com.gcgsauce.demo.repositories.BucketListItemRepository;
import com.gcgsauce.demo.repositories.BucketListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "bucketListService")
public class BucketlistServiceImpl implements BucketlistService {

    @Autowired
    BucketListRepository bucketlistrepos;

    @Autowired
    BucketlistItemService bucketListItemService;

    @Autowired
    UserService userService;

    @Autowired
    BucketListItemRepository bucketlistitemrepos;

    public List<BucketlistItem> sortListByBucketListItemName(List<BucketlistItem> list){
        list = list.stream()
                .sorted(Comparator.comparing(BucketlistItem::getItemname))
                .collect(Collectors.toList());
        return list;
    }

    public List<BucketlistItem> sortBuckListItemsByCreationTime(List<BucketlistItem> list){
        list = list.stream()
                .sorted(Comparator.comparing(BucketlistItem::getCreateddate))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public Bucketlist findById(long id) {
        return bucketlistrepos.findById(id).orElseThrow(()-> new ResourceNotFoundException("Bucketlist with id of " + id + " cannot be found!"));
    }

    @Override
    public List<BucketlistItem> findAllItemsWIthNameLike(long bucketlistid, String likename) {

        Bucketlist bucketlist = findById(bucketlistid);

        List<BucketlistItem> entries = new ArrayList<>();

        for (BucketlistItem b : bucketlist.getBucketlistitems()) {
            if (b.getItemname().contains(likename)) {
                entries.add(b);
            }
        }
        entries = sortListByBucketListItemName(entries);
        return entries;
    }

    @Override
    public List<BucketlistItem> findAllItemsWithPhotos(long id) {

        Bucketlist bucketList = findById(id);

        List<BucketlistItem> entries = new ArrayList<>();
        for (BucketlistItem b : bucketList.getBucketlistitems()) {
            if (b.getPhotos().size() > 0) {
                entries.add(b);
            }
        }
        entries = sortBuckListItemsByCreationTime(entries);
        return entries;
    }

    @Transactional
    @Override
    public Bucketlist update(Bucketlist bucketList, long bucketlistid) {

        Bucketlist currentBucketlist = findById(bucketlistid);

        if(bucketList.getBucketlistName() != null) {
            currentBucketlist.setBucketlistName(bucketList.getBucketlistName());
        }

        if(bucketList.isShareable() != currentBucketlist.isShareable()){
            currentBucketlist.setShareable(bucketList.isShareable());
        }

        if(bucketList.getBucketlistitems().size() > 0 ){

            for (BucketlistItem b : bucketList.getBucketlistitems()) {

                if(b.getPhotos().size() > 0){
                    throw new ResourceFoundException("Must add photos through a different endpoint.");
                }
                BucketlistItem bucketListItem = new BucketlistItem(b.getItemname(), b.getItemdescription(), currentBucketlist);

                if(bucketlistitemrepos.findBucketlistItemByItemname(currentBucketlist, bucketListItem.getItemname()) == null){
                    currentBucketlist.addBucketListItem(bucketListItem);
                } else{
                    throw new ResourceFoundException("Trying to add bucket list item with the same name as a current bucket list item in the bucket list"
                    + currentBucketlist.getBucketlistName() + ". Bucket list items within the same bucket list must be unique!");
                }
            }
        }
        return save(currentBucketlist);
    }

//    @Override
//    public BucketlistItem updateBucketlistItem(BucketlistItem bucketlistItem, String bucketListItemName, String bucketListName, boolean shareable) {
//        return bucketListItemService.update(bucketlistItem, bucketListItemName, bucketListName, shareable);
//    }

    @Transactional
    @Override
    public Bucketlist save(Bucketlist bucketList)    {
        return bucketlistrepos.save(bucketList);
    }

    @Override
    public void deleteBucketlist(long id) {
        Bucketlist b = findById(id);
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        //only able to delete bucket lists that you own
        if(b.getUser().getUsername().equals(loggedInUser.getName())){
            bucketlistrepos.deleteById(id);
        } else{
            throw new ResourceFoundException("Cannot delete other user's bucketlist.");
        }
    }

//    @Override
//    public void deleteBucketlistItem(String bucketListItemName, String bucketListName, boolean shareable) {
//        bucketListItemService.delete(bucketListItemName, bucketListName, shareable);
//    }
}
