<<<<<<< HEAD
package com.example.eventix.repository;

import com.example.eventix.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByRegNo(String regNo);
}
=======
package com.example.eventix.repository;

import com.example.eventix.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users, Integer> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByRegNo(String regNo);
}
>>>>>>> 4f912b739ea1ddc3a83484fa6247a275f99e1b3b
