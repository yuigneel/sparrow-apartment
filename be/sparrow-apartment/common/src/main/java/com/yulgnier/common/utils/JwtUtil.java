package com.yulgnier.common.utils;

import com.yulgnier.common.exception.LeaseException;
import com.yulgnier.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public final class JwtUtil {
    // 实际中可以把他放在一个配置类常量中，避免硬编码和泄露
    private static final SecretKey secretKey = Keys.hmacShaKeyFor("yulgnier&众生之门&罗小黑战记".getBytes());

    public static String createToken(Long id, String username) {
        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .setSubject("LOGIN_IN")
                .claim("id", id)
                .claim("username", username)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token) {
        if (token == null) {
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }
        try {

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return claimsJws.getBody();

        } catch (ExpiredJwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }


    //     临时测试
//    public static void main(String[] args) {
//        System.out.println(createToken(18800731910L, "王泽林"));
//    }

}

