package com.meguru.chatproject.common.exception;


import com.google.common.base.Charsets;
import com.meguru.chatproject.common.domain.vo.response.ApiResult;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.json.JSONUtil;

import java.io.IOException;

/**
 * Description: 业务校验异常码
 *
 * @author Meguru
 * @since 2025-05-29
 */
@AllArgsConstructor
@Getter
public enum HttpErrorEnum implements ErrorEnum {
    ACCESS_DENIED(401, "登录失效，请重新登录"),
    ;
    private Integer httpCode;
    private String msg;

    @Override
    public Integer getErrorCode() {
        return httpCode;
    }

    @Override
    public String getErrorMsg() {
        return msg;
    }

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(this.getErrorCode());
        ApiResult responseData = ApiResult.fail(this);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JSONUtil.toJsonStr(responseData));
    }
}
