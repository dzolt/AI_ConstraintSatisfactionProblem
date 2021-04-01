package pl.zoltowski.damian.utils.dataType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Segment {

    private Point start;
    private Point end;
}
