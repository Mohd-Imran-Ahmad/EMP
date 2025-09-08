package com.employee.ems.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.employee.ems.dto.APIResponse;
import com.employee.ems.dto.EmployeeRegistrationDto;
import com.employee.ems.entity.EmployeeRegistration;
import com.employee.ems.service.EmployeeRegistrationService;
import com.employee.ems.service.JwtService;
import com.employee.ems.service.TokenBlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reg")
//@CrossOrigin
//        (
//        origins = "http://localhost:4200",
//        methods = {RequestMethod.GET,
//                RequestMethod.POST,
//                RequestMethod.PUT,
//                RequestMethod.DELETE,
//                RequestMethod.OPTIONS},
//         allowedHeaders = "*",
////        allowedHeaders = {"Authorization",
////                "Content-Type",
////                "X-Requested-With"},
//        allowCredentials = "true"
//)

public class EmployeeRegistrationController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private EmployeeRegistrationService employeeRegistrationService;

    @Autowired
    private TokenBlacklistService blacklistService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody EmployeeRegistrationDto employeeRegistrationDto) {
        try {
            EmployeeRegistrationDto createdUser = employeeRegistrationService.createUser(employeeRegistrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registration successful.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User registration failed.");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<APIResponse<String>> loginCheck(@RequestBody EmployeeRegistration employeeRegistration){

        APIResponse<String> response = new APIResponse<>();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(employeeRegistration.getUsername(),
                        employeeRegistration.getPassword());

        try {
            Authentication authenticate = authManager.authenticate(token);

            if(authenticate.isAuthenticated()) {
                String jwtToken = jwtService.generateToken(employeeRegistration.getUsername(),
                        authenticate.getAuthorities().iterator().next().getAuthority());

                response.setMessage("Login Successful");
                response.setStatus(200);
                response.setData(jwtToken);  // return JWT
                return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setMessage("Failed");
        response.setStatus(401);
        response.setData("Un-Authorized Access");
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getStatus()));
    }



    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            long expiry = getExpirationFromToken(token); // use JWT parser
            blacklistService.blacklistToken(token, expiry);
        }

        return ResponseEntity.ok("Logged out successfully");
    }

    private long getExpirationFromToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret"); // Use the same key used to sign the token
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        return jwt.getExpiresAt().getTime();
    }
}
