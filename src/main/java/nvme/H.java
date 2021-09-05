package nvme;

import java.io.File;

import de.sstoehr.harreader.HarReader;
import de.sstoehr.harreader.model.Har;

public class H {
    okhttp3.Headers.Builder headersBuilder = new okhttp3.Headers.Builder();
    String harFilePath;
    HarReader harReader = new HarReader();
    Har har;

    public H(String harFilePath) throws Exception {
        this.har = harReader.readFromFile(new File(this.harFilePath = harFilePath));
        this.har.getLog();
    }

    public void reload() throws Exception {
        this.har = harReader.readFromFile(new File(harFilePath));
    }

    public void loadReqHeadersFor(String urlPath) {
        this.har.getLog().getEntries().stream().filter(a -> a.getRequest().getUrl().contains(urlPath)).toList().get(0)
                .getRequest().getHeaders().stream().filter(b -> !(b.getName().contains("Accept-Encoding")||b.getName().contains("Cookie"))).forEach(b -> {
                    this.headersBuilder.set(b.getName(), b.getValue());
                });
    }

    public okhttp3.Headers.Builder getReqHeadersBuilder() {
        return headersBuilder;
    }

    public void headerSetHar() {

    }

    public void headerSetCookie(String cookieString) {
        headersBuilder.set("Cookie", cookieString);
    }
}
