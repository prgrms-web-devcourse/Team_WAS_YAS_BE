package org.prgrms.yas.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import lombok.Getter;

@Getter
public class BaseEntity {

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
