package com.meguru.chatproject.chat.domain.entity.msg;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description: 文件基类
 * @author Meguru
 * @since 2025-05-31
 */


@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("大小（字节）")
    @NotNull
    private Long size;

    @ApiModelProperty("下载地址")
    @NotBlank
    private String url;
}
