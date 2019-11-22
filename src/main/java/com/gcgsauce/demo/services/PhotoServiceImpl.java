package com.gcgsauce.demo.services;

import com.gcgsauce.demo.exceptions.ResourceFoundException;
import com.gcgsauce.demo.exceptions.ResourceNotFoundException;
import com.gcgsauce.demo.models.Photo;
import com.gcgsauce.demo.models.User;
import com.gcgsauce.demo.repositories.PhotoRepository;
import com.gcgsauce.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service(value = "photoService")
public class PhotoServiceImpl implements PhotoService {

    @Autowired
    private PhotoRepository photorepos;

    @Autowired
    private UserRepository userrepos;


    @Override
    public Photo findPhotoById(long id) {
        return photorepos.findById(id).orElseThrow(()-> new ResourceNotFoundException("Photo with id " + id + " has not been found"));
    }

    @Override
    public Photo updatePhoto(Photo updatedPhoto, long id) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());

        Photo currentPhoto = findPhotoById(id);
        String photoUsername = currentPhoto.getBucketlistitem().getBucketlist().getUser().getUsername();

        if(photoUsername.equalsIgnoreCase(loggedInUser.getName()) || user.usernameHasBeenRequested(user.getUsername())) {
            if(updatedPhoto.getPhotolink() != null){
                currentPhoto.setPhotolink(updatedPhoto.getPhotolink());
            }

            return photorepos.save(updatedPhoto);
        } else{
            throw new ResourceFoundException("This photo cannot be updated unless its in your bucket list / bucket list item"
                    + " or in the bucket lists you've requested!");
        }
    }

    @Override
    public void deletePhoto(long id) {
        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepos.findByUsername(loggedInUser.getName());

        Photo photo = findPhotoById(id);
        String photoUsername = photo.getBucketlistitem().getBucketlist().getUser().getUsername();

        if(photoUsername.equalsIgnoreCase(loggedInUser.getName()) || user.usernameHasBeenRequested(user.getUsername())) {
            photorepos.deleteById(id);
        } else{
            throw new ResourceFoundException("This photo cannot be deleted unless its in your bucket list / bucket list item"
            + " or in the bucket lists you've requested!");
        }
    }
}
