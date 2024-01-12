package co.fullstacklabs.battlemonsters.challenge.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Entity
@Table(name = "MONSTER")
@Getter
@Setter
@NoArgsConstructor
public class Monster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "ATTACK", nullable = false)
    private Integer attack;

    @Column(name = "DEFENSE", nullable = false)
    private Integer defense;

    @Column(name = "HP", nullable = false)
    private Integer hp;

    @Column(name = "SPEED", nullable = false)
    private Integer speed;

    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;

}
