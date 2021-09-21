package com.ing.interview.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.interview.objects.ApplicationMessage;
import com.ing.interview.objects.JsonFullCarMessage;
import com.ing.interview.objects.JsonFullCarMessage.Response;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import springfox.documentation.spring.web.json.Json;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class CarMessageUtil {
    public static final String SET_PREFIX;
    public static final String GET_PREFIX;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final HttpHeaders headers = new HttpHeaders();
    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Map<Class<?>, List<Method>> cache = new HashMap<>();

    static {
        GET_PREFIX="get";
        SET_PREFIX="set";
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    private CarMessageUtil(){}

    public static HttpEntity<String> toHttpEntity(JsonFullCarMessage jsonFullCarMessage) throws JsonProcessingException {
        return new HttpEntity<>(objectMapper.writeValueAsString(jsonFullCarMessage), headers);
    }

    public static boolean successMessage(JsonFullCarMessage jsonFullCarMessage){
        if(isNull(jsonFullCarMessage)){
            return false;
        }
        return jsonFullCarMessage.getResponse().getCode()==null;
    }

    public static void setUnexpectedError(JsonFullCarMessage jsonFullCarMessage, String service){
        setApplicationError(jsonFullCarMessage, ApplicationMessage.UNEXPECTED, service);
    }

    public static void setApplicationError(JsonFullCarMessage jsonFullCarMessage, ApplicationMessage applicationMessage, String service){
        if(isNull(jsonFullCarMessage.getResponse())){
            jsonFullCarMessage.setResponse(new JsonFullCarMessage.Response());
        }

        Response response=jsonFullCarMessage.getResponse();
        response.setCode(applicationMessage.getCode());
        response.setMessage(applicationMessage.getMessage());
        response.setSourceService(service);
    }

    public static void validate(JsonFullCarMessage jsonListoFullMessage) {
        Set<ConstraintViolation<JsonFullCarMessage>> violations = validator.validate(jsonListoFullMessage);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> T mergeWith(T baseObject, T sourceObject) throws JsonProcessingException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(isNull(sourceObject)){
            return baseObject;
        }
        if(isNull(baseObject)){
            baseObject = (T) sourceObject.getClass().getConstructor().newInstance();
        }

        putClassToCache(sourceObject);

        List<Method> methods = Arrays.asList(sourceObject.getClass().getMethods());

        for (Method method : cache.get(sourceObject.getClass())) {
            Method getMethod = getMethod(method, sourceObject);
            if(isGetMethodConditionsAttended(getMethod, sourceObject)){
                invokeMethod(getMethod, method, sourceObject, baseObject);
            }
        }
        return baseObject;
    }

    private static <T> boolean isGetMethodConditionsAttended(Method getMethod, T sourceObject) throws InvocationTargetException, IllegalAccessException {
        return(nonNull(getMethod.invoke(sourceObject)) && !(Collections.class.isAssignableFrom(getMethod.getReturnType()) && ((Collection<?>)getMethod.invoke(sourceObject)).isEmpty()));
    }

    private static <T> Method getMethod(Method setMethod, T sourceObject) throws NoSuchMethodException {
        return sourceObject.getClass().getMethod(GET_PREFIX + setMethod.getName().substring(3));
    }

    private static boolean isVoidReturnAndIsSet(Method setMethod){
        return(void.class.equals(setMethod.getReturnType()) && (setMethod.getName().length() > 3 && setMethod.getName().startsWith(SET_PREFIX)));
    }

    private static <T> void putClassToCache(T sourceObject){
        if(!cache.containsKey(sourceObject.getClass())){
            cache.put(sourceObject.getClass(), Arrays.stream(sourceObject.getClass().getMethods()).filter(CarMessageUtil::isVoidReturnAndIsSet).collect(Collectors.toList()));
        }
    }

    private static <T> T invokeMethod(Method getMethod, Method setMethod, T sourceObject, T baseObject) throws InvocationTargetException, IllegalAccessException, JsonProcessingException, NoSuchMethodException, InstantiationException {
        if(JsonFullCarMessage.class.equals(getMethod.getReturnType().getEnclosingClass())){
            Object object = getMethod.invoke(baseObject);
            object = mergeWith(object, getMethod.invoke(sourceObject));
            setMethod.invoke(baseObject, object);
        }
        else{
            setMethod.invoke(baseObject, getMethod.invoke(sourceObject));
        }
        return baseObject;
    }
}
