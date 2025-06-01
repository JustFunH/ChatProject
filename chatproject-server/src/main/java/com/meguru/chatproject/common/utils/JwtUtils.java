package com.meguru.chatproject.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Description: jwt的token生成与解析
 *
 * @author Meguru
 * @since 2025-05-29
 */
@Slf4j
@Component
public class JwtUtils {
    /**
     * token 密钥
     */
    @Value("${chatproject.jwt.secret}")
    private String secret;

    private static final String UID_CLAIM = "uid";
    private static final String CREATED_TIME = "createTime";

    /**
     * JWT 生成 Token
     *
     * @param uid
     * @return
     */
    public String createToken(Long uid) {
        String token = JWT.create()
                .withClaim(UID_CLAIM, uid)
                .withClaim(CREATED_TIME, new Date())
                .sign(Algorithm.HMAC384(secret));
        return token;
    }

    /**
     * 解密 Token
     *
     * @param token
     * @return
     */
    public Map<String, Claim> verifyToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC384(secret)).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
            log.error("decode error, token : {}", token, e);
        }
        return null;
    }

    /**
     * 根据 Token 获取 uid
     *
     * @param token
     * @return
     */
    public Long getUidOrNull(String token) {
        return Optional.ofNullable(verifyToken(token))
                .map(map -> map.get(UID_CLAIM))
                .map(Claim::asLong)
                .orElse(null);
    }

}
