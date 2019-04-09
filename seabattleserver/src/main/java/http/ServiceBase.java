package http;

import common.MessageLogger;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

public abstract class ServiceBase {
  private static final String url = "localhost";
  private static final int port = 8090;
  private final Yaml yaml = new Yaml();
  protected final MessageLogger messageLogger;

  public ServiceBase(MessageLogger messageLogger) {
    this.messageLogger = messageLogger;
  }

  protected <TRequest, TResponse> QueryResponse<TResponse> executeQuery(TRequest request, Class<TResponse> response, String queryPath, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
    QueryResponse queryResponse = new QueryResponse<>(response);
    final String query = url + queryPath;
    setUri(queryPath, httpEntityEnclosingRequestBase, query);
    httpEntityEnclosingRequestBase.addHeader("Content-Type", "application/x-yaml");
    setRequestEntity(request, queryResponse, httpEntityEnclosingRequestBase);
    messageLogger.info(String.format("[Query %s] : %s", httpEntityEnclosingRequestBase.getMethod(), query));
    final String responseAsString = executeHttpUriRequest(httpEntityEnclosingRequestBase);
    if (responseAsString != null) {
      queryResponse.setSuccess(true);
      queryResponse.setResponse(responseAsString);
    }
    return queryResponse;
  }

  protected void setUri(String queryPath, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase, String query) {
    try {
      // TODO: refactor this!
      URI uri = new URI(query);
      // TODO: also fix for https
      URI uri2 = new URI("http", uri.getUserInfo(), url, port, queryPath, uri.getQuery(), uri.getFragment());
      httpEntityEnclosingRequestBase.setURI(uri2);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
  }

  protected <TRequest> void setRequestEntity(TRequest request, QueryResponse queryResponse, HttpEntityEnclosingRequestBase httpEntityEnclosingRequestBase) {
    StringEntity params;
    try {
      params = new StringEntity(yaml.dump(request));
      params.setContentType("application/x-yaml");
      httpEntityEnclosingRequestBase.setEntity(params);
    } catch (UnsupportedEncodingException ex) {
      queryResponse.setReasonIfFailed(ex.getMessage());
      messageLogger.error(ex.getMessage());
    }
  }

  protected String executeHttpUriRequest(HttpUriRequest httpUriRequest) {
    try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
      CloseableHttpResponse response = httpClient.execute(httpUriRequest);
      messageLogger.info("[Status Line] : " + response.getStatusLine());
      HttpEntity entity = response.getEntity();
      final String entityString = EntityUtils.toString(entity);
      messageLogger.info("[Entity] : " + entityString);
      return entityString;
    } catch (IOException e) {
      messageLogger.error(e.getMessage());
      return null;
    }
  }

}