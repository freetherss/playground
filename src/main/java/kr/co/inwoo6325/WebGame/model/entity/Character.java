package kr.co.inwoo6325.WebGame.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Getter // 모든 필드에 Getter 자동 생성
@Setter // 모든 필드에 Setter 자동 생성
@NoArgsConstructor // 기본 생성자 자동 생성
@AllArgsConstructor // 모든 필드를 포함하는 생성자 자동 생성
@Builder
@Table(name = "character_info")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK: UserAccount.id와 연결 (DIP 준수: 엔티티는 다른 엔티티에 의존)
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user; 

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 20)
    private String job; 

    @Builder.Default
    private int level = 1;
    @Builder.Default
    private long exp = 0;
    @Builder.Default
    private long gold = 0;
    
    @Column(name = "current_map", nullable = false, length = 50)
    @Builder.Default
    private String currentMap = "starting_village";

    // Getter/Setter 및 생성자 생략
    // ...
}