package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

	@Autowired
	UserRepository userRepository;

	@GetMapping("/")
	public String index() {
		return "Greetings from Test Boot!";
	}


	@GetMapping(value = "/users")
	public ResponseEntity<List<User>> getUsers() {
		try {
			List<User> usersList = new ArrayList<>();
			userRepository.findAll().forEach(usersList::add);
			return new ResponseEntity<>(usersList, HttpStatus.OK);
		} catch (Exception e) {
			throw new RuntimeException();

		}
	}

	@GetMapping(value = "/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable String id) {
		try {
			Optional<User> user = userRepository.findById(id);
			return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
		} catch (Exception e) {
			throw new RuntimeException();

		}
	}

	@PostMapping(value = "/user")
	public ResponseEntity<User> createUser(@RequestBody User user) {
		try{
			User newUser = new User(user.getId(),user.getFirstName(),user.getLastName(), user.getAddress());
			userRepository.save(newUser);
			return new ResponseEntity<>(newUser,HttpStatus.CREATED);
		}catch (Exception e) {
			throw  new RuntimeException();
		}
	}

	@DeleteMapping(value = "users/{id}")
	public void deleteUser(@PathVariable String id) {
		try{
			Optional<User> user = userRepository.findById(id);
			if(user.isPresent()){
				userRepository.delete(user.get());
				System.out.println("user with id "+ id + " successfully deleted" );
			} else {
				System.out.println("user with id "+ id + " not present" );
			}
		} catch (Exception e) {
			throw  new RuntimeException(e.getMessage());
		}
	}

}
