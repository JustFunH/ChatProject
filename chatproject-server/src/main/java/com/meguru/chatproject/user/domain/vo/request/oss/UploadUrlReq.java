package com.meguru.chatproject.user.domain.vo.request.oss;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Description: 上传url请求入参
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadUrlReq {
    @ApiModelProperty(value = "文件名（带后缀）")
    @NotBlank
    private String fileName;
    @ApiModelProperty(value = "上传场景1.聊天室,2.表情包")
    @NotNull
    private Integer scene;
}
