import java.sql.Timestamp;
public class Message {
    Timestamp timestamp;
    String id;
    String message;
    String author;
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setText(String text) {
        this.message = text;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Message(Timestamp timestamp, String id, String message, String author) {
        this.timestamp = timestamp;
        this.id = id;
        this.message = message;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Message{" +
                "timestamp=" + timestamp +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
