package no.ntnu.mycbr.rest.controller;

import de.dfki.mycbr.core.ICaseBase;
import de.dfki.mycbr.core.Project;
import de.dfki.mycbr.core.model.AttributeDesc;
import de.dfki.mycbr.core.model.Concept;
import de.dfki.mycbr.core.model.DoubleDesc;
import de.dfki.mycbr.core.model.StringDesc;
import de.dfki.mycbr.core.similarity.AmalgamationFct;
import de.dfki.mycbr.core.similarity.DoubleFct;
import de.dfki.mycbr.core.similarity.config.NumberConfig;
import no.ntnu.mycbr.rest.*;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@RestController
public class ConceptController {
    private final Log logger = LogFactory.getLog(getClass());

    //get all amalgationfunctions
    @ApiOperation(value = "getAmalgamationFunctions", nickname = "getAmalgamationFunctions")
    @RequestMapping(method = RequestMethod.GET, path="/concepts/{concept}/amalgamationFunctions", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AmalgamationFunctions.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public AmalgamationFunctions getAmalgamationFunctions(@PathVariable(value="concept") String concept) {
        return new AmalgamationFunctions(concept);
    }

    //delete all amalgationfunctions
    @ApiOperation(value = "deleteAmalgamationFunctions", nickname = "deleteAmalgamationFunctions")
    @RequestMapping(method = RequestMethod.DELETE, path="/concepts/{conceptID}/amalgamationFunctions", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public boolean deleteAmalgamationFunctions(@PathVariable(value="conceptID") String conceptID) {
        Concept thisconcept = App.getProject().getAllSubConcepts().get(conceptID);
        List<AmalgamationFct> list = thisconcept.getAvailableAmalgamFcts();
        for(AmalgamationFct fct : list){
            thisconcept.deleteAmalgamFct(fct);
        }
        return true;

    }

    //add one amalgationfunction
    @ApiOperation(value = "addAmalgamationFunction", nickname = "addAmalgamationFunction")
    @RequestMapping(method = RequestMethod.PUT, path="/concepts/{concept}/amalgamationFunctions", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AmalgamationFunctions.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public AmalgamationFunctions addAmalgamationFunctions(@PathVariable(value="concept") String concept,
                                                          @PathVariable(value="amalgationfunction") String amalgationfunction) {
        return new AmalgamationFunctions(concept);
    }

    //delete one amalgationfunction
    @ApiOperation(value = "deleteAmalgamationFunction", nickname = "deleteAmalgamationFunction")
    @RequestMapping(method = RequestMethod.DELETE, path="/concepts/{conceptID}/amalgamationFunctions/{amalgamationFunction}", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")})
    public boolean deleteAmalgamationFunction(@PathVariable(value="conceptID") String conceptID,
                                               @PathVariable(value="amalgamationFunction") String amalgamationFunction) {
        Concept thisconcept = App.getProject().getAllSubConcepts().get(conceptID);
        List<AmalgamationFct> list = thisconcept.getAvailableAmalgamFcts();
        for(AmalgamationFct fct : list){
            if(fct.getName().contentEquals(amalgamationFunction)){
                thisconcept.deleteAmalgamFct(fct);
                return true;
            }
        }
        return false;

    }


    //Get all concepts
    @ApiOperation(value = "getConcepts", nickname = "getConcepts")
    @RequestMapping(method = RequestMethod.GET, path="/concepts", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ConceptName.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public ConceptName getConcepts() {
        return new ConceptName();
    }

    //Delete all concepts
    @ApiOperation(value = "deleteConcepts", nickname = "deleteConcepts")
    @RequestMapping(method = RequestMethod.DELETE, path="/concepts", produces = "application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteConcepts() {
        Concept superConcept = App.getProject().getSuperConcept();
        return recDeleteConcept(superConcept);
    }

    //recursively deletes a concept and all subconcepts
    private static boolean recDeleteConcept(Concept superConcept){
        HashMap<String,Concept> subConcepts = superConcept.getAllSubConcepts();
        boolean ret = true;
        for(String key : subConcepts.keySet())
            ret = ret && recDeleteConcept(subConcepts.get(key));
        superConcept.getSuperConcept().removeSubConcept(superConcept.getName());
        return ret;
    }

    //Delete one concept
    @ApiOperation(value="deleteConcept", nickname="deleteConcept")
    @RequestMapping(method=RequestMethod.DELETE, value = "/concepts/{concept}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteConcept(@PathVariable(value="concept") String concept,
                                 @RequestParam(value="caseBase", defaultValue="cb") String caseBase){


        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(caseBase))
            return false;
        logger.info("deleting concept with id:"+concept);
        logger.info("concepts:"+p.getSubConcepts().keySet());
        Concept c = p.getSubConcepts().get(concept);
        c.getSuperConcept().removeSubConcept(concept);
        p.save();
        return true;
    }

    //add one concept
    @ApiOperation(value="addConcept", nickname="addConcept")
    @RequestMapping(method=RequestMethod.PUT, value = "/concepts/{concept}")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean addConcept(@PathVariable(value="concept") String concept,
                                 @RequestParam(value="caseBase", defaultValue="cb") String caseBase){
        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(caseBase))
            return false;
        logger.info("creating concept with id:"+concept);
        logger.info("concepts:"+p.getSubConcepts().keySet());
        int concepts = p.getSubConcepts().size();
        if (concepts == 0 )
            createTopConcept(concept,caseBase);
        else if (p.getSuperConcept() == null)
            createTopConcept(concept,caseBase);
        else {
            try {
                Concept c = new Concept(concept,p,p.getSuperConcept());
            } catch (Exception e) {
                logger.error("got exception trying to create concept:" , e);
            }
        }
        return true;
    }

    //get all  attributes
    @ApiOperation(value = "getAttributes", nickname = "getAttributes")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{concept}/attributes", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ApiResponse.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public Attribute getAttributes(@PathVariable(value="conceptID") String conceptID) {
        return new Attribute(conceptID);
    }

    //delete all  attributes
    @ApiOperation(value = "deleteAttributes", nickname = "deleteAttributes")
    @RequestMapping(method = RequestMethod.DELETE, value = "/concepts/{concept}/attributes", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteAttributes(@PathVariable(value="conceptID") String conceptID) {
        Project p = App.getProject();
        Concept c = p.getSubConcepts().get(conceptID);
        for(String attributeDescName : c.getAllAttributeDescs().keySet()) {
            c.removeAttributeDesc(attributeDescName);
        }
        return true;
    }

    //add one attribute
    @ApiOperation(value = "addAttribute", nickname = "addAttribute")
    @RequestMapping(method = RequestMethod.PUT, value = "/concepts/{conceptID}/attributes/{attributeName}", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean addAttribute(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="attributeName") String attributeName,
            @RequestParam(value="attributeType", defaultValue = "String") String attributeType) {
        Project p = App.getProject();
        Concept c = p.getSubConcepts().get(conceptID);

        try {
            if (attributeType.contains("String")) {
                //This attribute registers with the concept through callback!
                new StringDesc(c, attributeName);
            } else if(attributeType.contains("Double")){
                //This attribute registers with the concept through callback!
                new DoubleDesc(c,attributeName,0.0,1.0);
            }
        }catch (Exception e){
            logger.error("got an exception: ",e);
        }
        p.save();
        return true;
    }

    //delete one attribute
    @ApiOperation(value = "deleteAttribute", nickname = "deleteAttribute")
    @RequestMapping(method = RequestMethod.DELETE, value = "/concepts/{conceptID}/attributes/{attributeName}", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteAttribute(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="attributeName") String attributeName,
            @RequestParam(value="attributeType", defaultValue = "String") String attributeType) {
        Project p = App.getProject();
        Concept c = p.getSubConcepts().get(conceptID);

        try {
            c.removeAttributeDesc(attributeName);
        }catch (Exception e){
            logger.error("got an exception: ",e);
            return false;
        }
        p.save();
        return true;
    }

    //Get similarity function for attribute
    @ApiOperation(value = "getSimilarityFunction", nickname = "getSimilarityFunction")
    @RequestMapping(method = RequestMethod.GET, value = "/concepts/{conceptID}/attributes/{attributeName}/similarityFunction", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean getSimilarityFunction(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="attributeName") String attributeName) {
        Project p = App.getProject();
        Concept concept = p.getConceptByID(conceptID);
        /*Collection<AttributeDesc> attributeDescs = concept.getAllAttributeDescs().values();
        for(AttributeDesc attributeDesc : attributeDescs){
            concept.getActiveAmalgamFct().setActiveFct(attributeDesc,null);
        }*/
        AttributeDesc attributeDesc = concept.getAttributeDesc(attributeName);
        concept.getActiveAmalgamFct().getActiveFct(attributeDesc);
        return true;
    }

    //Delete similarity function for attribu5te
    @ApiOperation(value = "deleteSimilarityFunction", nickname = "deleteSimilarityFunction")
    @RequestMapping(method = RequestMethod.DELETE, value = "/concepts/{conceptID}/attributes/{attributeName}/similarityFunction", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean deleteSimilarityFunctions(
            @PathVariable(value="conceptID") String conceptID,
            @PathVariable(value="attributeName") String attributeName) {
        Project p = App.getProject();
        Concept concept = p.getConceptByID(conceptID);
        Collection<AttributeDesc> attributeDescs = concept.getAllAttributeDescs().values();
        AttributeDesc attributeDesc = concept.getAttributeDesc(attributeName);
        concept.getActiveAmalgamFct().setActiveFct(attributeDesc,null);

        return true;
    }

    //Add one similarity function
    @ApiOperation(value = "addSimilarityFunction", nickname = "addSimilarityFunction")
    @RequestMapping(method = RequestMethod.PUT, value = "/concepts/{concept}/attributes/{attributeName}/similarityFunctions/{similarityFunctionName}", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public boolean addSimilarityFunction(
            @PathVariable(value="concept") String concept,
            @PathVariable(value="attributeName") String attributeName,
            @PathVariable(value="similarityFunctionName") String similarityFunctionName,
            @RequestParam(value="caseBase", defaultValue="db") String caseBase,
            @RequestParam(value="parameter", defaultValue="1.0") Double parameter) {
        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(caseBase)){
            return false;
        }
        ICaseBase cb = p.getCaseBases().get(caseBase);
        //p.createTopConcept("heh");
        Concept c = (Concept)p.getSubConcepts().get(concept);
        //TODO need to check the type of attrdesc..
        DoubleDesc attributeDesc = (DoubleDesc) c.getAllAttributeDescs().get(attributeName);
        DoubleFct doubleFct = new DoubleFct(p,attributeDesc,similarityFunctionName);
        doubleFct.setSymmetric(true);
        doubleFct.setFunctionTypeL(NumberConfig.POLYNOMIAL_WITH);
        doubleFct.setFunctionParameterL(parameter);

        return true;
    }


    //helper methods.

    public boolean createTopConcept(String concept,
                                 String caseBase){
        Project p = App.getProject();
        if(!p.getCaseBases().containsKey(caseBase))
            return false;
        try {
            p.createTopConcept(concept);
        } catch (Exception e) {
            logger.error("got exception trying to create top concept:" , e);
        }
        p.save();
        return true;
    }




    /*
     to support recursive concepts:
     @RequestMapping("/{id}/**")
public void foo(@PathVariable("id") int id, HttpServletRequest request) {
   String restOfTheUrl = (String) request.getAttribute(
       HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
   ...
}
     */


    //??
    @ApiOperation(value = "getValueRange", nickname = "getValueRange")
    @RequestMapping(method = RequestMethod.GET, value = "/values", headers="Accept=application/json")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ValueRange.class),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 404, message = "Not Found"),
            @ApiResponse(code = 500, message = "Failure")
    })
    public ValueRange getValueRange(
            @RequestParam(value="concept name", defaultValue="Car") String concept,
            @RequestParam(value="attribute name", defaultValue="Color") String attributeName) {

        return new ValueRange(concept, attributeName);
    }

}
