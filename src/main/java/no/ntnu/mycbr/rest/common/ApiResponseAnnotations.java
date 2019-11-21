/**
 * 
 */
package no.ntnu.mycbr.rest.common;

import org.springframework.http.ResponseEntity;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import no.ntnu.mycbr.rest.AmalgamationFunctions;
import no.ntnu.mycbr.rest.Case;
import no.ntnu.mycbr.rest.CaseBases;
import no.ntnu.mycbr.rest.ConceptName;
import no.ntnu.mycbr.rest.Query;
import no.ntnu.mycbr.rest.ValueRange;

/**
 * @author Amar Jaiswal
 * @since Nov 20, 2019
 *
 */
public final class ApiResponseAnnotations {

    public static final String SUCCESS = "Success";	
    public static final String UNAUTHORIZED = "Unauthorized";
    public static final String FORBIDDEN = "Forbidden";	
    public static final String NOT_FOUND = "Not Found";
    public static final String FAILURE = "Failure";  

    // All ApiResponses annotations definitions----------------------------------------------------------------------------------------------------------   

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesDefault{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS, response = Case.class),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesForCase{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS, response = Query.class),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesForQuery{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Success", response = CaseBases.class),
	    @ApiResponse(code = 401, message = "Unauthorized"),
	    @ApiResponse(code = 403, message = "Forbidden"),
	    @ApiResponse(code = 404, message = "Not Found"),
	    @ApiResponse(code = 500, message = "Failure")
    })
    public @interface ApiResponsesForCaseBases{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS, response = AmalgamationFunctions.class),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesForAmalgamationFunctions{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS, response = ConceptName.class),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesForConceptName{}  

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS, response = ValueRange.class),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesForValueRange{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS, response = ApiResponse.class),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE),
    })
    public @interface ApiResponsesForApiResponse{}

    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = "Success", response = ResponseEntity.class),
	    @ApiResponse(code = 401, message = "Unauthorized"),
	    @ApiResponse(code = 403, message = "Forbidden"),
	    @ApiResponse(code = 404, message = "Not Found"),
	    @ApiResponse(code = 500, message = "Failure")
    })
    public @interface ApiResponsesForResponseEntity{}
}
