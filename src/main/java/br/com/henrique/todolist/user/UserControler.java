package br.com.henrique.todolist.user;

import java.util.Arrays;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.henrique.todolist.utils.Utils;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserControler {

    private final UserRepository userRepository;
   
    @PostMapping("/save")
    public ResponseEntity<String> saveUser(@RequestBody UserModel userModel){
        Optional<UserModel> userExists = userRepository.findByUserName(userModel.getUserName());
        String[] nullProperties = Utils.getNullPropertyNames(userModel);

        if(userExists.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario já cadastrado");
        }

        if(nullProperties.length > 2 || userModel.getUserName().trim() == "" || userModel.getName().trim() == "" || userModel.getPassword().trim() == ""){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Preencha todos os campos");
        }

        String bcryptHashString = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(bcryptHashString);
        userRepository.save(userModel);
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
