package pl.zoltowski.damian.problem.coloring.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum MCPDomain {

    DARKSLATEGRAY(1), MAROON(2), DARKGREEN(3), DARKBLUE(4), RED(5), ORANGE(6),
    YELLOW(7), LIME(8), MEDIUMSPRINGGREEN(9), AQUA(10), BLUE(11), FUCHSIA(12),
    DODGERBLUE(13), KHAKI(14), DEEPPINK(15), LIGHTPINK(16);

    private int number;

    public List<MCPDomain> getNDomainValues(int N) {
        if(N > MCPDomain.values().length) {
            throw new IllegalArgumentException("Value greater than existing colors");
        }
        List<MCPDomain> domainValues = new ArrayList<>();
        domainValues.addAll(
          Arrays.asList(MCPDomain.values()).subList(0, N)
        );
        return domainValues;
    }
}
