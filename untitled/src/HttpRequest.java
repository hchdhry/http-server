import java.util.Map;

public class HttpRequest {
    public String method;
    public String Path;
    public Map<String, String> headers;

    public HttpRequest(String method, String path, Map<String, String> headers) {
        this.method = method;
        this.Path = path;
        this.headers = headers;
    }



}
