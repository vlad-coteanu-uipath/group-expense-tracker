package org.fmi.unibuc.service.impl;

import org.fmi.unibuc.service.FCMTokenService;
import org.fmi.unibuc.domain.FCMToken;
import org.fmi.unibuc.repository.FCMTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link FCMToken}.
 */
@Service
@Transactional
public class FCMTokenServiceImpl implements FCMTokenService {

    private final Logger log = LoggerFactory.getLogger(FCMTokenServiceImpl.class);

    private final FCMTokenRepository fCMTokenRepository;

    public FCMTokenServiceImpl(FCMTokenRepository fCMTokenRepository) {
        this.fCMTokenRepository = fCMTokenRepository;
    }

    @Override
    public FCMToken save(FCMToken fCMToken) {
        log.debug("Request to save FCMToken : {}", fCMToken);

        // Custom code
        // 1. Check if a token with the same appUser exists and update that one
        Optional<FCMToken> existing = fCMTokenRepository.findOneByAppUserId(fCMToken.getAppUserId());
        if(existing.isPresent()) {
            FCMToken existingToken = existing.get();
            existingToken.setToken(fCMToken.getToken());
            return fCMTokenRepository.save(existingToken);
        }

        return fCMTokenRepository.save(fCMToken);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FCMToken> findAll() {
        log.debug("Request to get all FCMTokens");
        return fCMTokenRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FCMToken> findOne(Long id) {
        log.debug("Request to get FCMToken : {}", id);
        return fCMTokenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FCMToken : {}", id);
        fCMTokenRepository.deleteById(id);
    }

    @Override
    public Optional<FCMToken> findOneByAppUserId(Long appUserId) {
        log.debug("Request to get FCMToken by appUserId: {}", appUserId);
        return fCMTokenRepository.findOneByAppUserId(appUserId);
    }

}
