package ca.jrvs.apps.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Data {
    private String id;
    private String text;
    @JsonProperty("created_at")
    private Date createdAt;
    private Entities entities;
    @JsonProperty("public_metrics")
    private PublicMetrics publicMetrics;
    private Boolean deleted = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Entities getEntities() {
        return entities;
    }

    public void setEntities(Entities entities) {
        this.entities = entities;
    }

    public PublicMetrics getPublicMetrics() {
        return publicMetrics;
    }

    public void setPublicMetrics(PublicMetrics publicMetrics) {
        this.publicMetrics = publicMetrics;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Data{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + createdAt +
                ", entities=" + entities +
                ", publicMetrics=" + publicMetrics +
                ", deleted=" + deleted +
                '}';
    }
}
