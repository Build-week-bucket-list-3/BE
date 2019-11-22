package com.gcgsauce.demo;

import com.gcgsauce.demo.models.Bucketlist;
import com.gcgsauce.demo.models.Role;
import com.gcgsauce.demo.models.User;
import com.gcgsauce.demo.models.UserRoles;
import com.gcgsauce.demo.repositories.BucketListRepository;
import com.gcgsauce.demo.repositories.UserRepository;
import com.gcgsauce.demo.services.BucketlistService;
import com.gcgsauce.demo.services.RoleService;
import com.gcgsauce.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;

//@Transactional
//@Component
public class SeedData implements CommandLineRunner
{
    private BucketListRepository bucketlistrepos;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userrepos;

    @Autowired
    RoleService roleService;

    @Autowired
    BucketlistService bucketlistService;

    public SeedData(BucketListRepository repo) {
        bucketlistrepos = repo;
    }

    @Override
    public void run(String[] args) throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        roleService.save(r1);
        roleService.save(r2);

        ArrayList<UserRoles> admins = new ArrayList<>();

        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));

        User u1 = new User("admin",
                "password",
                "admin@lambdaschool.local",
                admins);
        userService.save(u1);

        // data, user
        ArrayList<UserRoles> datas = new ArrayList<>();
        datas.add(new UserRoles(new User(), r2));
        User u2 = new User("cinnamon",
                "1234567",
                "cinnamon@lambdaschool.local",
                datas);
        userService.save(u2);

        // user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u3 = new User("barnbarn", "ILuvM4th!", "barnbarn@lambdaschool.local", users);
        userService.save(u3);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u4 = new User("puttat", "password", "puttat@school.lambda", users);
        userService.save(u4);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u5 = new User("misskitty", "password", "misskitty@school.lambda", users);
        userService.save(u5);

        Bucketlist bucketlist1 = new Bucketlist("Attend church", true);
        Bucketlist bucketlist2 = new Bucketlist("Attend aquarium", false);

        Bucketlist bucketlist3 = new Bucketlist("Musical", false);
        Bucketlist bucketlist4 = new Bucketlist("Travel", true);
//
//        bucketlist1.setUser(u1);
//        bucketlist2.setUser(u1);
//        bucketlist3.setUser(u1);
//
//        bucketlistService.save(bucketlist1);
//        bucketlistService.save(bucketlist2);
//        bucketlistService.save(bucketlist3);

        u1.getBucketlists().add(bucketlist1);
        u1.getBucketlists().add(bucketlist2);

        u2.getBucketlists().add(bucketlist3);
        u3.getBucketlists().add(bucketlist4);

        userService.updateUserBucketlist(u1);
        userService.updateUserBucketlist(u2);
        userService.updateUserBucketlist(u3);
    }
}
