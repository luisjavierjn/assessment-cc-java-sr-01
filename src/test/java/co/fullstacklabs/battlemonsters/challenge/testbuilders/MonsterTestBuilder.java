package co.fullstacklabs.battlemonsters.challenge.testbuilders;

import co.fullstacklabs.battlemonsters.challenge.model.Monster;
import lombok.Builder;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class MonsterTestBuilder {
    @Builder
    public static Monster monster(Integer id, String name, Integer attack,
                Integer defense, Integer hp, Integer speed, String imageURL) {
        Monster monster = new Monster();
        monster.setId(id);
        monster.setName(name);
        monster.setAttack(attack);
        monster.setDefense(defense);
        monster.setHp(hp);
        monster.setSpeed(speed);
        monster.setImageUrl(imageURL);
        return monster;
    }

    public static class MonsterBuilder {
        private Integer id = 1;
        private String name = "Monster 1";
        private Integer attack = 50;
        private Integer defense = 40;
        private Integer hp = 30;
        private Integer speed = 25;
        private String imageURL = "http://www.dummymnonster.com/1";

        
    }
}
