package com.cs308.backend.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private ProductResponse commentedProduct;
    private Long userId;
    private boolean approved;
    private String comment;
    private LocalDateTime commentDate;

    public CommentResponse() {}

    public CommentResponse(Long id, ProductResponse commentedProduct, Long userId, boolean approved, String comment,
            LocalDateTime commentDate) {
        this.id = id;
        this.commentedProduct = commentedProduct;
        this.userId = userId;
        this.approved = approved;
        this.comment = comment;
        this.commentDate = commentDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductResponse getCommentedProduct() {
        return commentedProduct;
    }

    public void setCommentedProduct(ProductResponse commentedProduct) {
        this.commentedProduct = commentedProduct;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
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
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CommentResponse [id=").append(id)
            .append(", commentedProduct=").append(commentedProduct)
            .append(", userId=").append(userId)
            .append(", approved=").append(approved)
            .append(", comment=").append(comment)
            .append(", commentDate=").append(commentDate)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((commentedProduct == null) ? 0 : commentedProduct.hashCode());
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + (approved ? 1231 : 1237);
        result = prime * result + ((comment == null) ? 0 : comment.hashCode());
        result = prime * result + ((commentDate == null) ? 0 : commentDate.hashCode());
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
        CommentResponse other = (CommentResponse) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (commentedProduct == null) {
            if (other.commentedProduct != null)
                return false;
        } else if (!commentedProduct.equals(other.commentedProduct))
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (approved != other.approved)
            return false;
        if (comment == null) {
            if (other.comment != null)
                return false;
        } else if (!comment.equals(other.comment))
            return false;
        if (commentDate == null) {
            if (other.commentDate != null)
                return false;
        } else if (!commentDate.equals(other.commentDate))
            return false;
        return true;
    }
}
