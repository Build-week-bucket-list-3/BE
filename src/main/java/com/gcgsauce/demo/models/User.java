package com.gcgsauce.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false,
            unique = true)
    @Email
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    private List<Bucketlist> bucketlists = new ArrayList<>();

    @JsonIgnore //other user's bucketlist that this user requested, group this by people who requested for this
    @Transient
    private List<Bucketlist> requestedusersbucketlist = new ArrayList<>();

    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties("user")
    @JsonIgnore
    private List<UserRoles> userroles = new ArrayList<>();


    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password, @Email String email, List<Bucketlist> bucketlists, List<Bucketlist> requestedusersbucketlist, List<UserRoles> userroles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.bucketlists = bucketlists;
        this.requestedusersbucketlist = requestedusersbucketlist;
        this.userroles = userroles;
    }

    public User(String username, String password, String email, List<UserRoles> userRoles)
    {
        setUsername(username);
        setPassword(password);
        this.email = email;
        for (UserRoles ur : userRoles)
        {
            ur.setUser(this);
        }
        this.userroles = userRoles;
    }


    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getUsername() { // this is possible when updating a user
        if (username == null) {
            return null;
        } else
        {
            return username.toLowerCase();
        }
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setPasswordNoEncrypt(String password) {
        this.password = password;
    }

    public String getEmail() {
        if (email == null) // this is possible when updating a user
        {
            return null;
        } else
        {
            return email.toLowerCase();
        }
    }

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public List<Bucketlist> getBucketlists() {
        return bucketlists;
    }

    public void setBucketlists(List<Bucketlist> bucketlists) {
        this.bucketlists = bucketlists;
    }

    public List<Bucketlist> getRequestedusersbucketlist() {
        return requestedusersbucketlist;
    }

    public void setRequestedusersbucketlist(List<Bucketlist> requestedusersbucketlist) {
        this.requestedusersbucketlist = requestedusersbucketlist;
    }

    public List<UserRoles> getUserroles() {
        return userroles;
    }

    public void setUserroles(List<UserRoles> userroles) {
        this.userroles = userroles;
    }

    public boolean usernameHasBeenRequested(String username){
        for(Bucketlist b: requestedusersbucketlist){
            if(b.getUser().equals(username)){
                return true;
            }
        } return false;
    }

    @JsonIgnore
    public List<SimpleGrantedAuthority> getAuthority() {
        List<SimpleGrantedAuthority> rtnList = new ArrayList<>();

        for (UserRoles r : this.userroles)
        {
            String myRole = "ROLE_" + r.getRole()
                    .getName()
                    .toUpperCase();
            rtnList.add(new SimpleGrantedAuthority(myRole));
        }

        return rtnList;
    }
}
