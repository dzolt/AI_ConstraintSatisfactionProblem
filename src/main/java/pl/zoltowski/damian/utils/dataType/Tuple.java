package pl.zoltowski.damian.utils.dataType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Tuple<X, Y> implements Cloneable{
    public X first;
    public Y second;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "<" + this.first + ", " + this.second + ">";
    }
}
