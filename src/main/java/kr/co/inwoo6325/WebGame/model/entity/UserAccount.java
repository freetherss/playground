package kr.co.inwoo6325.WebGame.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Getter // 모든 필드에 Getter 자동 생성
@Setter // 모든 필드에 Setter 자동 생성
@NoArgsConstructor
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
@Table(name = "user_account") // 테이블명 지정
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username; // 로그인 ID

    @Column(nullable = false, length = 255)
    private String password; // 암호화된 비밀번호

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}