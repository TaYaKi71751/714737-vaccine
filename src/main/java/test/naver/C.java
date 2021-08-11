package test.naver;

import java.io.File;
import java.util.Arrays;

import okhttp3.Cookie;
import test.naver.exception.InvalidLogInException;

public class C extends Object {

    String cookieSQLitePath, cookieHeaderString;
    org.sqlite.SQLiteConfig cookieSQLiteConfig;
    java.sql.Connection c;
    java.sql.ResultSet r;
    java.sql.Statement s;
    final static java.lang.String jdbcSQLitePrefixString = "jdbc:sqlite:";

    public C() throws Exception {
        Arrays.asList(new File(System.getProperty("user.home") + (System.getProperty("os.name").contains("Linux")
                ? "/.mozilla/firefox"
                : (System.getProperty("os.name").contains("Windows") ? "\\AppData\\Roaming\\Mozilla\\Firefox\\Profiles"
                        : ""))).listFiles())
                .stream()
                .filter(a -> a.getPath().contains("default") && a.getPath().contains("dev-edition") && a.isDirectory())
                .forEach(a -> {
                    this.cookieSQLitePath = a + "/cookies.sqlite";
                });
    }

    public Object b() throws Exception {
        this.cookieSQLiteConfig = new org.sqlite.SQLiteConfig() {
            {
                setReadOnly(false);
                setSharedCache(true);
            }
        };
        this.c = java.sql.DriverManager.getConnection(jdbcSQLitePrefixString + this.cookieSQLitePath,
                this.cookieSQLiteConfig.toProperties());

        s = c.createStatement();
        s.setQueryTimeout(r());
        return this;
    }

    public void s(okhttp3.Request.Builder q, okhttp3.Response e) throws Exception {
        if (e == null) {
            return;
        }
        if (e.header("Set-Cookie") == null) {
            throw new InvalidLogInException("Cannot parse Cookies because Set-Cookie is null.");
        }
        for (Cookie a : okhttp3.Cookie.parseAll(q.getUrl$okhttp(), e.headers()).stream()
                .filter(a -> a.name().contains("NID_SES")).toList()) {
            s.executeUpdate("update moz_cookies set value = \'" + a.value() + "\' where name = \'" + a.name() + "\';");
            if (a.value().contains("expired")) {
                throw new InvalidLogInException();
            }
        }

    }

    public int r() throws Exception {
        this.cookieHeaderString = "";
        this.r = this.s.executeQuery("select * from moz_cookies where host like \'.n%aver%\'");
        while (r.next() && r.getString("host").contains("naver")) {
            this.cookieHeaderString += r.getString("name") + "=" + this.r.getString("value") + "; ";
        }
        return (int) ((Math.random()) / Math.random()) + 30;
    }

    public String getCookieHeaderString() {
        return this.cookieHeaderString.substring(0, this.cookieHeaderString.length() - 2);
    }
}
