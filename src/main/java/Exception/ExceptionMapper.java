package Exception;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ExceptionMapper {
    @ServerExceptionMapper
    public RestResponse<Object> mapException(CustomServiceException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("FileName", e.getStackTrace()[0].getFileName());
        errors.put("Error", e.getCause().getMessage());
        if (e.getCause() instanceof NotFoundException)
            return RestResponse.status(Response.Status.NOT_FOUND, errors);
        if (e.getCause() instanceof SQLException)
            return RestResponse.status(Response.Status.SERVICE_UNAVAILABLE, errors);
        return RestResponse.status(Response.Status.BAD_REQUEST, errors);
    }
}
