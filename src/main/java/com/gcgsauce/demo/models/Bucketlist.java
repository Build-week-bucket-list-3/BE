package com.gcgsauce.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bucketlists")
public class Bucketlist extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty("bucketListId")
    private long bucketlistid;

    @Column
    private String bucketlistName;

    @OneToMany(mappedBy = "bucketlist",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    @JsonIgnoreProperties("bucketlist")
    @JsonProperty("entries")
    private List<BucketlistItem> bucketlistitems = new ArrayList<>();

    @ManyToOne
    @JsonIgnoreProperties("bucketlists")
    @JoinColumn(name = "userid", nullable = false)
    @JsonIgnore
    private User user;

    @Column
    private boolean shareable;

    public Bucketlist() {
    }

    public Bucketlist(String bucketlistName, boolean shareable) {
        this.bucketlistName = bucketlistName;
        this.shareable = shareable;
    }

    public long getBucketlistid() {
        return bucketlistid;
    }

    public void setBucketlistid(long bucketlistid) {
        this.bucketlistid = bucketlistid;
    }

    public String getBucketlistName() {
        return bucketlistName;
    }

    public void setBucketlistName(String bucketlistName) {
        this.bucketlistName = bucketlistName;
    }

    public boolean isShareable() {
        return shareable;
    }

    public void setShareable(boolean shareable) {
        this.shareable = shareable;
    }

    public List<BucketlistItem> getBucketlistitems() {
        return bucketlistitems;
    }

    public void setBucketlistitems(List<BucketlistItem> bucketlistitems) {
        this.bucketlistitems = bucketlistitems;
    }

//    public User getUser() {
//        return user;
//    }
//
//    public void setUser(User user) {
//        this.user = user;
//    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void addBucketListItem(BucketlistItem b){
        bucketlistitems.add(b);
    }
}
