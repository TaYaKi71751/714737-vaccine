package test.naver;

import java.util.Arrays;
import java.util.WeakHashMap;

class Main {
    org.jsoup.select.Elements vaccinElements = null;
    String cd = "", url = "", key = "", host = "", harFilePath = Arrays.asList(new java.io.File("./").listFiles())
            .stream().filter(a -> a.getName().contains("har") && a.isFile()).toList().get(0).getName();
    R reservation;

    public void reservation() throws Exception {
        reservation.headersBuilder = new H(harFilePath) {
            {
                this.loadReqHeadersFor("reservation");
                this.headerSetCookie(new C() {
                    {
                        this.b();
                        this.s(reservation.requestBuilder, reservation.$res);
                    }
                }.getCookieHeaderString());
            }
        }.getReqHeadersBuilder();
        reservation.reservation();
    }

    public void auth() throws Exception {
        reservation.headersBuilder = new H(harFilePath) {
            {
                this.loadReqHeadersFor("auth");
                this.headerSetCookie(new test.naver.C() {
                    {
                        this.b();
                        this.s(reservation.requestBuilder, reservation.$res);
                    }
                }.getCookieHeaderString());
            }
        }.getReqHeadersBuilder();
        reservation.auth();
    }

    public void pre_info() {
        key = reservation.requestBuilder.getUrl$okhttp().queryParameter("key");
        host = reservation.requestBuilder.getUrl$okhttp().host();
    }

    public void info() throws Exception {
        org.jsoup.nodes.Document infoDoc;
        while (reservation.$info_res == null
                || (vaccinElements = (infoDoc = org.jsoup.Jsoup.parse(reservation.$info_res.body().string())).select("[data-id]"))
                        .size() == 0) {
            Thread.sleep((long) (Math.random() * 255 + 1990));
            reservation.headersBuilder = new H(harFilePath) {
                {
                    this.loadReqHeadersFor("info");
                    this.headerSetCookie(new C() {
                        {
                            this.b();
                            this.s(reservation.requestBuilder, reservation.$info_res == null ? reservation.$res : reservation.$info_res);
                        }
                    }.getCookieHeaderString());
                }
            }.getReqHeadersBuilder();
            System.gc();
            reservation.info();
        }
        
    }

    public void pre_progress() throws Exception {
        cd = vaccinElements.get((int) (Math.random() * vaccinElements.size())).attr("data-id");
        // get.cd = "VEN00014";
    }

    public void progress() throws Exception {
        reservation.headersBuilder.set("Cookie", new C() {
            {
                this.b();
                this.s(reservation.requestBuilder, reservation.$info_res);
            }
        }.getCookieHeaderString());
        reservation.progress();
    }

    public void confirm() throws Exception {
        reservation.headersBuilder.set("Cookie", new C() {
            {
                this.b();
                this.s(reservation.requestBuilder, reservation.$prog_res);
            }
        }.getCookieHeaderString()).set("Referer", reservation.progressURL);
        try {
            reservation.confirm();
        } catch (Exception e) {
            reservation.error();
        }
        return;
    }

    public void result() throws Exception {
        String confirmResponseBodyString;
        okhttp3.ResponseBody confirmResponseBody;
        WeakHashMap<String, String> responseJson = new WeakHashMap<String, String>();
        Arrays.asList((confirmResponseBodyString = (confirmResponseBody = reservation.$confirm_res.body()).string())
                .replaceAll("[\"|{|}]", "").split(",")).stream().forEach(a -> {
                    responseJson.put(a.split(":")[0], a.split(":")[1]);
                });
        confirmResponseBody.close();
        reservation.headersBuilder.set("Cookie", new C() {
            {
                this.b();
                this.s(reservation.requestBuilder, reservation.$prog_res);
            }
        }.getCookieHeaderString()).set("Referer", reservation.progressURL);
        if (responseJson.get("code").equals("SUCCESS")) {
            reservation.success();
        } else {
            reservation.failure(responseJson.get("code"));
        }
        reservation.success();
    }

    public Main() {
        reservation = new R();
    }

    public static void main(String[] args) throws Exception {
        new Main() {
            {
                this.reservation();
                this.auth();
                this.pre_info();
                this.info();
                this.pre_progress();
                this.progress();
                this.confirm();
                this.result();
            }
        };
        return;
    }
}
