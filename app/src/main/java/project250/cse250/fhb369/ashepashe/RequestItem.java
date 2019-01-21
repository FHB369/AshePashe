/**
 *Created by Faisal Haque Bappy on 09-Jan-19.
 */

package project250.cse250.fhb369.ashepashe;

public class RequestItem {
    String title, date, pkg, status, key;

    public RequestItem(String title, String date, String pkg, String status, String key) {
        this.title = title;
        this.date = date;
        this.pkg = pkg;
        this.status = status;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
