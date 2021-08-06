package test.naver;

import java.io.File;
import java.util.HashMap;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.Dotenv.Filter;
import okhttp3.Cache;

class R {
    Boolean isTried;
    String want_vaccine;
    HashMap<String, String> reservationParams;
    okhttp3.Response $res;
    okhttp3.Response $auth_res;
    okhttp3.Response $info_res;
    String orgCd, sid, cookieString, NID_SES;
    okhttp3.OkHttpClient.Builder okHttpClientBuilder = new okhttp3.OkHttpClient.Builder().followSslRedirects(true)
            .cache(new Cache(new File("_cache"), 1024L * 1024L * 1024L * 1024L)).followRedirects(false);
    okhttp3.OkHttpClient nonRdrctOkHttpClient = okHttpClientBuilder.build();
    okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
    okhttp3.Request.Builder reqestBuilder = new okhttp3.Request.Builder();
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

    public void auth() throws Exception {
        $auth_res = null;
        if (isExpired()) {
            throw new test.naver.exception.InvalidLogInException();
        }
        while ($auth_res == null
                || $auth_res.code() / 100 == 3 && $auth_res.header("Location").equals($res.header("Location"))) {
            $auth_res = nonRdrctOkHttpClient
                    .newCall(reqestBuilder.url(($auth_res == null ? $res : $auth_res).header("Location"))
                            .headers(headersBuilder.build()).build())
                    .execute();
            if ($auth_res.code() / 100 == 4)
                throw new test.naver.exception.HttpResponseException();
        }
    }

    public void info() throws Exception {
        $info_res = null;
        while ($info_res == null || $info_res.code() / 100 == 3 || $info_res.header("Location") != null) {
            this.$info_res = nonRdrctOkHttpClient
                    .newCall(reqestBuilder.url(($info_res == null ? $auth_res : $info_res).header("Location"))
                            .headers(headersBuilder.build()).build())
                    .execute();
            if ($info_res.code() / 100 == 2)
                break;
            if ($info_res.code() / 100 == 4)
                throw new test.naver.exception.HttpResponseException(this.$info_res);
        }
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

    public void reservation(Integer orgCd, Integer sid) throws Exception {
        reqestBuilder.url("https://v-search.nid.naver.com/reservation?orgCd=" + orgCd + "&sid=" + sid)
                .headers(headersBuilder.build());
        this.$res = nonRdrctOkHttpClient.newCall(reqestBuilder.build()).execute();
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
                Dotenv.configure().filename(".env.reservation").load().entries(Filter.DECLARED_IN_ENV_FILE)
                        .forEach(a -> {
                            put(a.getKey(), a.getValue());
                        });
            }
        };
    }
}
