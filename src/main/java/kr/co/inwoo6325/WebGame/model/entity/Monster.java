package kr.co.inwoo6325.WebGame.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "monster_info")
public class Monster implements Combatant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String name;

    @Builder.Default
    private int level = 1;
    @Builder.Default
    private int maxHp = 50;
    @Builder.Default
    private int currentHp = 50;
    @Builder.Default
    private int attack = 8;
    @Builder.Default
    private int defense = 3;
    @Builder.Default
    private int agility = 4;

    @Builder.Default
    private long expDrop = 10; // Experience gained when this monster is defeated
    @Builder.Default
    private long goldDrop = 5; // Gold gained when this monster is defeated

    // Additional fields could be added later, e.g., item drop list.
}
