package com.coinbase.exchange.api.exchange;

import com.coinbase.exchange.api.orders.Order;
import com.coinbase.exchange.security.Signature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.collections.CaseInsensitiveKeyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

/**
 * This class acts as a central point for providing user configuration and making GET/POST/PUT requests as well as
 * getting responses as Lists of objects rather than arrays.
 */
public class CoinbaseExchangeImpl implements CoinbaseExchange {

    static final Logger log = LoggerFactory.getLogger(CoinbaseExchangeImpl.class.getName());
    public static final String COINBASE_RESPONSE_HEADER_BEFORE = "CB-BEFORE";
    public static final String COINBASE_RESPONSE_HEADER_AFTER = "CB-AFTER";

    private final String publicKey;
    private final String passphrase;
    private final String baseUrl;
    private final Signature signature;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public CoinbaseExchangeImpl(final String publicKey,
                                final String passphrase,
                                final String baseUrl,
                                final Signature signature,
                                final ObjectMapper objectMapper) {
        this.publicKey = publicKey;
        this.passphrase = passphrase;
        this.baseUrl = baseUrl;
        this.signature = signature;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public <T> T get(String resourcePath, ParameterizedTypeReference<T> responseType) {
        return getWithHeaders(resourcePath, responseType, null);
    }

    public <T> T getWithHeaders(String resourcePath, ParameterizedTypeReference<T> responseType,
                                Map<String, String> responseHeaders) {
        return getWithHeaders(resourcePath, responseType, responseHeaders, 100);
    }
    public <T> T getWithHeaders(String resourcePath, ParameterizedTypeReference<T> responseType,
                                Map<String, String> responseHeaders, Integer limit) {
        try {
            final String fullPath = getBaseUrl() + resourcePath + (limit != null ? ("?limit=" + limit) : "");
            ResponseEntity<T> responseEntity = restTemplate.exchange(getBaseUrl() + resourcePath,
                    HttpMethod.GET,
                    securityHeaders(resourcePath,
                            "GET",
                            ""),
                    responseType);
            if (null != responseHeaders) {
                responseHeaders.putAll(responseEntity.getHeaders().toSingleValueMap());
            }
            return responseEntity.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("GET request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());
        }
        return null;
    }

    @Override
    public <T> List<T> getAsList(String resourcePath, ParameterizedTypeReference<T[]> responseType) {
       return getAsListWithHeaders(resourcePath, responseType, null);
    }

    public <T> List<T> getAsListWithHeaders(String resourcePath, ParameterizedTypeReference<T[]> responseType,
                                            Map<String, String> responseHeaders) {
        T[] result = getWithHeaders(resourcePath, responseType, responseHeaders);

        return result == null ? emptyList() : Arrays.asList(result);
    }

    public <T> T pagedGetWithHeaders(String resourcePath,
                          ParameterizedTypeReference<T> responseType,
                          String beforeOrAfter,
                          Map<String, String> previousResponseHeaders,
                          Map<String, String> newResponseHeaders,
                          Integer limit) {
            resourcePath += (resourcePath.contains("?") ? "&" : "?") + "limit=" + limit;

        if (!beforeOrAfter.equalsIgnoreCase("start")) {
            final CaseInsensitiveKeyMap<String> caseInsensitiveResponseHeaders = new CaseInsensitiveKeyMap<>();
            caseInsensitiveResponseHeaders.putAll(previousResponseHeaders);
            final String headerKey = beforeOrAfter.equalsIgnoreCase("before") ?
                    COINBASE_RESPONSE_HEADER_BEFORE : COINBASE_RESPONSE_HEADER_AFTER;
            final String pageNumber = caseInsensitiveResponseHeaders.get(headerKey);
            //resourcePath += "?" + beforeOrAfter + "=" + pageNumber
            resourcePath += "&" + beforeOrAfter + "=" + pageNumber;
        }
        return getWithHeaders(resourcePath, responseType, newResponseHeaders);
    }

    public <T> List<T> pagedGetAsListWithHeaders(String resourcePath,
                             ParameterizedTypeReference<T> responseType,
                             String beforeOrAfter,
                             Map<String, String> previousResponseHeaders,
                             Map<String, String> newResponseHeaders,
                             Integer limit) {
        T[] result = (T[]) pagedGetWithHeaders(resourcePath, responseType, beforeOrAfter, previousResponseHeaders,
                newResponseHeaders, limit );
        return result == null ? emptyList() : Arrays.asList(result);
    }

    @Override
    public <T> T pagedGet(String resourcePath,
                          ParameterizedTypeReference<T> responseType,
                          String beforeOrAfter,
                          Integer pageNumber,
                          Integer limit) {
        resourcePath += "?" + beforeOrAfter + "=" + pageNumber + "&limit=" + limit;
        return get(resourcePath, responseType);
    }

    @Override
    public <T> List<T> pagedGetAsList(String resourcePath,
                          ParameterizedTypeReference<T[]> responseType,
                          String beforeOrAfter,
                          Integer pageNumber,
                          Integer limit) {
        T[] result = pagedGet(resourcePath, responseType, beforeOrAfter, pageNumber, limit );
        return result == null ? emptyList() : Arrays.asList(result);
    }

    @Override
    public <T> T delete(String resourcePath, ParameterizedTypeReference<T> responseType) {
        try {
            ResponseEntity<T> response = restTemplate.exchange(getBaseUrl() + resourcePath,
                HttpMethod.DELETE,
                securityHeaders(resourcePath, "DELETE", ""),
                responseType);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("DELETE request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());
        }
        return null;
    }

    @Override
    public <T, R> T post(String resourcePath,  ParameterizedTypeReference<T> responseType, R jsonObj) {
        String jsonBody = toJson(jsonObj);

        try {
            ResponseEntity<T> response = restTemplate.exchange(getBaseUrl() + resourcePath,
                    HttpMethod.POST,
                    securityHeaders(resourcePath, "POST", jsonBody),
                    responseType);
            return response.getBody();
        } catch (HttpClientErrorException ex) {
            log.error("POST request Failed for '" + resourcePath + "': " + ex.getResponseBodyAsString());
        }
        return null;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public HttpEntity<String> securityHeaders(String endpoint, String method, String jsonBody) {
        HttpHeaders headers = new HttpHeaders();

        String timestamp = Instant.now().getEpochSecond() + "";
        String resource = endpoint.replace(getBaseUrl(), "");

        headers.add("accept", "application/json");
        headers.add("content-type", "application/json");
        headers.add("User-Agent", "gdax-java unofficial coinbase pro api library");
        headers.add("CB-ACCESS-KEY", publicKey);
        headers.add("CB-ACCESS-SIGN", signature.generate(resource, method, jsonBody, timestamp));
        headers.add("CB-ACCESS-TIMESTAMP", timestamp);
        headers.add("CB-ACCESS-PASSPHRASE", passphrase);

        curlRequest(method, jsonBody, headers, resource);

        return new HttpEntity<>(jsonBody, headers);
    }

    /**
     * Purely here for logging an equivalent curl request for debugging
     * note that the signature is time-sensitive and has a time to live of about 1 minute after which the request
     * is no longer valid.
     */
    private void curlRequest(String method, String jsonBody, HttpHeaders headers, String resource) {
        String curlTest = "curl ";
        for (String key : headers.keySet()){
            curlTest +=  "-H '" + key + ":" + headers.get(key).get(0) + "' ";
        }
        if (jsonBody!=null && !jsonBody.equals(""))
            curlTest += "-d '" + jsonBody + "' ";

        curlTest += "-X " + method + " " + getBaseUrl() + resource;
        log.debug(curlTest);
    }

    private String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Unable to serialize", e);
            throw new RuntimeException("Unable to serialize");
        }
    }

    public <T> ParameterizedTypeReference<T> getParameterizedTypeReferenceForType(T obj) {
        return new ParameterizedTypeReference<T>(){};
    }

    public ParameterizedTypeReference<Order> getParameterizedTypeReferenceForOrder() {
        return new ParameterizedTypeReference<Order>(){};
    }

    public ParameterizedTypeReference<Order[]> getParameterizedTypeReferenceForOrderArray() {
        return new ParameterizedTypeReference<Order[]>(){};
    }
}
