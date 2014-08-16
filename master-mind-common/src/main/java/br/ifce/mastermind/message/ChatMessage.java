package br.ifce.mastermind.message;

/**
 * Created by jrocha on 16/08/14.
 */
public class ChatMessage extends AbstractMessage {

    private String content;

    private String author;

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }
}
