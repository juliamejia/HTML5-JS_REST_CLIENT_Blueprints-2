/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.controllers;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 *
 * @author hcadavid
 */
@RestController
@RequestMapping(value = "/blueprints")

public class BlueprintAPIController {

    @Autowired
    BlueprintsServices bps;
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> getBlueprint(){
        try{
            Set<Blueprint> data = bps.getAllBlueprints();
            Gson gson = new Gson();
            return new ResponseEntity<>(gson.toJson(data), HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error",HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/{author}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlueprintByAuthor(@PathVariable String author) throws BlueprintNotFoundException {
        Set<Blueprint> data = bps.getBlueprintsByAuthor(author);
        Gson gson = new Gson();
        if(data.isEmpty()){
            return new ResponseEntity<>("Dicho autor no existe",HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(gson.toJson(data), HttpStatus.ACCEPTED);


    }

    @RequestMapping(path = "/{author}/{bpname}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBlueprintByAuthorName(@PathVariable String author, @PathVariable String bpname) throws BlueprintNotFoundException {
            Blueprint blueprint = bps.getBlueprint(author, bpname);
            Gson gson = new Gson();
            if(blueprint == null){
                return new ResponseEntity<>("Dicho nombre o autor no existe",HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(gson.toJson(blueprint), HttpStatus.ACCEPTED);

    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> postBlueprint(@RequestBody Blueprint blueprint){
        try {
//            System.out.println(blueprint.getName());
            bps.addNewBlueprint(blueprint);
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException ex) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(path = "/{author}/{bpname}", method = RequestMethod.PUT,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> putBlueprint(@PathVariable String author, @PathVariable String bpname, @RequestBody Blueprint blueprint) throws BlueprintNotFoundException {
        bps.updatePoints(author, bpname, blueprint.getPoints());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @RequestMapping(path = "/{author}/{bpname}", method = RequestMethod.DELETE,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBlueprint(@PathVariable String author, @PathVariable String bpname) throws BlueprintNotFoundException {
        bps.deleteBlueprint(author, bpname);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}

