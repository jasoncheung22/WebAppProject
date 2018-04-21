package ouhk.comps380f.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String customerName;
    private String subject;
    private String body;
    private int price;
    private int bidprice;
    private int status;
    private String bidusername;

    public String getBidusername() {
        return bidusername;
    }

    public void setBidusername(String bidusername) {
        this.bidusername = bidusername;
    }
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Fetch(FetchMode.SELECT)
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();
    @OneToMany(mappedBy = "item", fetch = FetchType.EAGER,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BidRecord> bidRecord = new ArrayList<>();

    public List<BidRecord> getBidRecord() {
        return bidRecord;
    }

    public void setBidRecord(List<BidRecord> bidRecord) {
        this.bidRecord = bidRecord;
    }

// getters and setters of all properties
    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void deleteComment(Comment comment) {
        comment.setItem(null);
        this.comments.remove(comment);
    }

    public void deleteAttachment(Attachment attachment) {
        attachment.setItem(null);
        this.attachments.remove(attachment);
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBidprice() {
        return bidprice;
    }

    public void setBidprice(int bidprice) {
        this.bidprice = bidprice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
