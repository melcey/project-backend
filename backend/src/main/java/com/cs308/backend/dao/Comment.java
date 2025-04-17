package com.cs308.backend.dao;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// The entity Comment
@Entity
// Corresponds to the `comments` table in the database
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    // Many comments-one product
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
    private Product commentedProduct;

    // Many comments-one user
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User commentingUser;

    // boolean field (not Boolean, the wrapper type)
    @Column(name = "approved")
    private boolean approved = false;

    @Column(name = "comment")
    private String comment;

    @Column(name = "comment_date")
    private LocalDateTime commentDate;

    public Comment() {}

    public Comment(Product commentedProduct, User commentingUser, String comment) {
        this.commentedProduct = commentedProduct;
        this.commentingUser = commentingUser;
        this.comment = comment;
        this.commentDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Product getCommentedProduct() {
        return commentedProduct;
    }

    public void setCommentedProduct(Product commentedProduct) {
        this.commentedProduct = commentedProduct;
    }

    public User getCommentingUser() {
        return commentingUser;
    }

    public void setCommentingUser(User commentingUser) {
        this.commentingUser = commentingUser;
    }

    public boolean getIsApproved() {
        return approved;
    }

    public void setIsApproved(boolean approved) {
        this.approved = approved;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(LocalDateTime commentDate) {
        this.commentDate = commentDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comment other = (Comment) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}