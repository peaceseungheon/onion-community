package com.onion.backend.jwt;

import com.onion.backend.entity.JwtBlackList;
import com.onion.backend.repository.JwtBlackListRepository;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtBlackListService {

    private final JwtBlackListRepository jwtBlackListRepository;
    private final JwtUtils jwtUtils;
    @Value("${jwt.expire}")
    private Long jwtExpire;

    public void addToBlackList(String token, LocalDateTime expirationTime, String email){
        JwtBlackList jwtBlackList = new JwtBlackList();
        jwtBlackList.setToken(token);
        jwtBlackList.setExpirationTime(expirationTime);
        jwtBlackList.setEmail(email);
        jwtBlackListRepository.save(jwtBlackList);
    }

    public boolean isBlackListed(String token){
        String email = jwtUtils.getUsernameFromToken(token);
        Optional<JwtBlackList> opJwtBlackList = jwtBlackListRepository.findFirstByEmailOrderByExpirationTimeDesc(email);
        if(opJwtBlackList.isEmpty()){
            return false;
        }
        Date currentTokenExpirationTime = jwtUtils.getExpirationTimeFromToken(token);
        LocalDateTime expirationLocalDateTime = LocalDateTime.ofInstant(
            currentTokenExpirationTime.toInstant(), ZoneId.systemDefault()).minusSeconds(jwtExpire / 1000);

        return opJwtBlackList.get().getExpirationTime().isAfter(expirationLocalDateTime);
    }

    public void removeExpiredTokens(){
        jwtBlackListRepository.deleteByExpirationTimeBefore(Instant.now());
    }
}
