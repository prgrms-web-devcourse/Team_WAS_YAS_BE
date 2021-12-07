package org.prgrms.yas.domain.user.domain;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.internal.CoreMessageLogger;
import org.prgrms.yas.domain.routine.domain.RoutineCategory;
import org.prgrms.yas.domain.routine.domain.Week;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false,length = 20)
  private String name;

  @Column(nullable = false,length = 30)
  private String nickname;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

//  @Column(nullable = false, columnDefinition = "TEXT")
//  private String userImage;

  @Column(nullable = false, columnDefinition = "TINYINT default false")
  private boolean isDeleted;

  @Builder
  public User(String name, String nickname, String email, String password) {
    this.name = name;
    this.nickname = nickname;
    this.email = email;
    this.password = password;
  }


}
