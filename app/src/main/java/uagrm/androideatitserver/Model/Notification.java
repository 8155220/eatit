package uagrm.androideatitserver.Model;

/**
 * Created by Shep on 10/29/2017.
 */

public class Notification {
    public String title;
    public String body;

    public Notification(String title,String body ) {
        this.body = body;
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
