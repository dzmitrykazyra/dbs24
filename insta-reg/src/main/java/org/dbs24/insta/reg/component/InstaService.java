/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dbs24.insta.reg.component;

import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.dbs24.insta.reg.action.CsrfRecords;
import org.dbs24.insta.reg.action.BatchFetchWeb;
import org.dbs24.insta.reg.action.EmailSignUpResult;
import org.dbs24.insta.reg.action.RegAttemptResult;
import org.dbs24.insta.reg.action.AccountCreateResult;
import org.dbs24.insta.reg.action.CheckConfirmationCodeResult;
import org.dbs24.insta.reg.entity.Account;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.dbs24.spring.core.api.AbstractApplicationService;
import org.dbs24.stmt.StmtProcessor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.dbs24.insta.reg.encrypt.SealedBoxUtility;
import org.dbs24.service.JavaFakerService;

@Data
@Log4j2
@Component
@ConditionalOnProperty(name = "config.restfull.profile.name", havingValue = "insta-reg")
public class InstaService extends AbstractApplicationService {

    final JavaFakerService javaFakerService;
    final RefsService refsService;

    private String userAgent;

    public InstaService(JavaFakerService javaFakerService, RefsService refsService) {
        this.javaFakerService = javaFakerService;
        this.refsService = refsService;
    }

    //==========================================================================
    public String getUserAgent() {
        StmtProcessor.ifNull(userAgent, () -> updateUserAgent());
        return userAgent;
    }

    public void updateUserAgent() {
        userAgent = refsService.getUserAgent();
        log.info("using new userAgent '{}'", userAgent);
    }

    //==========================================================================
    private String encrypt(int key, String pkey, String password, String time) throws Exception {
        int overheadLength = 48;
        byte[] pkeyArray = new byte[pkey.length() / 2];
        for (int i = 0; i < pkeyArray.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(pkey.substring(index, index + 2), 16);
            pkeyArray[i] = (byte) j;
        }

        byte[] y = new byte[password.length() + 36 + 16 + overheadLength];

        int f = 0;
        y[f] = 1;
        y[f += 1] = (byte) key;
        f += 1;

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);

        // Generate Key
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] IV = new byte[12];

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
        cipher.updateAAD(time.getBytes());

        byte[] sealed = SealedBoxUtility.crypto_box_seal(secretKey.getEncoded(), pkeyArray);

        byte[] cipherText = cipher.doFinal(password.getBytes());
        y[f] = (byte) (255 & sealed.length);
        y[f + 1] = (byte) (sealed.length >> 8 & 255);
        f += 2;
        for (int j = f; j < f + sealed.length; j++) {
            y[j] = sealed[j - f];
        }
        f += 32;
        f += overheadLength;

        byte[] c = Arrays.copyOfRange(cipherText, cipherText.length - 16, cipherText.length);
        byte[] h = Arrays.copyOfRange(cipherText, 0, cipherText.length - 16);

        for (int j = f; j < f + c.length; j++) {
            y[j] = c[j - f];
        }
        f += 16;
        for (int j = f; j < f + h.length; j++) {
            y[j] = h[j - f];
        }
        return Base64.getEncoder().encodeToString(y);
    }

    //==========================================================================
    public String getMid(OkHttpClient httpProxyClient) {

        return StmtProcessor.create(() -> {

            RequestBody formBody = new FormBody.Builder()
                    .build();

            updateUserAgent();

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/web/__mid/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("cache-control", "max-age=0")
                    .addHeader("dnt", "1")
                    .addHeader("upgrade-insecure-requests", "1")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "navigate")
                    .addHeader("sec-fetch-user", "?1")
                    .addHeader("sec-fetch-dest", "document")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .post(formBody)
                    .get()
                    .build();

            Response response = httpProxyClient.newCall(request).execute();

            final String mid = new String(response.body().bytes());

            log.info("getting mid = {}, code = {}", mid, response.code());

            return mid;

        });
    }

    //==========================================================================
    public CsrfRecords generateCsrf(OkHttpClient httpProxyClient, String mid) {

        return StmtProcessor.create(CsrfRecords.class, csrf -> {

            final RequestBody formBody = new FormBody.Builder()
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/data/shared_data/?__a=1")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("cache-control", "max-age=0")
                    .addHeader("dnt", "1")
                    .addHeader("upgrade-insecure-requests", "1")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "navigate")
                    .addHeader("sec-fetch-user", "?1")
                    .addHeader("sec-fetch-dest", "document")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .post(formBody)
                    .get()
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            final JSONObject obj = new JSONObject(jsonString);

            csrf.setPublicKey(obj.getJSONObject("encryption").getString("public_key"));
            csrf.setKeyId(obj.getJSONObject("encryption").getInt("key_id"));
            csrf.setVersion(obj.getJSONObject("encryption").getString("version"));
            csrf.setDeviceId(obj.getString("device_id"));
            csrf.setCsrfToken(obj.getJSONObject("config").getString("csrf_token"));
            csrf.setXInstagramAjax(obj.getString("rollout_hash"));
            csrf.getCookie().append("ig_did=").append(csrf.getDeviceId()).append(";")
                    .append("csrftoken=").append(csrf.getCsrfToken()).append(";")
                    .append("mid=").append(mid).append(";")
                    .append("ig_nrcb=").append("1").append(";");

            log.info("getting scrf = {}, code = {}", csrf, response.code());

        });
    }

    //==========================================================================
    public String getFirst(OkHttpClient httpProxyClient, StringBuilder cookie) {

        return StmtProcessor.create(String.class, answer -> {

            final RequestBody formBody = new FormBody.Builder()
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("upgrade-insecure-requests", "1")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("sec-fetch-site", "none")
                    .addHeader("sec-fetch-mode", "navigate")
                    .addHeader("sec-fetch-user", "?1")
                    .addHeader("sec-fetch-dest", "document")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", cookie.toString())
                    .post(formBody)
                    .get()
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            log.info("getting first, code = {}", response.code());

        });
    }

    //==========================================================================
    public BatchFetchWeb batchFetchWeb(OkHttpClient httpProxyClient, CsrfRecords csrfRecords) {

        return StmtProcessor.create(BatchFetchWeb.class, bfw -> {

            RequestBody formBody = new FormBody.Builder()
                    .add("bloks_versioning_id", "9e8c537791731f0e4de1edcf762c184369fe1018eb792790f62aaedc5be9b00b")
                    .add("surfaces_to_queries", "%7B%225095%22%3A%22viewer()+%7B%5Cn++++eligible_promotions.ig_parameters(%3Cig_parameters%3E).surface_nux_id(%3Csurface%3E).external_gating_permitted_qps(%3Cexternal_gating_permitted_qps%3E)+%7B%5Cn++++++edges+%7B%5Cn++++++++priority%2C%5Cn++++++++time_range+%7B%5Cn++++++++++start%2C%5Cn++++++++++end%5Cn++++++++%7D%2C%5Cn++++++++node+%7B%5Cn++++++++++id%2C%5Cn++++++++++promotion_id%2C%5Cn++++++++++max_impressions%2C%5Cn++++++++++triggers%2C%5Cn++++++++++logging_data%2C%5Cn++++++++++template+%7B%5Cn++++++++++++name%2C%5Cn++++++++++++parameters+%7B%5Cn++++++++++++++name%2C%5Cn++++++++++++++string_value%5Cn++++++++++++%7D%5Cn++++++++++%7D%2C%5Cn++++++++++creatives+%7B%5Cn++++++++++++title+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++content+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++footer+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++social_context+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++primary_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++secondary_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++dismiss_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++image+%7B%5Cn++++++++++++++uri%5Cn++++++++++++%7D%5Cn++++++++++%7D%5Cn++++++++%7D%5Cn++++++%7D%5Cn++++%7D%5Cn%5Cn%7D%22%2C%225780%22%3A%22viewer()+%7B%5Cn++++eligible_promotions.ig_parameters(%3Cig_parameters%3E).surface_nux_id(%3Csurface%3E).external_gating_permitted_qps(%3Cexternal_gating_permitted_qps%3E)+%7B%5Cn++++++edges+%7B%5Cn++++++++priority%2C%5Cn++++++++time_range+%7B%5Cn++++++++++start%2C%5Cn++++++++++end%5Cn++++++++%7D%2C%5Cn++++++++node+%7B%5Cn++++++++++id%2C%5Cn++++++++++promotion_id%2C%5Cn++++++++++max_impressions%2C%5Cn++++++++++triggers%2C%5Cn++++++++++logging_data%2C%5Cn++++++++++template+%7B%5Cn++++++++++++name%2C%5Cn++++++++++++parameters+%7B%5Cn++++++++++++++name%2C%5Cn++++++++++++++string_value%5Cn++++++++++++%7D%5Cn++++++++++%7D%2C%5Cn++++++++++creatives+%7B%5Cn++++++++++++title+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++content+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++footer+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++social_context+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++primary_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++secondary_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++dismiss_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++image+%7B%5Cn++++++++++++++uri%5Cn++++++++++++%7D%5Cn++++++++++%7D%5Cn++++++++%7D%5Cn++++++%7D%5Cn++++%7D%5Cn%5Cn%7D%22%2C%225906%22%3A%22viewer()+%7B%5Cn++++eligible_promotions.ig_parameters(%3Cig_parameters%3E).surface_nux_id(%3Csurface%3E).external_gating_permitted_qps(%3Cexternal_gating_permitted_qps%3E)+%7B%5Cn++++++edges+%7B%5Cn++++++++priority%2C%5Cn++++++++time_range+%7B%5Cn++++++++++start%2C%5Cn++++++++++end%5Cn++++++++%7D%2C%5Cn++++++++node+%7B%5Cn++++++++++id%2C%5Cn++++++++++promotion_id%2C%5Cn++++++++++max_impressions%2C%5Cn++++++++++triggers%2C%5Cn++++++++++logging_data%2C%5Cn++++++++++template+%7B%5Cn++++++++++++name%2C%5Cn++++++++++++parameters+%7B%5Cn++++++++++++++name%2C%5Cn++++++++++++++string_value%5Cn++++++++++++%7D%5Cn++++++++++%7D%2C%5Cn++++++++++creatives+%7B%5Cn++++++++++++title+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++content+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++footer+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++social_context+%7B%5Cn++++++++++++++text%5Cn++++++++++++%7D%2C%5Cn++++++++++++primary_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++secondary_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++dismiss_action%7B%5Cn++++++++++++++title+%7B%5Cn++++++++++++++++text%5Cn++++++++++++++%7D%2C%5Cn++++++++++++++url%2C%5Cn++++++++++++++limit%2C%5Cn++++++++++++++dismiss_promotion%5Cn++++++++++++%7D%2C%5Cn++++++++++++image+%7B%5Cn++++++++++++++uri%5Cn++++++++++++%7D%5Cn++++++++++%7D%5Cn++++++++%7D%5Cn++++++%7D%5Cn++++%7D%5Cn%5Cn%7D%22%7D")
                    .add("vc_policy", "default")
                    .add("version", "1")
                    .build();

            Request request = new Request.Builder()
                    .url("https://www.instagram.com/qp/batch_fetch_web/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", csrfRecords.getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("user-agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", csrfRecords.getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", csrfRecords.getCookie().toString())
                    .post(formBody)
                    .build();

            Response response = httpProxyClient.newCall(request).execute();

            String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            bfw.setBatchFetchWeb(jsonString);
            bfw.setBatchFetchWebCode(response.code());

            log.info("getting first, code = {}", response.code());

        });
    }

    //==========================================================================
    public EmailSignUpResult emailSignUp(OkHttpClient httpProxyClient, CsrfRecords csrfRecords) {

        return StmtProcessor.create(EmailSignUpResult.class, esu -> {

            final RequestBody formBody = new FormBody.Builder()
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/accounts/emailsignup/?__a=1")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("accept", "*/*")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", csrfRecords.getCookie().toString())
                    .post(formBody)
                    .get()
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            esu.setAnswerCode(response.code());
            esu.setEmailSignUp(jsonString);

            log.info("EmailSignUp code = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult createAttempt1(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("createAttempt1 email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .addEncoded("email", account.getFakedEmail())
                    .add("username", "")
                    .add("first_name", "")
                    .add("opt_into_one_tap", "false")
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/accounts/web_create_ajax/attempt/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("createAttempt1 code = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult createAttempt2(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("createAttempt2 email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .addEncoded("email", account.getFakedEmail())
                    .add("username", "")
                    .add("first_name", account.getFirstName())
                    .add("opt_into_one_tap", "false")
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/accounts/web_create_ajax/attempt/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            final JSONObject obj = new JSONObject(jsonString);

            try {

                final JSONArray jsonArray = obj.getJSONArray("username_suggestions");

                rar.setNameSuggestions(jsonArray.getString(2));

            } catch (Throwable th) {
                rar.setNameSuggestions(account.getFirstName());
                log.error("createAttempt2: username_suggestions not defined: " + th.getMessage());
            }

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("createAttempt2 code = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult createAttempt3(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("createAttempt3 email = {}", account.getFakedEmail());

            final String time = String.valueOf(System.currentTimeMillis() / 1000);
            //password_insta = new String(generatePassword(10));

            account.setEncPwd("#PWD_INSTAGRAM_BROWSER:" + account.getInstaCsrf().getVersion()
                    + ":" + time + ":"
                    + encrypt(account.getInstaCsrf().getKeyId(),
                            account.getInstaCsrf().getPublicKey(),
                            account.getPwd(), time));

            final RequestBody formBody = new FormBody.Builder()
                    .addEncoded("email", account.getFakedEmail())
                    .add("enc_password", account.getEncPwd())
                    .add("username", account.getLogin())
                    .add("first_name", account.getFirstName())
                    .addEncoded("client_id", account.getInstaMid())
                    .add("seamless_login_enabled", "1")
                    .add("opt_into_one_tap", "false")
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/accounts/web_create_ajax/attempt/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("createAttempt3 code = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult checkAgeEligibility(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("checkAgeEligibility email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .add("month", String.valueOf(account.getMonth()))
                    .add("day", String.valueOf(account.getDay()))
                    .add("year", String.valueOf(account.getYear()))
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/web/consent/check_age_eligibility/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("checkAgeEligibility code = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult createAttempt4(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("createAttempt4 email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .addEncoded("email", account.getFakedEmail())
                    .add("enc_password", account.getEncPwd())
                    .add("username", account.getLogin())
                    .add("first_name", account.getFirstName())
                    .add("month", String.valueOf(account.getMonth()))
                    .add("day", String.valueOf(account.getDay()))
                    .add("year", String.valueOf(account.getYear()))
                    .addEncoded("client_id", account.getInstaMid())
                    .add("seamless_login_enabled", "1")
                    .add("opt_into_one_tap", "false")
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/accounts/web_create_ajax/attempt/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("createAttempt4 code = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult sendVerifyEmailOption(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("sendVerifyEmailOption email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .build();

            final Request request = new Request.Builder()
                    .url("https://i.instagram.com/api/v1/accounts/send_verify_email/")
                    .addHeader("authority", "i.instagram.com")
                    .addHeader("accept", "*/*")
                    .addHeader("access-control-request-method", "POST")
                    .addHeader("access-control-request-headers", "x-csrftoken,x-ig-app-id,x-ig-www-claim,x-instagram-ajax")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-site")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .method("OPTIONS", formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("sendVerifyEmailOption = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult sendVerifyEmail(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("sendVerifyEmail email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .add("device_id", account.getInstaMid())
                    .addEncoded("email", account.getFakedEmail())
                    .build();

            final Request request = new Request.Builder()
                    .url("https://i.instagram.com/api/v1/accounts/send_verify_email/")
                    .addHeader("authority", "i.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-site")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("sendVerifyEmail = {}", response.code());

        });
    }

    //==========================================================================
    public RegAttemptResult checkConfirmationCodeOption(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(RegAttemptResult.class, rar -> {

            log.info("checkConfirmationCodeOption email = {}", account.getFakedEmail());

            final RequestBody formBody = new FormBody.Builder()
                    .build();

            final Request request = new Request.Builder()
                    .url("https://i.instagram.com/api/v1/accounts/check_confirmation_code/")
                    .addHeader("authority", "i.instagram.com")
                    .addHeader("accept", "*/*")
                    .addHeader("access-control-request-method", "POST")
                    .addHeader("access-control-request-headers", "x-csrftoken,x-ig-app-id,x-ig-www-claim,x-instagram-ajax")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-site", "same-site")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/")
                    .addHeader("User-Agent", userAgent)
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .method("OPTIONS", formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("checkConfirmationCodeOption = {}", response.code());

        });
    }
    //==========================================================================

    public CheckConfirmationCodeResult checkConfirmationCode(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(CheckConfirmationCodeResult.class, cccr -> {

            log.info("checkConfirmationCode email = {}, code = {}", account.getFakedEmail(), account.getValidationCode());

            /*
            Scanner sc= new Scanner(System.in); //System.in is a standard input stream
            System.out.print("Enter a validatecode: ");
            validatecode= sc.nextLine();              //reads string
            System.out.print("You have entered validatecode: "+validatecode);
             */
            RequestBody formBody = new FormBody.Builder()
                    .add("code", account.getValidationCode())
                    .addEncoded("device_id", account.getInstaMid())
                    .addEncoded("email", account.getFakedEmail())
                    .build();

            Request request = new Request.Builder()
                    .url("https://i.instagram.com/api/v1/accounts/check_confirmation_code/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-site")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            cccr.setAnswerCode(response.code());
            cccr.setAttemptResult(jsonString);

            final JSONObject obj = new JSONObject(jsonString);

            cccr.setSignUpCode(obj.getString("signup_code"));

            log.info("checkConfirmationCode = {}, signup_code = {}", response.code(), cccr.getSignUpCode());

        });
    }

    //==========================================================================
    public AccountCreateResult webCreate(OkHttpClient httpProxyClient, Account account) {

        return StmtProcessor.create(AccountCreateResult.class, rar -> {

            log.info("webCreate email = {}, signUpCode = {}", account.getFakedEmail(), account.getSignUpCode());

            final RequestBody formBody = new FormBody.Builder()
                    .addEncoded("email", account.getFakedEmail())
                    .add("enc_password", account.getEncPwd())
                    .add("username", account.getLogin())
                    .addEncoded("first_name", account.getFirstName())
                    .add("month", String.valueOf(account.getMonth()))
                    .add("day", String.valueOf(account.getDay()))
                    .add("year", String.valueOf(account.getYear()))
                    .addEncoded("client_id", account.getInstaMid())
                    .add("seamless_login_enabled", "1")
                    .add("tos_version", "row")
                    .add("force_sign_up_code", account.getSignUpCode())
                    .build();

            final Request request = new Request.Builder()
                    .url("https://www.instagram.com/accounts/web_create_ajax/")
                    .addHeader("authority", "www.instagram.com")
                    .addHeader("dnt", "1")
                    .addHeader("x-ig-www-claim", "0")
                    .addHeader("x-instagram-ajax", account.getInstaCsrf().getXInstagramAjax())
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("accept", "*/*")
                    .addHeader("User-Agent", getUserAgent())
                    .addHeader("x-requested-with", "XMLHttpRequest")
                    .addHeader("x-csrftoken", account.getInstaCsrf().getCsrfToken())
                    .addHeader("x-ig-app-id", "936619743392459")
                    .addHeader("origin", "https://www.instagram.com")
                    .addHeader("sec-fetch-site", "same-origin")
                    .addHeader("sec-fetch-mode", "cors")
                    .addHeader("sec-fetch-dest", "empty")
                    .addHeader("referer", "https://www.instagram.com/accounts/emailsignup/")
                    .addHeader("accept-language", "q=0.9,en-US;q=0.8,en;q=0.7")
                    .addHeader("cookie", account.getInstaCsrf().getCookie().toString())
                    .post(formBody)
                    .build();

            final Response response = httpProxyClient.newCall(request).execute();

            final String jsonString = new String(response.body().bytes());

            log.info("insta response is '{}'", jsonString);

            rar.setAccountCreated(jsonString.contains("\"account_created\":true"));
            rar.setAnswerCode(response.code());
            rar.setAttemptResult(jsonString);

            log.info("checkConfirmationCode = {}", response.code());

        });
    }
}
