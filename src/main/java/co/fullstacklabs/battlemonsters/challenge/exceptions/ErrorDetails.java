package co.fullstacklabs.battlemonsters.challenge.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author FullStack Labs
 * @version 1.0
 * @since 2022-10
 */
@Getter
@Setter
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String details;
}
