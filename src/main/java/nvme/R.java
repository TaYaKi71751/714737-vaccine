package nvme;

import java.io.File;
import java.util.HashMap;
import java.util.WeakHashMap;

import nvme.exception.HttpResponseException;
import nvme.exception.InvalidLogInException;
import okhttp3.Cache;
import okhttp3.FormBody;
import okhttp3.RequestBody;

class R {
    Boolean isTried;
    String want_vaccine;
    HashMap<String, String> reservationParams;
    okhttp3.Response $res, $auth_res, $info_res, $prog_res, $confirm_res, $fin_res, $suc_res, $fail_res, $err_res;
    WeakHashMap<String, String> e = new WeakHashMap<>();
    String orgCd, sid, cookieString, NID_SES, key, cd, progressURL;
    okhttp3.OkHttpClient.Builder okHttpClientBuilder = new okhttp3.OkHttpClient.Builder().followSslRedirects(true)
            .cache(new Cache(new File("_cache"), 1024L * 1024L * 1024L * 1024L)).followRedirects(false);
    okhttp3.OkHttpClient nonRdrctOkHttpClient = okHttpClientBuilder.build();
    okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
    okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
    okhttp3.Cookie.Builder cookieBuilder = new okhttp3.Cookie.Builder();

    public void reservation() throws Exception {
        Integer orgCdInt, sidInt;
        try {
            orgCdInt = Integer.parseInt(orgCd);
            sidInt = Integer.parseInt(sid);
        } catch (java.lang.NumberFormatException e) {
            throw new java.lang.NumberFormatException("Failed with parseInt(orgCd),parseInt(sid)");
        }
        reservation(orgCdInt, sidInt);
    }

    /**
     * @GET
     * @PREFETCH
     * @NAVIGATE
     * @REDIRECT
     * @UPGRADE_INSECURE_REQUESTS
     * @throws Exception
     */
    public void auth() throws Exception {
        $auth_res = null;
        if (isExpired()) {
            throw new nvme.exception.InvalidLogInException();
        }
        while ($auth_res == null
                || $auth_res.code() / 100 == 3 && $auth_res.header("Location").equals($res.header("Location"))) {
            $auth_res = nonRdrctOkHttpClient
                    .newCall(requestBuilder.url(($auth_res == null ? $res : $auth_res).header("Location"))
                            .headers(headersBuilder.set("X-moz", "prefetch").build()).build())
                    .execute();
            if (((($auth_res.code() / 100) >> 1) << 1) == ((($auth_res.code() / 100) << 1) >> 1)) {
                if ((($auth_res.code() / 100) >> 2) << 2 == ((($auth_res.code() / 100) << 2) >> 2))
                    throw new nvme.exception.HttpResponseException();
                if ((($auth_res.code() / 100) >> 2) << 2 != ((($auth_res.code() / 100) << 2) >> 2))
                    throw new nvme.exception.AuthException();
            }
            this.$auth_res.close();
        }
        this.key = requestBuilder.getUrl$okhttp().queryParameter("key");
    }

    /**
     * @GET
     * @PREFETCH
     * @NAVIGATE
     * @UPGRADE_INSECURE_REQUESTS
     * @throws Exception
     */
    public void info() throws Exception {
        $info_res = null;
        while ($info_res == null || $info_res.code() / 100 == 3 || $info_res.header("Location") != null) {
            this.$info_res = nonRdrctOkHttpClient
                    .newCall(requestBuilder.url(($info_res == null ? $auth_res : $info_res).header("Location"))
                            .headers(headersBuilder.set("X-moz", "prefetch").build()).build())
                    .execute();
            if (((($info_res.code() / 100) >> 1) << 1) == ((($info_res.code() / 100) << 1) >> 1)) {
                if ((($info_res.code() / 100) >> 2) << 2 == ((($info_res.code() / 100) << 2) >> 2))
                    throw new nvme.exception.HttpResponseException(this.$info_res);
                if ((($info_res.code() / 100) >> 2) << 2 != ((($info_res.code() / 100) << 2) >> 2))
                    break;
            }
            this.$info_res.close();
        }
        this.$info_res.close();
    }

    /**
     * @GET
     * @NAVIGATE
     * @throws Exception
     */
    public void progress() throws Exception {
        this.progressURL = "https://" + requestBuilder.getUrl$okhttp().host() + "/reservation/progress?key=" + this.key
                + "&cd=" + this.cd;
        $prog_res = nonRdrctOkHttpClient
                .newCall(requestBuilder.url(this.progressURL).headers(headersBuilder.build()).build()).execute();
        return;
    }

    /**
     * @POST
     * @CORS
     * @EMPTY
     * @Referer : progress.URL
     */
    public void confirm() throws Exception {
        String url = "https://" + requestBuilder.getUrl$okhttp().host() + "/reservation/confirm";
        $confirm_res = nonRdrctOkHttpClient.newCall(requestBuilder.url(url)
                .headers(new okhttp3.Headers.Builder().addAll(headersBuilder.build()).removeAll("Pragma")
                        .removeAll("Cache-Control").removeAll("Sec-Fetch-User").removeAll("Upgrade-Insecure-Requests")
                        .removeAll("X-moz").set("Sec-Fetch-Site", "same-origin").set("Sec-Fetch-Mode", "cors")
                        .set("Sec-Fetch-Dest", "empty").set("Accept", "*/*").set("content-type", "application/json")
                        .set("Content-Length", "0").build())
                .post((RequestBody) (new FormBody.Builder().add("key", this.key).build())).build()).execute();
        switch (this.$confirm_res.code() / 100) {
            case 4:
                throw new HttpResponseException(this.$confirm_res);
            case 3:
                if (this.$confirm_res.header("Set-Cookie").contains("expired"))
                    throw new InvalidLogInException();
            case 2:
            default:
                break;
        }
    }

    /**
     * TODO
     * 
     * @GET
     * @Referer : progress.URL
     */
    public void success() throws Exception {
        String url = "https://" + requestBuilder.getUrl$okhttp().host() + "/reservation/success?key=" + this.key;
        this.$suc_res = nonRdrctOkHttpClient
                .newCall(requestBuilder.url(url)
                        .headers((new okhttp3.Headers.Builder().addAll($confirm_res.request().headers()))
                                .set("Sec-Fetch-Mode", "navigate").set("Sec-Fetch-Dest", "document").build())
                        .build())
                .execute();
    }

    /**
     * @GET
     * @NAVIGATE
     * @Referer : progress.URL
     */
    public void failure(String code) throws Exception {
        String url = "https://" + requestBuilder.getUrl$okhttp().host() + "/reservation/failure?key=" + this.key
                + "&code=" + code;
        this.$fail_res = nonRdrctOkHttpClient
                .newCall(requestBuilder.url(url)
                        .headers((new okhttp3.Headers.Builder().addAll($confirm_res.request().headers()))
                                .set("Sec-Fetch-Mode", "navigate").set("Sec-Fetch-Dest", "document").build())
                        .build())
                .execute();
        return;
    }

    /**
     * TODO
     * 
     * @GET
     * @Referer : progress.URL
     */
    public void error() throws Exception {
        String url = "https://" + requestBuilder.getUrl$okhttp().host() + "/reservation/error?key=" + this.key;
        this.$err_res = nonRdrctOkHttpClient
                .newCall(requestBuilder.url(url)
                        .headers((new okhttp3.Headers.Builder().addAll($confirm_res.request().headers()))
                                .set("Sec-Fetch-Mode", "navigate").set("Sec-Fetch-Dest", "document").build())
                        .build())
                .execute();
    }

    public void subsNID_SES() {
        String tmp = "";
        Integer a = -1, b = -1;
        if ($auth_res != null) {
            tmp = (this.NID_SES = this.$auth_res.header("Set-Cookie").split(";")[0]);
        } else if ($res != null) {
            tmp = (this.NID_SES = this.$res.header("Set-Cookie").split(";")[0]);
        }
        if (tmp != "" && tmp != null && ($auth_res != null || $res != null)) {
            a = tmp.indexOf("=") + 1;
            b = tmp.length();
        }
        if (a != -1 && b != -1) {
            this.NID_SES = tmp.substring(a, b);
        }
    }

    public boolean isExpired() {
        return $res.header("Set-Cookie").contains("expired");
    }

    /**
     * @GET
     * @PREFETCH
     * @NAVIGATE
     * @REDIRECT
     * @UPGRADE_INSECURE_REQUESTS
     * @throws Exception
     */
    public void reservation(Integer orgCd, Integer sid) throws Exception {
        requestBuilder.url("https://v-search.nid.naver.com/reservation?orgCd=" + orgCd + "&sid=" + sid)
                .headers(headersBuilder.set("X-moz", "prefetch").build());
        this.$res = nonRdrctOkHttpClient.newCall(requestBuilder.build()).execute();
        this.$res.close();
        return;
    }

    public void setH(okhttp3.Headers.Builder e) {
        this.headersBuilder = e;
    }

    public R() {
        reservationParams = getReservationParams();
        this.orgCd = reservationParams.get("orgCd");
        this.sid = reservationParams.get("sid");
    }

    private HashMap<String, String> getReservationParams() {
        return new HashMap<String, String>() {
            {
                io.github.cdimascio.dotenv.Dotenv.configure().filename(".env.reservation").load()
                        .entries(io.github.cdimascio.dotenv.Dotenv.Filter.DECLARED_IN_ENV_FILE).forEach(a -> {
                            put(a.getKey(), a.getValue());
                        });
            }
        };
    }
}
