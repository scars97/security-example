package com.example.security.utils;

import com.example.security.domain.entity.Member;
import com.example.security.domain.enums.MemberRole;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class JwtTokenUtils {

    private static SecretKey secretKey;

    public JwtTokenUtils(@Value("${spring.jwt.secret-key}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public static String generateJwtToken(Member member) {
        return Jwts.builder()
                .subject(member.getMemberId())
                .claims(createClaims(member))
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(createExpireDateForOneYear())
                .signWith(secretKey)
                .compact();
    }

    public static boolean isValidToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            log.info("expireTime :{}", claims.getExpiration());
            log.info("email :{}", claims.get("memberId"));
            log.info("role :{}", claims.get("role"));
            return true;

        } catch (ExpiredJwtException exception) {
            log.error("Token Expired");
            return false;
        } catch (JwtException exception) {
            log.error("Token Tampered");
            return false;
        } catch (NullPointerException exception) {
            log.error("Token is null");
            return false;
        }
    }

    public static String getTokenFromHeader(String header) {
        return header.split(" ")[1];
    }

    private static Date createExpireDateForOneYear() {
        // 토큰 만료시간 1분
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 1);
        return c.getTime();
    }

    private static Map<String, Object> createClaims(Member member) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("memberId", member.getMemberId());
        claims.put("role", member.getRole());

        return claims;
    }

    private static String getMemberIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("memberId", String.class);
    }

    private static MemberRole getRoleFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.get("role", MemberRole.class);
    }

    private static Claims getClaimsFromToken(String token) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }
}
