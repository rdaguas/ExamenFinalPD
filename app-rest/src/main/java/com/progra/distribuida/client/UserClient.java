package com.progra.distribuida.client;

import com.progra.distribuida.dto.UserDto;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "user-api")
@Path("/users")
public interface UserClient {

    @GET
    @Path("/{id}")
    UserDto getUser(@PathParam("id") Long id);
}