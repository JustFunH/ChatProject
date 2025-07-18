package com.meguru.chatproject.chat.domain.entity.msg;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * Description: 视频消息入参
 * @author Meguru
 * @since 2025-05-31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class VideoMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("缩略图宽度（像素）")
    @NotNull
    private Integer thumbWidth;

    @ApiModelProperty("缩略图高度（像素）")
    @NotNull
    private Integer thumbHeight;

    @ApiModelProperty("缩略图大小（字节）")
    @NotNull
    private Long thumbSize;

    @ApiModelProperty("缩略图下载地址")
    @NotBlank
    private String thumbUrl;

}
