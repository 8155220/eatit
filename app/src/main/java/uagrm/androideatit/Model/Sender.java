package uagrm.androideatit.Model;

/**
 * Created by Shep on 10/29/2017.
 */

public class Sender {
    public String to;
    public Notification notification;


    public Sender(String token, Notification notification) {
        this.to = token;
        this.notification=notification;
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

    @Override
    public String toString() {
        return "Sender{" +
                "to='" + to + '\'' +
                ", notification=" + notification +
                '}';
    }
}
