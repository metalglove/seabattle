package http;


import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.util.LinkedHashMap;

public class QueryResponse<TResponse> {
    private static final Yaml yaml = new Yaml();
    private boolean success;
    private TResponse response;
    private Class<TResponse> responseClass;
    private String reasonIfFailed = "";

    public QueryResponse(Class<TResponse> responseClass) {
        this.responseClass = responseClass;
    }

    public void setResponse(String response) {
        yaml.setBeanAccess(BeanAccess.PROPERTY);
        this.response = yaml.loadAs(response, responseClass);
    }

    public TResponse getResponse() {
        if (response instanceof LinkedHashMap) {
            System.out.println("FOUND LinkedHashMap");
        }
        return response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReasonIfFailed() {
        return reasonIfFailed;
    }

    public void setReasonIfFailed(String reasonIfFailed) {
        this.reasonIfFailed = reasonIfFailed;
    }
}
