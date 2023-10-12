package br.com.henrique.todolist.user;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserControler {

    private final UserRepository userRepository;
    
    @GetMapping("/")
    public ResponseEntity<String> ola(){
        return ResponseEntity.ok("Hello world");
    }

    @PostMapping("/save")
    public ResponseEntity<UserModel> saveUser(@RequestBody UserModel userModel){
        Optional<UserModel> userExists = userRepository.findByUsername(userModel.getUsername());

        if(userExists.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        System.out.println(userModel.getPassword());
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(bcryptHashString);

        UserModel user = userRepository.save(userModel);
        return ResponseEntity.ok(user);
    }
}
