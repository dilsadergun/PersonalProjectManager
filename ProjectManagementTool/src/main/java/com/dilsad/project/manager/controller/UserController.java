package com.dilsad.project.manager.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dilsad.project.manager.entitiy.User;
import com.dilsad.project.manager.payload.JWTLoginSucessReponse;
import com.dilsad.project.manager.payload.LoginRequest;
import com.dilsad.project.manager.security.JwtTokenProvider;
import com.dilsad.project.manager.service.MapValidationErrorService;
import com.dilsad.project.manager.service.UserService;
import com.dilsad.project.manager.validator.UserValidator;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import static com.dilsad.project.manager.security.SecurityConstants.TOKEN_PREFIX;



@RestController
@RequestMapping("/api/users")
public class UserController {
	
	@Autowired
    private MapValidationErrorService mapValidationErrorService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserValidator userValidator;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @RequestMapping( value= "/login",  method = { RequestMethod.POST})
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult result){
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = TOKEN_PREFIX +  tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTLoginSucessReponse(true, jwt));
    }
	@RequestMapping( value= "/register",  method = { RequestMethod.POST})
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
        // Validate passwords match
		
		userValidator.validate(user,result);

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null)return errorMap;

        User newUser = userService.saveUser(user);

        return  new ResponseEntity<User>(newUser, HttpStatus.CREATED);
    }

}
