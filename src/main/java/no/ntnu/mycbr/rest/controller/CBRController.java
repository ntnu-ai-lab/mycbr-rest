package no.ntnu.mycbr.rest.controller;

import no.ntnu.mycbr.core.ICaseBase;
import no.ntnu.mycbr.core.Project;
import no.ntnu.mycbr.core.casebase.Instance;
import no.ntnu.mycbr.core.model.AttributeDesc;
import no.ntnu.mycbr.core.model.Concept;
import no.ntnu.mycbr.core.model.DoubleDesc;
import no.ntnu.mycbr.core.model.StringDesc;
import no.ntnu.mycbr.core.similarity.DoubleFct;
import no.ntnu.mycbr.core.similarity.ISimFct;
import no.ntnu.mycbr.core.similarity.config.NumberConfig;
import io.swagger.annotations.*;
import no.ntnu.mycbr.rest.*;
import no.ntnu.mycbr.rest.utils.CSVTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import javax.xml.ws.Response;
import java.io.InputStream;
import java.util.*;

/**
 * Created by kerstin on 05/08/16.
 */
@RestController
public class CBRController {
    private final Log logger = LogFactory.getLog(getClass());


    /*@ApiOperation(value = "importCsv", nickname = "importCsv")
    @RequestMapping(method = RequestMethod.POST, value = "/importCsv", headers="Accept=application/json", consumes =)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@RequestParam("file") InputStream inputStream,
                           @RequestParam("file") FormDataContentDisposition fileMetaData) {

    }*/
    /*@ApiOperation(value = "importCsv", nickname = "importCsv")
    @Path("/importCsv")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") InputStream inputStream,
                           @FormDataParam("file") FormDataContentDisposition fileMetaData) {
    ...
    }*/

}
