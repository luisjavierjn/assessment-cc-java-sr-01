package co.fullstacklabs.battlemonsters.challenge.exceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
public class ValidationError {
    private List<ErrorDetails> violations = new ArrayList<>();

    public List<ErrorDetails> getViolations() {
        return violations;
    }

    public void addViolations(ErrorDetails violation) {
        this.violations.add(violation);
    }
}
