package com.gcgsauce.demo.services;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.User;

import java.util.List;

public interface UserService {

    User findByName(String name);
    List<Bucketlist> findAllBucketlistsInUser();
    List<Bucketlist> findAllNonShareableBucketListsInUser();
    List<Bucketlist> findAllShareableBucketListsInUser();
    List<Bucketlist> findAllShareableBucketListsInUser(long id);
    List<Bucketlist> findAllSharedBucketlists();
    List<Bucketlist> findBucketlistByNamelike(String name);

    List<Bucketlist> findBucketlistInUserByName(String bucketlistName);
    Bucketlist findBucketlistInUserByNameWithSpecificPrivacy(String bucketlistName, boolean isPrivate);

    Bucketlist addBucketlistToUser(Bucketlist newBucketlist);

    User updateUserById(User updatedUser, long id, boolean isAdmin);
    User updateCurrentUser(User updatedUser);
    void updateUserBucketlist(User updatedUser);

    void deleteCurrentUser();
    void deleteById(long id, boolean isAdmin);

    User save(User user);
}
