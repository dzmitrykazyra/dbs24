/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.rest.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.client.Client;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import com.kdg.rest.exception.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.LinkedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ws.rs.core.MediaType;
import com.kdg.fs24.application.core.log.LogService;
import com.kdg.fs24.rest.exception.InvalidResponseException;
import com.kdg.fs24.rest.exception.FileUploadException;
import java.time.LocalDateTime;
import com.kdg.fs24.application.core.sysconst.SysConst;
import com.kdg.fs24.application.core.nullsafe.StopWatcher;
import java.util.stream.Collectors;
import java.util.Timer;
import java.util.TimerTask;
import javax.annotation.PreDestroy;

//import com.google.gson.Gson;
//import java.lang.reflect.Type;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;
import com.kdg.fs24.application.core.nullsafe.NullSafe;
import org.springframework.beans.factory.annotation.Value;
import lombok.Data;

//==============================================================================
@Data
public abstract class RestProcessor {// extends ObjectRoot {

    @Value("${reactive.rest.debug:false}")
    private Boolean restDebug = SysConst.BOOLEAN_FALSE;

    // коллекция подтвердающих сообщений
    protected final static Collection<ReqInfo> REQUESTS_LIST = ServiceFuncs.<ReqInfo>createCollection();

    private String targetServer;
    private String serverReadTimeout;
    private String serverConnectTimeout;
    //==========================================================================
    private static TimerTask reqListTask = null;
    private static Timer reqListTimer = null;

    public RestProcessor() {
        super();

//        this.LOAD_FROM_DB = LogService.getWarPackageName().endsWith(SysConst.LOG_PKG_NAME);
//        LogService.LogInfo(this.getClass(), () -> String.format("USE_DB_SOURCE = %b, hc = %d, Class = %s",
//                this.LOAD_FROM_DB,
//                this.hashCode(),
//                this.getClass().getCanonicalName()));
//
        if (!LogService.getWarPackageName().endsWith(SysConst.LOG_PKG_NAME)) {

            // сервис для подтверждения rest-запросов
            final long timerDelay = 2000;
            final long timerResend = 8000;

            RestProcessor.reqListTask = new TimerTask() {
                @Override
                public void run() {
                    //LogService.LogInfo(this.getClass(), LogService.getCurrentObjProcName(this));
                    RestProcessor.this.sendReqList();
                }
            };

            this.reqListTimer = NullSafe.createObject(Timer.class);
            this.reqListTimer.schedule(this.reqListTask, timerDelay, timerResend);
        }
    }

    //==========================================================================
    protected abstract void initRestService();

    //==========================================================================
    public <K extends Object, V extends Object> Response postMap(final Map<K, V> map, final String path, final Boolean useGZIP) {

        if (NullSafe.isNull(getTargetServer())) {
            this.initRestService();
        }

        return this.postMap(map, getTargetServer(), path, getServerReadTimeout(), getServerConnectTimeout(), HttpConst.HTTP_200_OK, useGZIP);

    }

    //==========================================================================
    protected <T extends Object> Collection<T> executeGetRequest4List(
            final String server,
            final String path,
            final Class<T> resultClass) throws InvalidResponseException {
        final Response response = buildRequest(server, path).get(Response.class);

        return NullSafe.create()
                .execute2result(() -> {
                    return parseResponse2Collection(response, resultClass);
                })
                .finallyBlock(() -> {
                    response.close();
                })
                .<Collection<T>>getObject();

    }

    //==========================================================================
    protected Map<String, String> executeGetRequest4Map(
            final String server,
            final String path) throws InvalidResponseException {
        final Response response = buildRequest(server, path).get(Response.class);

        return NullSafe.create()
                .execute2result(() -> {
                    return parseResponse2Map(response);
                })
                .finallyBlock(() -> {
                    response.close();
                })
                .<Map<String, String>>getObject();
    }

    //==========================================================================
    protected Invocation.Builder buildRequest(final String targetServer,
            final String path,
            final String serverReadTimeout,
            final String serverConnectTimeout,
            final Boolean useGZIP) {

        //String targetServer = SysConst.EMPTY_STRING;
        String dataTransferFormat = HttpConst.APP_JSON;

//        if (NullSafe.isNull(serverReadTimeout)) {
//            serverReadTimeout = HttpConst.SRV_READ_TIMEOUT;
//        }
//
//        if (NullSafe.isNull(serverConnectTimeout)) {
//            serverConnectTimeout = HttpConst.SRV_CONNECT_TIMEOUT;
//        }
        final String acceptEncoding = HttpConst.GZIP_ACCEPT_ENCODING;
        final String contentEncoding = HttpConst.IDENTITY_CONTENT_ENCODING;
        final String contentType = HttpConst.CONTENT_TYPE;

        final Client client = new ResteasyClientBuilder()
                .connectTimeout(NullSafe.<Long>getSafeNumeric(serverConnectTimeout), TimeUnit.MILLISECONDS)
                .readTimeout(NullSafe.<Long>getSafeNumeric(serverReadTimeout), TimeUnit.MILLISECONDS)
                .build();

        final Invocation.Builder req = client
                .target(targetServer)
                .path(path)
                .request(dataTransferFormat)
                //                .header(HttpHeaders.ACCEPT_ENCODING, acceptEncoding)
                .header(HttpHeaders.CONTENT_ENCODING, contentEncoding)
                .header(HttpHeaders.CONTENT_TYPE, contentType);

        if (useGZIP) {
            req.header(HttpHeaders.ACCEPT_ENCODING, acceptEncoding);
        }

        return req;
    }

    //==========================================================================
    protected Invocation.Builder buildRequest(final String targetServer, final String path) {
        return buildRequest(targetServer, path, HttpConst.SRV_READ_TIMEOUT, HttpConst.SRV_CONNECT_TIMEOUT, HttpConst.USE_GZIP_ENCODING);
    }

    //==========================================================================
    protected Invocation.Builder buildMultiPartRequest(final String targetServer, final String path) {

        //String targetServer = SysConst.EMPTY_STRING;
        final String dataTransferFormat = HttpConst.APP_JSON;
        final String acceptEncoding = HttpConst.GZIP_ACCEPT_ENCODING;

        return NullSafe.createObject(ResteasyClientBuilder.class)
                .connectTimeout(NullSafe.<Long>getSafeNumeric(this.serverConnectTimeout), TimeUnit.MILLISECONDS)
                .readTimeout(NullSafe.<Long>getSafeNumeric(this.serverReadTimeout), TimeUnit.MILLISECONDS)
                .build()
                .target(targetServer)
                .path(path)
                .request(dataTransferFormat)
                .header(HttpHeaders.ACCEPT_ENCODING, acceptEncoding)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA);

    }

    //==========================================================================
    protected <T> Collection<T> parseResponse2Collection(
            final Response response,
            final Class<T> resultClass) throws InvalidResponseException {

        if (!HttpConst.HTTP_200_OK.equals(response.getStatus())) {
            throw new InvalidResponseException(String.format("response.Status = %s", response.getStatus()));
        }

        return NullSafe.create()
                .execute2result(() -> {

                    final ObjectMapper mapper = NullSafe.createObject(ObjectMapper.class);
                    //JavaType type = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, Collection.class, resultClass);
                    JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, resultClass);
                    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                    // игнорируем ненужные поля
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);

                    return mapper.readValue(response.readEntity(String.class), type);

                }).<Collection<T>>getObject();
    }

    //==========================================================================
    protected Map<String, String> parseResponse2Map(
            final Response response) throws InvalidResponseException {

        if (!HttpConst.HTTP_200_OK.equals(response.getStatus())) {
            throw new InvalidResponseException(String.format("response.Status = %s", response.getStatus()));
        }

        return NullSafe.create()
                .execute2result(() -> {

                    return getMapFromJson(response.readEntity(String.class));

                })
                .<Map<String, String>>getObject();
    }

    //==========================================================================
    protected String parseResponse2String(
            final Response response) throws InvalidResponseException {

        if (!HttpConst.HTTP_200_OK.equals(response.getStatus())) {
            throw new InvalidResponseException(String.format("response.Status = %s", response.getStatus()));
        }

        return NullSafe.create()
                .execute2result(() -> {

                    return response.readEntity(String.class);

                })
                .<String>getObject();
    }

    //==========================================================================    
    protected Map<String, String> getMapFromJson(final String json) {

        return NullSafe.create()
                .execute2result(() -> {
                    final ObjectMapper mapper = NullSafe.createObject(ObjectMapper.class);
                    //JavaType type = mapper.getTypeFactory().constructParametrizedType(LinkedHashMap.class, Map.class, String.class, String.class);
                    final JavaType type = mapper.getTypeFactory().constructMapLikeType(LinkedHashMap.class, String.class, String.class);
                    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

                    // игнорируем ненужные поля
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);

                    return mapper.readValue(json, type);
                })
                .<Map<String, String>>getObject();
    }

    //==========================================================================    
    public <T extends Object> Collection<T> getObjectListFromJson(
            final String json,
            final Class<T> clazz) {

        return NullSafe.create()
                .execute2result(() -> {
                    final ObjectMapper mapper = NullSafe.createObject(ObjectMapper.class).findAndRegisterModules();
                    //JavaType mapType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, Collection.class, clazz);
                    final JavaType mapType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
                    // игнорируем ненужные поля
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);

                    return mapper.readValue(json, mapType);
                }).<Collection<T>>getObject();

    }

    //==========================================================================    
    public static <T extends Object> Collection<T> getStaticObjectListFromJson(
            final String json,
            final Class<T> clazz) {
        return NullSafe.create()
                .execute2result(() -> {
                    final ObjectMapper mapper = NullSafe.createObject(ObjectMapper.class).findAndRegisterModules();
                    //JavaType mapType = mapper.getTypeFactory().constructParametrizedType(ArrayList.class, Collection.class, clazz);
                    final JavaType mapType = mapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);

                    // игнорируем ненужные поля
                    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, Boolean.FALSE);

                    return mapper.readValue(json, mapType);
                }).<Collection<T>>getObject();
    }

    public String getJsonRawFromConfig(final Map<String, String> config) throws JSONConversionError {

        final String jsonRaw = (String) NullSafe.create()
                .execute2result(() -> {

                    // конвертировали в json и отдали
                    return new ObjectMapper().writeValueAsString(config);
                }).<String>getObject();

        return NullSafe.create(jsonRaw)
                .whenIsNull(() -> {
                    return "json conversion error";
                }).<String>getObject();

    }

    //==========================================================================    
    public <T extends Object> String getJsonRawFromList(final Collection<T> list) throws JSONConversionError {

        String jsonRaw = (String) NullSafe.create()
                .execute2result(() -> {

                    // конвертировали в json и отдали
                    return new ObjectMapper().writeValueAsString(list);
                }).<T>getObject();

        return NullSafe.create(jsonRaw)
                .whenIsNull(() -> {
                    return "json conversion error";
                }).<String>getObject();

    }

    //==========================================================================    
//    public <T extends Object> String getGsonFromList(List<T> list, Type type) throws JSONConversionError {
//
//        String jsonRaw = "json conversion error";
//
//        try {
//            // конвертировали в json и отдали
//            jsonRaw = new Gson().toJson(list, type);
//        } catch (Throwable e) {
//            String msg = String.format("Ошибка конвертации JSON (%s)", NullSafe.getErrorMessage(e));
//            LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), msg);
//            throw new JSONConversionError(msg);
//        }
//
//        return jsonRaw;
//    }
    //==========================================================================
    public <T extends Object> Response postMessages(
            final Collection<T> msgs,
            final String targetServer,
            final String path,
            final String serverReadTimeout,
            final String serverConnectTimeout,
            final Integer requiredStatus) {

        final String reqAddress = String.format("%s%s",
                targetServer,
                path);

        return (Response) NullSafe.create()
                .execute2result(() -> {

                    final String jsonMsg = this.processValueString(new ObjectMapper().writeValueAsString(msgs));

                    return this.executeRequest(javax.ws.rs.POST.class.getCanonicalName(),
                            reqAddress,
                            jsonMsg.hashCode(),
                            () -> {

                                return buildRequest(
                                        targetServer,
                                        path,
                                        serverReadTimeout,
                                        serverConnectTimeout,
                                        HttpConst.USE_GZIP_ENCODING)
                                        .post((Entity) Entity.json(jsonMsg));
                            });
                })
                .safeExecute((nullSafeResponse) -> {
                    final Response response = (Response) nullSafeResponse;
                    NullSafe.create()
                            .execute(() -> {
                                if (!Integer.valueOf(response.getStatus()).equals(requiredStatus)) {
//                        LogService.LogErr(response.getClass(), String.format("%s exception (%d): %s%s: %s",
//                                LogService.getCurrentObjProcName(this),
//                                Integer.valueOf(response.getStatus()),
//                                targetServer,
//                                path,
//                                response.getStatusInfo().getReasonPhrase()));
                                    throw new InvalidResponseException(String.format("%s exception (response status = %d when required %d)\n "
                                            + "address is %s%s: %s",
                                            LogService.getCurrentObjProcName(this),
                                            Integer.valueOf(response.getStatus()),
                                            Integer.valueOf(requiredStatus),
                                            targetServer,
                                            path,
                                            response.getStatusInfo().getReasonPhrase()));
                                }
                            })
                            .registerOuterException(new InvalidResponseException(String.format("can't post messages to %s%s", targetServer, path)))
                            .finallyBlock(() -> {
                                response.close();
                            });
                })
                //.registerOuterException(new InvalidResponseException(String.format("can't post messages to %s%s", targetServer, path)))
                .<T>getObject();
    }

    //==========================================================================
    public <K extends Object, V extends Object> Response postMap(
            final Map<K, V> map,
            final String targetServer,
            final String path,
            final String serverReadTimeout,
            final String serverConnectTimeout,
            final Integer requiredStatus,
            final Boolean useGZIP) {

        final String reqAddress = String.format("%s%s",
                targetServer,
                path);

        return NullSafe.create()
                .execute2result(() -> {

                    final String jsonMsg = this.processValueString(new ObjectMapper().writeValueAsString(map));

                    return this.executeRequest(javax.ws.rs.POST.class.getCanonicalName(),
                            reqAddress,
                            jsonMsg.hashCode(),
                            () -> {

                                return buildRequest(
                                        targetServer,
                                        path,
                                        serverReadTimeout,
                                        serverConnectTimeout,
                                        useGZIP)
                                        .post((Entity) Entity.json(jsonMsg));

                            });
                })
                .safeExecute((nullSafeResponse) -> {
                    final Response response = (Response) nullSafeResponse;
                    NullSafe.create()
                            .execute(() -> {
                                if (!Integer.valueOf(response.getStatus()).equals(requiredStatus)) {

                                    throw new InvalidResponseException(String.format("%s exception (response status = %d when required %d)\n "
                                            + "address is %s%s: %s",
                                            LogService.getCurrentObjProcName(this),
                                            Integer.valueOf(response.getStatus()),
                                            Integer.valueOf(requiredStatus),
                                            targetServer,
                                            path,
                                            response.getStatusInfo().getReasonPhrase()));
                                }
                            })
                            .registerOuterException(new InvalidResponseException(String.format("can't post messages to %s%s", targetServer, path)))
                            .finallyBlock(() -> {
                                //response.close();
                            });
                })
                //.registerOuterException(new InvalidResponseException(String.format("can't post messages to %s%s", targetServer, path)))
                .<Response>getObject();

    }

    //==========================================================================
    protected String processValueString(final String json) {
        //return this.getRegParam("encodeRestJson", "0").equals("1") ? this.encryptString(json) : json;
        //return this.encryptString(json);
        return (json);
    }
    //==========================================================================
    private final String MP_KEY_NAME = "ppoFile";

    public Response uploadFile(MultipartFormDataInput input, final String uploadDirectory) {

        String fileName = SysConst.EMPTY_STRING;

        Map<String, List<InputPart>> formParts = input.getFormDataMap();

        List<InputPart> inPart = formParts.get(MP_KEY_NAME);
        Response response = null;

        for (InputPart inputPart : inPart) {

            try {

                // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
                MultivaluedMap<String, String> headers = inputPart.getHeaders();
                fileName = parseFileName(headers);

                // Handle the body of that part with an InputStream
                InputStream istream = inputPart.getBody(InputStream.class, null);

                // создаем директорию, если не существует
                Files.createDirectories(Paths.get(uploadDirectory));

                fileName = uploadDirectory + "\\" + fileName;

                saveFile(istream, fileName);

                istream.close();

            } catch (IOException e) {
                LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), e);
                response = Response.serverError().entity(NullSafe.getErrorMessage(e)).status(HttpConst.HTTP_500_INTERNAL_ERROR).build();

                e.printStackTrace();
            }
        }

        return (Response) NullSafe.nvl(response, Response.ok().entity("File saved to server location : " + fileName).build());

    }
    //Parse Content-Disposition header to get the original file name

    private String parseFileName(MultivaluedMap<String, String> headers) {

        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

        for (final String name : contentDispositionHeader) {

            if ((name.trim().startsWith("filename"))) {

                String[] tmp = name.split("=");
                String fileName = tmp[1].trim().replaceAll("\"", SysConst.EMPTY_STRING);

                return fileName;
            }
        }

        return "randomName";
    }
    //save uploaded file to a defined location on the server

    private void saveFile(InputStream uploadedInputStream,
            String serverLocation) {

        try {

            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));

            int read = 0;

            byte[] bytes = new byte[1024];
            outpuStream = new FileOutputStream(new File(serverLocation));

            while ((read = uploadedInputStream.read(bytes)) != -1) {

                outpuStream.write(bytes, 0, read);

            }

            outpuStream.flush();
            outpuStream.close();

        } catch (IOException e) {
            LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), e);
            e.printStackTrace();

        }

    }

    //==============================================================================
    public Boolean upload2Rest(final String httpURL, final String httpPath, byte[] fileContent, final String fileName) throws IOException, FileUploadException {

        Boolean result = Boolean.FALSE;

        try {
            final MultipartFormDataOutput multipartFormDataOutput = new MultipartFormDataOutput();
            multipartFormDataOutput.addFormData(MP_KEY_NAME, fileContent, MediaType.MULTIPART_FORM_DATA_TYPE, fileName);

            final GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipartFormDataOutput) {
            };

            final Response response = buildMultiPartRequest(httpURL, httpPath).post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA_TYPE));

            result = (Integer.valueOf(response.getStatus()).equals(HttpConst.HTTP_200_OK));

            if (!result) {

                throw new FileUploadException(String.format("Failed upload (%d, %s) ",
                        response.getStatus(),
                        response.getStatusInfo().getReasonPhrase()));
            }

        } catch (Exception ex) {
            LogService.LogErr(this.getClass(), LogService.getCurrentObjProcName(this), ex);
            throw new FileUploadException(NullSafe.getErrorMessage(ex));
        }

        return result;

    }
    //==========================================================================
    private static int encodeKey = 0;

    public String encryptString(final String toEncode) {
        char[] chars = toEncode.toCharArray();
        int i, j, k;

        if (encodeKey == 0) {
            //обрезаем до byte
            //encodeKey = this.getRegParam("encodeKey", "192837465").hashCode() & 0x00000000000000000000000011111111;
            encodeKey = "192837465".hashCode() & 0x00000000000000000000000011111111;
        }
        j = encodeKey;
        //j = keyEncode;
        //j = 0x10011001;

        for (i = 0; i < chars.length - 1; i++) {
            chars[i] = (char) (((int) (chars[i])) ^ j);

            k = (j & 0x00000001);
            j = j >>> 1;

            // перевод последнего бита вперед
            if (k > 0) {
                j = j | 0x10000000;
            }
        }
        return String.valueOf(chars);
    }
    ////////////////////////////////////////////////////////////////////////////
    private final Map<String, String> REGISTRY_PARAMS = ServiceFuncs.<String, String>getOrCreateMap(ServiceFuncs.MAP_NULL);
    private Boolean LOAD_FROM_DB;

    //==========================================================================
//    private String internalGetRegParam(final String paramName, final String defValue) {
//
//        String paramValue = defValue;
//
//        synchronized (REGISTRY_PARAMS) {
//
//            // ищем в кэше
//            final Map<String, String> mapExistKey
//                    = (REGISTRY_PARAMS.entrySet())
//                            .stream()
//                            .unordered()
//                            .filter(p -> p.getKey().equals(paramName))
//                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
//
//            // параметр не найден, читаем из БД
//            if (!mapExistKey.isEmpty()) {
//                //paramValue = (String) mapExistKey.get(0).trim();
//                for (Map.Entry<String, String> ep : mapExistKey.entrySet()) {
//                    paramValue = ep.getValue();
//                    break;
//                }
//
//            } else {
//
//                // прочитать настройки из БД
//                if (LOAD_FROM_DB) {
//
//                    paramValue = NullSafe.create()
//                            .execute2result(() -> {
//                                return ServiceLocator
//                                        .find(AbstractJdbcService.class)
//                                        .createCallQuery("{:RES = call vcs_get_reg_param_value_ext(:P)}")
//                                        .setParamByNameAsOutput("RES", SysConst.EMPTY_STRING)
//                                        .setParamByName("P", paramName)
//                                        .<String>getSingleFieldValue();
//
//                            }).<String>getObject();
//
//                    if (paramValue.isEmpty()) {
//                        paramValue = defValue;
//                    }
//
//                } else {
//                    // получить через RestService
//                    final Map<String, String> map = ServiceFuncs.<String, String>getOrCreateMap(ServiceFuncs.MAP_NULL);
//                    map.put(paramName, defValue);
//
//                    paramValue = NullSafe.create(LogService.getCurrentObjProcName(String.format("%s_%s ", LogService.getCurrentObjProcName(this), paramName)))
//                            .execute2result(() -> {
//                                // посылаем рест запрос для получения значения параметра
//
//                                return this.<String, String>postMap(map, LogService.REG_PARAM, HttpConst.NO_GZIP_ENCODING);
//                            })
//                            .safeExecute2result((nullSafeResponse) -> {
//                                final Response response = (Response) nullSafeResponse;
//
//                                final String responceBody = response.readEntity(String.class);
//
//                                response.close();
//
//                                //final String pmValue = this.processValueString(responceBody);
//                                LogService.LogInfo(response.getClass(), () -> String.format("Getting parameter %s :: [Body = '%s']",
//                                        paramName, responceBody));
//
//                                return responceBody;
//                            }).<String>getObject();
//                }
//
//                // сохранили в кэше
//                REGISTRY_PARAMS.put(paramName, paramValue);
//
//            }
//        }
//
//        return paramValue;
//    }
    //==========================================================================
//    public synchronized String getRegParam(final String paramName, final String defValue) {
//
//        return this.internalGetRegParam(paramName, defValue);
//
//    }
//
//    //==========================================================================
//    public synchronized Integer getRegParam(final String paramName, final Integer defValue) {
//
//        Integer paramValue = defValue;
//
//        paramValue = NullSafe.create()
//                .execute2result(() -> {
//                    return Integer.valueOf(this.internalGetRegParam(paramName, defValue.toString()));
//                }).catchException(e -> {
//            LogService.LogErr(this.getClass(),
//                    () -> String.format("%s: Can't read '%s' parameter (%s) ",
//                            LogService.getCurrentObjProcName(this),
//                            paramName,
//                            e.getMessage()));
//        })
//                .<Integer>getObject();
//
//        return paramValue;
//    }
    //==========================================================================
    protected void registerRequestsExt(
            final String json) {

        NullSafe.runNewThread(() -> {
            synchronized (RestProcessor.class) {
                final ReqInfo reqInfo = new ReqInfo();

                reqInfo.setReqJson(json);
            }
        });
    }

    //==========================================================================
    // выполнить запрос
    protected Response executeRequest(
            final String reqType,
            final String reqAddress,
            final int hashCode,
            final RequestRunner rr) {

        //LogService.LogInfo(this.getClass(), String.format("execute request 2 '%s'", reqAddress));
        return NullSafe.create()
                .execute2result(() -> {
                    final StopWatcher stopWatcher = StopWatcher.create(reqAddress);
                    // выполняем сам запрос
                    final Response response = rr.execRequest();

                    if (!reqAddress.endsWith(LogService.REGISTER_REQ)) {
                        // добавляем в отдельном потоке
                        NullSafe.runNewThread(() -> {

                            synchronized (REQUESTS_LIST) {
                                REQUESTS_LIST.add(new ReqInfo()
                                        .setReqType(reqType)
                                        .setReqAddress(reqAddress)
                                        .setReqAnswer(String.format("Status=%d, ReasonPhrase='%s'",
                                                response.getStatus(),
                                                response.getStatusInfo().getReasonPhrase()))
                                        .setReqDuration((int) stopWatcher.getExecutionTime())
                                        .setReqHashCode(hashCode)
                                        .setIsConfirmed(SysConst.BOOLEAN_TRUE)
                                        .setReqIp(SysConst.APPLICATION_ADDRESS)
                                );
//                            LogService.LogInfo(this.getClass(), String.format("reqList count = %s",
//                                    REQ_LIST.size()));
                            }
                        });
                    }
                    return response;

                }).<Response>getObject();
    }

    //==========================================================================
    protected <T> Response processRequest(
            final Class<T> reqClass,
            final int reqHashCode,
            final String jsonMessage,
            final RequestProcessorServ requestResponce) {

        return (Response) NullSafe.create()
                .execute2result(() -> {
                    // выполняем сам запрос
                    final Response response = requestResponce.processRequest();

                    // добавляем в отдельном потоке
                    NullSafe.runNewThread(() -> {

                        synchronized (REQUESTS_LIST) {
                            REQUESTS_LIST.add(new ReqInfo()
                                    .setReqClass(reqClass)
                                    .setReqJson(jsonMessage)
                                    .setReqAnswer(String.format("Status=%d, ReasonPhrase='%s'",
                                            response.getStatus(),
                                            response.getStatusInfo().getReasonPhrase()))
                                    .setReqHashCode(reqHashCode)
                                    .setReqDate(LocalDateTime.now())
                                    .setIsConfirmed(Boolean.FALSE)
                                    .setReqException(response.getStatusInfo().getReasonPhrase()));

//                            LogService.LogInfo(this.getClass(), String.format("reqList count = %s",
//                                    REQ_LIST.size()));
                        }
                    });

                    return response;

                }).<T>getObject();
    }

    //==========================================================================
    private void sendReqList() {

        final List<ReqInfo> list4send;

        synchronized (REQUESTS_LIST) {
            // скопипастили текущие записи
            list4send = REQUESTS_LIST
                    .stream()
                    .collect(Collectors.toList());
        }

        if (!list4send.isEmpty()) {

            // отправляем в отдельном потоке
            NullSafe.runNewThread(() -> {

                final Response response = this.postMessages(list4send,
                        this.getTargetServer(),
                        LogService.REGISTER_REQ,
                        this.getServerReadTimeout(),
                        this.getServerConnectTimeout(),
                        HttpConst.HTTP_200_OK);

                if (HttpConst.HTTP_200_OK.equals(response.getStatus())) {
                    // чистим список
                    synchronized (REQUESTS_LIST) {
                        list4send
                                .stream()
                                .unordered()
                                .forEach((reqInfo) -> {
                                    REQUESTS_LIST.removeIf((removeItem -> removeItem.getReqHashCode() == reqInfo.getReqHashCode()));
                                });
                    }
                }
            });
        }
    }

    //==========================================================================
    @PreDestroy
    private void destroyRestProcessor() {
        //LogService.LogInfo(this.getClass(), () -> LogService.getCurrentObjProcName(this));
    }
}
