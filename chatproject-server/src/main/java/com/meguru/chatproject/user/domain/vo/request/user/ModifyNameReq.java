package com.meguru.chatproject.user.domain.vo.request.user;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;


/**
 * Description: 修改用户名
 *
 * @author Meguru
 * @since 2025-05-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyNameReq {

    @NotNull
    @Length(max = 8, message = "用户名长度最高8位")
    @ApiModelProperty("用户名")
    private String name;

}
