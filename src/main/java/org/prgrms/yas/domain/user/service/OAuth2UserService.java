package org.prgrms.yas.domain.user.service;

import java.util.Map;
import java.util.Optional;
import org.prgrms.yas.domain.user.domain.User;
import org.prgrms.yas.domain.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuth2UserService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final UserRepository userRepository;

  public OAuth2UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Transactional(readOnly = true)
  public Optional<User> findByProviderAndProviderId(String provider, String providerId) {
    return userRepository.findByProviderAndProviderId(
        provider,
        providerId
    );
  }

  @Transactional
  public User signUp(OAuth2User oAuth2User, String provider) {
    String providerId = oAuth2User.getName();
    return findByProviderAndProviderId(
        provider,
        providerId
    ).map(user -> {
       logger.warn(
           "Already exists: {} for (provider: {}, providerId: {})",
           user,
           provider,
           providerId
       );
       return user;
     })
     .orElseGet(() -> {
       Map<String, Object> attributes = oAuth2User.getAttributes();
       Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
       Map<String, Object> accounts = (Map<String, Object>) attributes.get("kakao_account");
       Map<String, Object> accountsDetail = (Map<String, Object>) accounts.get("profile");
       String nickname = (String) properties.get("nickname");
       String profileImageUrl = (String) accountsDetail.get("profile_image_url");
       String email = (String) accounts.get("email");

       return userRepository.save(User.builder()
                                      .name(nickname)
                                      .email(email)
                                      .provider(provider)
                                      .providerId(providerId)
                                      .profileImage(profileImageUrl)
                                      .build());
     });
  }
}
