package pl.zoltowski.damian.problem.einstain.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Domain {

    FIRST(1), SECOND(2), THIRD(3), FOURTH(4), FIFTH(5);

    private int number;
}
