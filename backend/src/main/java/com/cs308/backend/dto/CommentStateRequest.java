package com.cs308.backend.dto;

public class CommentStateRequest {
    private Long commentId;

    public CommentStateRequest() {}

    public CommentStateRequest(Long commentId) {
        this.commentId = commentId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("CommentStateRequest [commentId=").append(commentId)
            .append("]");

        return builder.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((commentId == null) ? 0 : commentId.hashCode());
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
        CommentStateRequest other = (CommentStateRequest) obj;
        if (commentId == null) {
            if (other.commentId != null)
                return false;
        } else if (!commentId.equals(other.commentId))
            return false;
        return true;
    }
}
