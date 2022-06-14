package com.boot.security.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName collect
 */
@TableName(value ="collect")
@Data
public class Collect implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer cId;

    /**
     * 
     */
    private Integer uId;

    /**
     * 
     */
    private Integer bId;

    /**
     * 
     */
    @TableLogic
    private Integer cIsBeenDelete;

    /**
     * 
     */
    @Version
    private Integer cVersion;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}