package com.tochratana.api.service.Impl;

import com.tochratana.api.dto.RegisterRequest;
import com.tochratana.api.dto.RegisterResponse;
import com.tochratana.api.service.AuthService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final Keycloak keycloak;
    @Override
    public RegisterResponse register(RegisterRequest registerRequest) {

        if(!registerRequest.password().equals(registerRequest.confirmedPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Password don't match");
        }
        UserRepresentation user = new UserRepresentation();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setFirstName(registerRequest.firstName());
        user.setLastName(registerRequest.lastName());

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(registerRequest.password());

        user.setCredentials(List.of(credential));
        user.setEmailVerified(false);
        user.setEnabled(true);

        try (Response response = keycloak.realm("mbapi").users().create(user)) {

            // Only run email verify if user creation is successful
            if (response.getStatus() == HttpStatus.CREATED.value()) {

                // Get the created user's ID from the Location header
                String location = response.getHeaderString("Location");
                String userId = location.substring(location.lastIndexOf("/") + 1);

                // Send verification email
                keycloak.realm("endora_api")
                        .users()
                        .get(userId)
                        .sendVerifyEmail();

                return RegisterResponse.builder()
                        .email(registerRequest.email())
                        .firstName(registerRequest.firstName())
                        .lastName(registerRequest.lastName())
                        .build();
            } else {
                String errorBody = response.readEntity(String.class);
                throw new ResponseStatusException(
                        HttpStatus.valueOf(response.getStatus()),
                        "Fail to create user: " + errorBody
                );
            }
        }


    }

    @Override
    public void verify(String userId) {
        UserResource userResource = keycloak.realm("endora_api").users().get(userId);
        userResource.sendVerifyEmail();
    }
}
