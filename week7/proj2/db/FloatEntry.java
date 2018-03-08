package db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FloatEntry extends DateEntry<Float> {

    public FloatEntry(String value){
        super(false, false, value, "float");
        realvalue = Float.parseFloat(value);
    }

    @Override
    public Float value(){
        float x = (float) Float.parseFloat(value);
        DecimalFormat fnum = new DecimalFormat("##0.000");
        String dd = fnum.format(x);
        return Float.parseFloat(dd);
    }

    public boolean equals(Object o){
        DateEntry b = (DateEntry) o;
        if (b.NaN() && this.NaN())
            return true;
        else if (b.NaN() || this.NaN())
            return false;

        if (b.NOVALUE() || this.NOVALUE())
            return false;

        if (b.type() != this.type())
            return false;
        else if (b.value != this.value)
            return false;
        return true;
    }

}
