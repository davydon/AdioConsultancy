package com.management.model.Settings;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TwoParam {

    @NotNull
    @Min(1L)
    private Long id;
    @NotBlank(
            message = "required"
    )
    private String content;

    public TwoParam() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
