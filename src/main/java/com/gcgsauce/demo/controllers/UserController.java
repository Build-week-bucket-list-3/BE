package com.gcgsauce.demo.controllers;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.User;
import com.gcgsauce.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping(value = "/request/{username}")
    public ResponseEntity<?> requestUserBucket(@PathVariable String username){
        User user = userService.findByName(username);
        User userUpdated = new User();
        List<Bucketlist> requestedbucketlists = userService.findAllShareableBucketListsInUser(user.getUserid());

        for(Bucketlist b: requestedbucketlists){
            userUpdated.getRequestedusersbucketlist().add(b);
        }

        userService.updateCurrentUser(userUpdated);

        return new ResponseEntity<>(requestedbucketlists, HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> findAllBucketLists(){
        List<Bucketlist> list = userService.findAllBucketlistsInUser();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/bucketlists/{namelike}")
    public ResponseEntity<?> findAllBucketListsByNamelike(@PathVariable String namelike) {
        List<Bucketlist> list = userService.findBucketlistByNamelike(namelike);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/shared")
    public ResponseEntity<?> findSharedBucketlists(){
        List<Bucketlist> list = userService.findAllSharedBucketlists();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/nonshareable")
    public ResponseEntity<?> findNonShareableBucketlists(){
        List<Bucketlist> list = userService.findAllNonShareableBucketListsInUser();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/shareable")
    public ResponseEntity<?> findShareableBucketlists(){
        List<Bucketlist> list = userService.findAllShareableBucketListsInUser();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /*
    {
        "username": "gcgsauce",
        "password": "ILuvM4th!",
        "email": "gattigcg1@gmail.com"
    }
    */
    @PutMapping(value = "/user", consumes = {"application/json"})
    public ResponseEntity<?> updateCurrentUser(HttpServletRequest request, @RequestBody User updateUser) {
        userService.updateCurrentUser(updateUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
        {
            "username": "gcgsauce",
            "password": "ILuvM4th!"
            "email": "gattigcg1@gmail.com"
        }
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping(value = "/user/{id}")
    public ResponseEntity<?> updateUserById(HttpServletRequest request, @RequestBody User updateUser, @PathVariable long id) {
        userService.updateUserById(updateUser, id, request.isUserInRole("ADMIN"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    {
        "bucketlistName": "gcgsauce's list",
        "shareable": false
    }
     */
    @PostMapping(value = "/bucketlist", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addNewBucketlistToUser(HttpServletRequest request, @Valid @RequestBody Bucketlist newBucketlist) throws URISyntaxException {

        newBucketlist = userService.addBucketlistToUser(newBucketlist);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{bucketlistid}")
                .buildAndExpand(newBucketlist.getBucketlistid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }


    @DeleteMapping(value = "/bucketlist")
    public ResponseEntity<?> deleteCurrentUser(HttpServletRequest request){
        userService.deleteCurrentUser();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping(value = "/user/{id}")
    public ResponseEntity<?> deleteUserById(HttpServletRequest request, @PathVariable long id){
        userService.deleteById(id, request.isUserInRole("ADMIN"));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/user",
            consumes = {"application/json"},
            produces = {"application/json"})
    public ResponseEntity<?> addNewUser(HttpServletRequest request,@Valid @RequestBody User newuser) throws URISyntaxException {
        newuser = userService.save(newuser);

        // set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{userid}")
                .buildAndExpand(newuser.getUserid())
                .toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    // http://localhost:2032/users/getuserinfo
    @GetMapping(value = "/getuserinfo",
            produces = {"application/json"})
    public ResponseEntity<?> getCurrentUserInfo(HttpServletRequest request, Authentication authentication) {
        User u = userService.findByName(authentication.getName());
        return new ResponseEntity<>(u,
                HttpStatus.OK);
    }
}
