package db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FloatEntry extends DateEntry<Float> {

    public FloatEntry(String value) throws RuntimeException{
        super(false, false, value.trim(), "float");
        realvalue = Float.parseFloat(value);
        if (value.equals(NOVALUE)){
            value = null;
            super.NOVALUE = true;
        }

    }

    public FloatEntry(){
        super(true, false, "NaN", "float");
    }

    @Override
    public String toString() {
        float x = Float.parseFloat(value);
        DecimalFormat fnum = new DecimalFormat("##0.000");
        String dd = fnum.format(x);
        Float result = (Float.parseFloat(dd));
        return result.toString();
    }

    @Override
    public Float value(){
        return Float.parseFloat(value);
    }

    public boolean equals(Object o){
        DateEntry b = (DateEntry) o;
        if (b.NaN() && this.NaN())
            return true;
        else if (b.NaN() || this.NaN())
            return false;

        if (b.NOVALUE() || this.NOVALUE())
            return false;

        if (!b.type().equals(this.type()))
            return false;
        else if (!b.value.equals(this.value))
            return false;
        return true;
    }

}
