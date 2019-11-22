package com.gcgsauce.demo.services;

import com.gcgsauce.demo.exceptions.ResourceFoundException;
import com.gcgsauce.demo.exceptions.ResourceNotFoundException;
import com.gcgsauce.demo.models.*;
import com.gcgsauce.demo.repositories.BucketListRepository;
import com.gcgsauce.demo.repositories.RoleRepository;
import com.gcgsauce.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userrepos;

    @Autowired
    private RoleRepository rolerepos;

    public List<Bucketlist> sortListByBucketListName(List<Bucketlist> list) {
        list = list.stream()
                .sorted(Comparator.comparing(Bucketlist::getBucketlistName))
                .collect(Collectors.toList());
        return list;
    }

    public List<BucketlistItem> sortBuckListItemsByCreationTime(List<BucketlistItem> list) {
        list = list.stream()
                .sorted(Comparator.comparing(BucketlistItem::getCreateddate))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public User findByName(String name) {
        User user = userrepos.findByUsername(name);
        if(user != null){
            return user;
        } throw new ResourceNotFoundException("User " + name + " not found.");
    }

    @Override
    public List<Bucketlist> findBucketlistByNamelike(String name) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());
        List<Bucketlist> bucketlists = new ArrayList<>();

        for(Bucketlist b: user.getBucketlists()){
            if(b.getBucketlistName().contains(name)){
                bucketlists.add(b);
            }
        }
        bucketlists = sortListByBucketListName(bucketlists);
        return bucketlists;
    }

    //bucket lists can have the same name ONLY if they are different privacy types, otherwise a user cannot have
    //the same bucket list name and have them both be shareable
    @Override
    public List<Bucketlist> findBucketlistInUserByName(String bucketlistName) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());
        List<Bucketlist> bucketlistsWithName = new ArrayList<>();

        for(Bucketlist b: user.getBucketlists()){
            if(b.getBucketlistName().equals(bucketlistName)){
                bucketlistsWithName.add(b);
            }
        }
        return bucketlistsWithName;
    }

    @Override
    public Bucketlist findBucketlistInUserByNameWithSpecificPrivacy(String bucketlistName, boolean shareable) {

        if(!shareable){
            for(Bucketlist b: findAllNonShareableBucketListsInUser()){
                if(b.getBucketlistName().equals(bucketlistName)){
                    return b;
                }
            }
        } else{
            for(Bucketlist b: findAllShareableBucketListsInUser()){
                if(b.getBucketlistName().equals(bucketlistName)){
                    return b;
                }
            }
        }

        throw new ResourceNotFoundException("Bucketlist " + bucketlistName + "(shareable: " + shareable + ") cannot be found!");
    }

    @Override
    public List<Bucketlist> findAllBucketlistsInUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();

        List<Bucketlist> b = sortListByBucketListName(userrepos.findByUsername(loggedInUser.getName()).getBucketlists());
        return b;
    }

    @Override
    public List<Bucketlist> findAllNonShareableBucketListsInUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());
        List<Bucketlist> list = new ArrayList<>();

        if (user != null) {
            for (Bucketlist b : user.getBucketlists()) {
                if (!b.isShareable()) {
                    list.add(b);
                }
            }
        } else {
            throw new ResourceNotFoundException("User has not been authenticated!");
        }
        list = sortListByBucketListName(list);
        return list;
    }

    @Override
    public List<Bucketlist> findAllShareableBucketListsInUser(long id) {

        User user = userrepos.findById(id).orElseThrow(()->new ResourceNotFoundException("User " + id + " not found."));

        List<Bucketlist> list = new ArrayList<>();

        if (user != null) {
            for (Bucketlist b : user.getBucketlists()) {
                if (b.isShareable()) {
                    list.add(b);
                }
            }
        } else {
            throw new ResourceNotFoundException("User has not been authenticated!");
        }
        list = sortListByBucketListName(list);
        return list;
    }

    @Override
    public List<Bucketlist> findAllShareableBucketListsInUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());

        List<Bucketlist> list = new ArrayList<>();

        if (user != null) {
            for (Bucketlist b : user.getBucketlists()) {
                if (b.isShareable()) {
                    list.add(b);
                }
            }
        } else {
            throw new ResourceNotFoundException("User has not been authenticated!");
        }
        list = sortListByBucketListName(list);
        return list;
    }

    @Override
    public List<Bucketlist> findAllSharedBucketlists() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());

        return user.getRequestedusersbucketlist();
    }

    @Override
    public Bucketlist addBucketlistToUser(Bucketlist newBucketlist) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(loggedInUser.getName());

        try{
            findBucketlistInUserByNameWithSpecificPrivacy(newBucketlist.getBucketlistName(), newBucketlist.isShareable());
            throw new ResourceFoundException("Bucket list with the name " + newBucketlist.getBucketlistName() + " with the selected privacy settings" +
                    " has been found.");
        } catch (ResourceNotFoundException r){ // we can only save if this method generated the response that meant there was a free space
            User user = new User();
            user.getBucketlists().add(newBucketlist);
            updateCurrentUser(user);
        }
        return newBucketlist;
    }

    @Transactional
    @Override
    public User updateCurrentUser(User updatedUser) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(loggedInUser.getName());

        if(currentUser == null){
            throw new ResourceNotFoundException("Not authenticated");
        }

        if(userrepos.findByEmail(updatedUser.getEmail()) != null){
            throw new ResourceFoundException("Email " + updatedUser.getEmail() + "is being used in another account");
        }

        if (updatedUser.getEmail() != null) {
            currentUser.setEmail(updatedUser.getEmail().toLowerCase());
        }

        if (updatedUser.getUsername() != null) {
            currentUser.setUsername(updatedUser.getUsername().toLowerCase());
        }

        if (updatedUser.getPassword() != null) {
            currentUser.setPasswordNoEncrypt(updatedUser.getPassword());
        }


        if (updatedUser.getUserroles().size() > 0) {
            throw new ResourceFoundException("User Roles are not updated through User.");
        }

        if(updatedUser.getBucketlists().size() > 0){
            for(Bucketlist b: updatedUser.getBucketlists()){
                currentUser.getBucketlists().add(b);
                b.setUser(currentUser);
            }
        }

        return userrepos.save(currentUser);
    }

    @Transactional
    @Override
    public User updateUserById(User updatedUser, long id, boolean isAdmin) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(loggedInUser.getName());

        if (id == currentUser.getUserid() || isAdmin)
        {
            if (updatedUser.getUsername() != null)
            {
                currentUser.setUsername(updatedUser.getUsername().toLowerCase());
            }

            if (updatedUser.getPassword() != null)
            {
                currentUser.setPasswordNoEncrypt(updatedUser.getPassword());
            }

            if (updatedUser.getEmail() != null)
            {
                currentUser.setEmail(updatedUser.getEmail().toLowerCase());
            }

            if (updatedUser.getUserroles()
                    .size() > 0)
            {
                throw new ResourceFoundException("User Roles are not updated through User.");
            }

            return userrepos.save(currentUser);
        } else
        {
            throw new ResourceNotFoundException(id + " Not current user");
        }
    }

    //has no auth
    @Override
    public void updateUserBucketlist(User updatedUser) {

        User currentUser = userrepos.findByUsername(updatedUser.getUsername());

        if(updatedUser.getBucketlists().size() > 0){
            for(Bucketlist b: updatedUser.getBucketlists()){
                currentUser.getBucketlists().add(b);
                b.setUser(currentUser);
            }
        }

        userrepos.save(currentUser);
    }

    @Override
    public void deleteCurrentUser() {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        userrepos.delete(userrepos.findByUsername(loggedInUser.getName()));
    }

    @Transactional
    @Override
    public void deleteById(long id, boolean isAdmin) {
        if(isAdmin){
            User user = userrepos.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User id " + id + " not found!"));

            userrepos.deleteById(id);
        }
    }

    @Transactional
    @Override
    public User save(User user)
    {
        if (userrepos.findByUsername(user.getUsername().toLowerCase()) != null)
        {
            throw new ResourceFoundException(user.getUsername() + " is already taken!");
        }

        User newUser = new User();
        newUser.setUsername(user.getUsername().toLowerCase());
        newUser.setPasswordNoEncrypt(user.getPassword());
        newUser.setEmail(user.getEmail().toLowerCase());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        for (UserRoles ur : user.getUserroles())
        {
            long id = ur.getRole()
                    .getRoleid();
            Role role = rolerepos.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Role id " + id + " not found!"));
            newRoles.add(new UserRoles(newUser,
                    ur.getRole()));
        }
        newUser.setUserroles(newRoles);

        return userrepos.save(newUser);
    }
}
