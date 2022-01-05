package nvme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;

import org.jsoup.nodes.Element;

import nvme.exception.NotEligibleVaccineSelectException;

class Main {
    List<Element> vaccinRadioItemElements = null, availaVaccinElements = null;
    String url = "", key = "", host = "", harFilePath = "";
    R reservation;

    public void loadHarFilePath() throws Exception {
        this.harFilePath = Arrays.asList(new java.io.File("./").listFiles()).stream()
                .filter(a -> a.getName().contains("har") && a.isFile()).toList().get(0).getName();
    }

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
                this.headerSetCookie(new nvme.C() {
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
        while (reservation.$info_res == null || availaVaccinElements == null || availaVaccinElements.size() == 0) {
            try {
                Thread.sleep((long) (Math.random() * 255 + 1990));
                reservation.headersBuilder = new H(harFilePath) {
                    {
                        this.loadReqHeadersFor("info");
                        this.headerSetCookie(new C() {
                            {
                                this.b();
                                this.s(reservation.requestBuilder,
                                        reservation.$info_res == null ? reservation.$res : reservation.$info_res);
                            }
                        }.getCookieHeaderString());
                    }
                }.getReqHeadersBuilder();
                reservation.info();
                infoDoc = org.jsoup.Jsoup.parse(reservation.$info_res.body().string());
                vaccinRadioItemElements = infoDoc.select("ul > li[class=radio_item]");
                availaVaccinElements = vaccinRadioItemElements.stream()
                        .filter(a -> !a.select("[data-cd]").first().hasAttr("disabled")).toList();
            } catch (java.lang.IllegalStateException e) {
                e.printStackTrace();
                continue;
            }
        }

    }

    public int random() {
        int _tmp = (int) Long.parseLong((Math.random() + "").replace(".", ""));
        return (_tmp != 0) ? ((_tmp < 0) ? (Integer.parseInt(String.valueOf(_tmp).replace("-", ""))) : (_tmp))
                : random();
    }

    public boolean includesAnd(String[] words, String... filterWords) {
        List<String> _words = Arrays.asList(words), _filterWords = Arrays.asList(filterWords);
        ArrayList<String> matchedList = new ArrayList<String>();
        _words.stream().forEach(word -> {
            _filterWords.stream().forEach(filterWord -> {
                if (!word.contains(filterWord))
                    return;
                matchedList.add(word);
            });
        });
        return (_filterWords.size() > matchedList.size()) ? false : true;
    }

    public boolean isEligibleVaccine(String cd) throws NotEligibleVaccineSelectException {
        List<Element> selectedVaccineElements = vaccinRadioItemElements.stream()
                .filter(a -> (a.select("[data-cd]").first().attr("data-cd").equals(cd))).toList();
        for (Element a : selectedVaccineElements) {
            String lastText = a.select("span").last().text();
            String[] lastTextsWithOutNum = lastText.replaceAll("[0-9]", "").split(" ");
            if (!lastText.matches("[0-9]")) {
                final String available = "가능", time = "시간", upper = "이상", age = "세";
                if (includesAnd(lastTextsWithOutNum, available, time)) {
                    return false;
                }
                if (includesAnd(lastTextsWithOutNum, available, upper, age)) {
                    throw new NotEligibleVaccineSelectException();
                }
            }
        }
        return true;
    }

    boolean isNull(Object o) {
        return o == null;
    }

    /**
     * @if reservation.cd value is null
     * @set value to Random available cd
     * 
     * @throws Exception
     */
    public void setCd() throws Exception {
        if (isNull(reservation.cd)) {
            // reservation.cd = availaVaccinElements.get(random() %
            // availaVaccinElements.size()).attr("data-cd");
            reservation.cd = "VEN00013";
        }
        if (!isNull(reservation.cd)) {
            if (isEligibleVaccine(reservation.cd)) {
                return;
            }
        }
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
                this.loadHarFilePath();
                this.reservation();
                this.auth();
                this.pre_info();
                this.info();
                this.setCd();
                this.progress();
                this.confirm();
                this.result();
            }
        };
        return;
    }
}
