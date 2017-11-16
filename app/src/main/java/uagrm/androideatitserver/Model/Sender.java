package uagrm.androideatitserver.Model;

/**
 * Created by Shep on 10/29/2017.
 */

public class Sender {
    public String to;
    public Notification notification;


    public Sender() {
    }

    public Sender(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}
