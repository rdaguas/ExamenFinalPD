package com.progra.distribuida.client;

import com.progra.distribuida.dto.UserDto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RegisterRestClient(baseUri = "stork://user-api")
@Path("https://jsonplaceholder.typicode.com")
public interface UserRestClient {
    @GET
    @Path("users/{id}")
    @Retry(maxRetries = 2, delay = 1000)
    @Fallback(fallbackMethod = "findAllFallback")
    UserDto findAll(@PathParam("id") Long id);

    default UserDto findAllFallback(Long id) {
        var user = new UserDto();
        user.setName("Usesrs unavailable");
        return user;
    }

    @PUT
    @Path("todos")
    @Retry(maxRetries = 2, delay = 1000)
    Response insertAll();
}