package Fruit;

import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.jboss.resteasy.reactive.server.jaxrs.ResponseBuilderImpl.ok;

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class FruitResource {
    @Inject
    FruitService fruitService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAllFruits() {
        return fruitService.getAllFruit().map(data -> ok(data).build());
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getFruitById(@PathParam("id") final Long id) {
        return fruitService.findById(id)
                .map(fruitDto -> ok(fruitDto).build());
    }

    @POST
    public Uni<Response> createFruit(@NotNull @Valid FruitDto dto) {
        return fruitService.save(dto)
                .map(fruitDto -> Response.accepted(fruitDto).build());
    }

    @PUT
    @Path("{id}")
    public Uni<Response> updateFruit(@PathParam("id") final Long id, @Valid FruitDto dto) {
        return fruitService.update(dto,id)
                .map(fruitDto -> Response.accepted(fruitDto).build());
    }


    @DELETE
    @Path("{id}")
    public Uni<Response> deleteFruit(@Parameter(name = "id", required = true) @PathParam("id") Long id) {
        return fruitService.delete(id)
                .map(fruitDto -> Response.noContent().build());
    }
}
