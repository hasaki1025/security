package com.boot.security.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName book
 */
@TableName(value ="book")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @JsonProperty(value = "bId")
    private Integer bId;

    /**
     * 
     */
    @JsonProperty(value = "bName")
    private String bName;

    /**
     * 
     */
    @JsonProperty(value = "bType")
    private String bType;

    /**
     * 
     */
    @JsonProperty(value = "bDes")
    private String bDes;

    /**
     * 
     */
    @JsonProperty(value = "bAuthor")
    private String bAuthor;

    /**
     * 
     */
    @Version
    @JsonProperty(value = "bVersion")
    private Integer bVersion;

    /**
     * 
     */
    @TableLogic
    @JsonProperty(value = "bIsBeenDeleted")
    private Integer bIsBeenDeleted;

    /**
     * 
     */
    @JsonProperty(value = "bUri")
    private String bUri;

    /**
     * 
     */
    @JsonProperty(value = "bFiletype")
    private String bFiletype;


    @JsonProperty(value = "ISBN")
    private String ISBN;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}