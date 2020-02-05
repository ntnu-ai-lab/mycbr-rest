/**
 * 
 */
package no.ntnu.mycbr.rest.common;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * This class provides the REST API response annotations, which binds multiple response codes together.
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

    /**
     * The ApiResponseDefault is a default REST API response annotation.
     * 
     */
    @ApiResponses(value = {
	    @ApiResponse(code = 200, message = SUCCESS),
	    @ApiResponse(code = 401, message = UNAUTHORIZED),
	    @ApiResponse(code = 403, message = FORBIDDEN),
	    @ApiResponse(code = 404, message = NOT_FOUND),
	    @ApiResponse(code = 500, message = FAILURE)
    })
    public @interface ApiResponsesDefault{}
}
