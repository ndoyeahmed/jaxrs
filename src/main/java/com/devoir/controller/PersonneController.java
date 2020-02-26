package main.java.com.devoir.controller;

import main.java.com.devoir.dao.implementation.PersonneImp;
import main.java.com.devoir.dao.interfaces.IPersonne;
import main.java.com.devoir.models.Personne;
import main.java.com.devoir.utils.JsonResponse;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.List;

@Path("/")
public class PersonneController {

    private IPersonne iPersonne = new PersonneImp();

    private JsonResponse jsonResponse = new JsonResponse();

    @POST
    @Path("personnes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String addPersonne(String body) {
        Personne personne = jsonResponse.getGsonInstance().fromJson(body, Personne.class);
        if (iPersonne.save(personne))
            return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("success", true));
        else
            return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("error", HttpServletResponse.SC_BAD_REQUEST));
    }

    @GET
    @Path("personnes")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersonne() {
        List<Personne> personnes = iPersonne.findAll();
        return jsonResponse.getGsonInstance().toJson(personnes);
    }

    @GET
    @Path("personnes/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonneById(@PathParam("id") Long id) {
        Personne personne = iPersonne.getPersonneById(id);
        if (personne != null) {
            return jsonResponse.getGsonInstance().toJson(personne);
        } else {
            return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("error", HttpServletResponse.SC_BAD_REQUEST));
        }
    }

    @POST
    @Path("personnes/update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String updatePersonne(@PathParam("id") Long id, String body) {
        Personne personne = jsonResponse.getGsonInstance().fromJson(body, Personne.class);
        if (iPersonne.update(id, personne)) {
            return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("success", true));
        } else {
            return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("error", HttpServletResponse.SC_BAD_REQUEST));
        }
    }

    @POST
    @Path("personnes/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePersonne(@PathParam("id") Long id) {
        try {
            if (iPersonne.delete(id)) {
                return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("success", true));
            } else {
                return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("error", HttpServletResponse.SC_BAD_REQUEST));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return jsonResponse.getGsonInstance().toJson(Collections.singletonMap("error", HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
