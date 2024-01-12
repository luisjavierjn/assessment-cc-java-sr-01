package co.fullstacklabs.battlemonsters.challenge.service.game;

import co.fullstacklabs.battlemonsters.challenge.dto.BattleDTO;
import co.fullstacklabs.battlemonsters.challenge.dto.MonsterDTO;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

import static co.fullstacklabs.battlemonsters.challenge.service.game.Event.GAME_EVT_ATTACK;
import static co.fullstacklabs.battlemonsters.challenge.service.game.Event.GAME_EVT_DAMAGE;
import static co.fullstacklabs.battlemonsters.challenge.service.game.Event.GAME_EVT_END;
import static co.fullstacklabs.battlemonsters.challenge.service.game.Event.GAME_EVT_NEUTRAL;
import static co.fullstacklabs.battlemonsters.challenge.service.game.Event.GAME_EVT_START;
import static co.fullstacklabs.battlemonsters.challenge.service.game.State.GAME_STT_CALC_DAMAGE;
import static co.fullstacklabs.battlemonsters.challenge.service.game.State.GAME_STT_END;
import static co.fullstacklabs.battlemonsters.challenge.service.game.State.GAME_STT_FIGHT;
import static co.fullstacklabs.battlemonsters.challenge.service.game.State.GAME_STT_HOLD;
import static co.fullstacklabs.battlemonsters.challenge.service.game.State.GAME_STT_NEUTRAL;
import static co.fullstacklabs.battlemonsters.challenge.service.game.State.GAME_STT_START;

@Component
public class BattleStateMachine {

    private Event event;
    private State currentState;
    private State previousState;
    private BattleDTO battleDTO;
    private final Queue<Event> queue;

    MonsterDTO monsterOne;
    MonsterDTO monsterTwo;
    MonsterDTO monsterTmp;
    Integer damage;

    public BattleStateMachine() {
        this.queue = new LinkedList<>();
    }

    public Queue<Event> getQueue() {
        return queue;
    }

    public void check(Event evt, BattleDTO battleDTO) {
        if(this.battleDTO != battleDTO) {
            hold();
            this.battleDTO = battleDTO;
        }
        exec(t(currentState, evt));
    }

    private State t(State initialState, Event evt) {
        event = evt;

        switch (initialState) {
            case GAME_STT_HOLD:
                if (event == GAME_EVT_START) return GAME_STT_START;
                break;
            case GAME_STT_START:
                if (event == GAME_EVT_ATTACK) return GAME_STT_FIGHT;
                break;
            case GAME_STT_FIGHT:
                if (event == GAME_EVT_DAMAGE) return GAME_STT_CALC_DAMAGE;
                break;
            case GAME_STT_CALC_DAMAGE:
                if (event == GAME_EVT_ATTACK) return GAME_STT_FIGHT;
                if (event == GAME_EVT_END) return GAME_STT_END;
                break;
            case GAME_STT_END:
                if (event == GAME_EVT_NEUTRAL) return GAME_STT_HOLD;
                break;
        }

        return GAME_STT_NEUTRAL;
    }

    private State exec(State state) {
        if (state != GAME_STT_NEUTRAL) {
            previousState = currentState;
            currentState = state;
        }

        switch (state) {
            case GAME_STT_HOLD:
                hold();
                break;
            case GAME_STT_START:
                start();
                break;
            case GAME_STT_FIGHT:
                fight();
                break;
            case GAME_STT_CALC_DAMAGE:
                calcDamage();
                break;
            case GAME_STT_END:
                end();
                break;
        }

        return state;
    }

    private void hold() {
        this.battleDTO = null;
        this.event = GAME_EVT_NEUTRAL;
        this.currentState = GAME_STT_HOLD;
        this.previousState = GAME_STT_HOLD;
    }

    private void start() {
        MonsterDTO monsterA = battleDTO.getMonsterA().clone();
        MonsterDTO monsterB = battleDTO.getMonsterB().clone();

        if(monsterA.getSpeed() > monsterB.getSpeed()) {
            monsterOne = monsterA;
            monsterTwo = monsterB;
        } else if(monsterA.getSpeed() < monsterB.getSpeed()) {
            monsterOne = monsterB;
            monsterTwo = monsterA;
        } else {
            if(monsterA.getAttack() > monsterB.getAttack()) {
                monsterOne = monsterA;
                monsterTwo = monsterB;
            } else if(monsterA.getAttack() < monsterB.getAttack()) {
                monsterOne = monsterB;
                monsterTwo = monsterA;
            } else {
                monsterOne = monsterA;
                monsterTwo = monsterB;
            }
        }

        getQueue().add(GAME_EVT_ATTACK);
    }

    private void fight() {
        if(previousState == GAME_STT_CALC_DAMAGE) {
            monsterTmp = monsterOne;
            monsterOne = monsterTwo;
            monsterTwo = monsterTmp;
        }

        getQueue().add(GAME_EVT_DAMAGE);
    }

    private void calcDamage() {
        damage = monsterOne.getAttack() <= monsterTwo.getDefense() ? 1
                : monsterOne.getAttack() - monsterTwo.getDefense();
        monsterTwo.setHp(monsterTwo.getHp() - damage);

        if(monsterTwo.getHp() <= 0) getQueue().add(GAME_EVT_END);
        else getQueue().add(GAME_EVT_ATTACK);
    }

    private void end() {
        battleDTO.setWinner(monsterOne.getHp() <= 0 ? monsterTwo : monsterOne);
        getQueue().add(GAME_EVT_NEUTRAL);
    }
}
