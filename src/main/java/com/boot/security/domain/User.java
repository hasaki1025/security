package com.boot.security.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type=IdType.ASSIGN_ID)
    private Long uId;

    /**
     * 
     */
    private String uName;

    /**
     * 
     */
    private String uEmail;

    /**
     * 
     */
    private Integer uIsBeenDeleted;

    /**
     * 
     */
    private Integer uVersion;

    /**
     * 
     */
    private String uPassword;

    /**
     * 
     */
    private Integer admin;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}