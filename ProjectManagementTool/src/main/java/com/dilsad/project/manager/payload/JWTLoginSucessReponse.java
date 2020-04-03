package com.dilsad.project.manager.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JWTLoginSucessReponse {
	
    private boolean success;
    private String token;

    

    @Override
    public String toString() {
        return "JWTLoginSucessReponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                '}';
    }	
	

}
