package org.prgrms.yas.domain;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
  
  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private ZonedDateTime createdAt;
  
  @LastModifiedDate
  @Column(name = "updated_at")
  private ZonedDateTime updatedAt;
}
