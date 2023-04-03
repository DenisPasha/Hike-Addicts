package bg.softuni.pathfinder.model.dto.view;

import bg.softuni.pathfinder.model.entities.User;


public class CommentsView {


    private String dateTime;
    private String text;
    private User author;

    public CommentsView() {
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
