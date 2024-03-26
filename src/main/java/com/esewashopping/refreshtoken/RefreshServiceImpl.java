package com.esewashopping.refreshtoken;

import com.esewashopping.customer.CustomerRepo;
import com.esewashopping.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshServiceImpl implements RefreshService{

    private final CustomerRepo customerRepo;

    private final RefreshRepo refreshRepo;

    @Override
    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken=RefreshToken.builder()
                .customer(customerRepo.findByEmail(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusSeconds(86400))
                .build();
        return refreshRepo.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now())<0){
            refreshRepo.delete(token);
            throw new NotFoundException("Need to Login");
        }
        return token;
    }
}
