/**
 * 
 */
package no.ntnu.mycbr.rest.common;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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
	    @ApiResponse(responseCode = "200", description = SUCCESS),
	    @ApiResponse(responseCode = "401", description = UNAUTHORIZED),
	    @ApiResponse(responseCode = "403", description = FORBIDDEN),
	    @ApiResponse(responseCode = "404", description = NOT_FOUND),
	    @ApiResponse(responseCode = "500", description = FAILURE)
    })
    public @interface ApiResponsesDefault{}
}
