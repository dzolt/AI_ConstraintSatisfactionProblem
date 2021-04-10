package pl.zoltowski.damian.problem.coloring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MCPDomain {

    DARKSLATEGRAY(1), MAROON(2), DARKGREEN(3), DARKBLUE(4), RED(5), ORANGE(6),
    YELLOW(7), LIME(8), MEDIUMSPRINGGREEN(9), AQUA(10), BLUE(11), FUCHSIA(12),
    DODGERBLUE(13), KHAKI(14), DEEPPINK(15), LIGHTPINK(16);

    private int number;
    }
