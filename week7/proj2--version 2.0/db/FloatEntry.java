package db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class FloatEntry extends DateEntry<Float> {

    public FloatEntry(String value) throws RuntimeException{
        super(false, false, value.trim(), "float");
        if (value.equals("NOVALUE")){
            value = null;
            super.NOVALUE = true;
            return ;
        }
        realvalue = Float.parseFloat(value);
    }

    public FloatEntry(){
        super(true, false, "NaN", "float");
    }

    @Override
    public String toString() {
        if (NaN)
            return "NaN";
        if (NOVALUE)
            return value;
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

    public float realvalue(){
        return realvalue;
    }
    public static FloatEntry Int_operator_FloatLiteral(IntEntry entry1, String x2, char operator) throws RuntimeException{
        Float x1 = entry1.realvalue.floatValue();
        FloatEntry transfer = new FloatEntry(x1.toString());
        transfer.NOVALUE = entry1.NOVALUE;
        transfer.NaN = entry1.NaN;
        return Float_operator_Literal(transfer, x2, operator);
    }

    public static FloatEntry Float_operator_Literal(FloatEntry x1, String x2, char operator) throws RuntimeException{
        FloatEntry entry1 = x1;
        FloatEntry entry2 = new FloatEntry(x2);
        return Float_operator_Float(entry1, entry2, operator);
    }

    public static FloatEntry Float_operator_Float(IntEntry entry1, FloatEntry entry2, char operator) throws RuntimeException{
        Float x1 = entry1.realvalue.floatValue();
        FloatEntry transfer = new FloatEntry(x1.toString());
        transfer.NOVALUE = entry2.NOVALUE;
        transfer.NaN = entry2.NaN;
        return Float_operator_Float(transfer, entry2, operator);
    }

    public static FloatEntry Float_operator_Float(FloatEntry entry1, IntEntry entry2, char operator) throws RuntimeException{
        Float x1 = entry2.realvalue.floatValue();
        FloatEntry transfer = new FloatEntry(x1.toString());
        transfer.NOVALUE = entry2.NOVALUE;
        transfer.NaN = entry2.NaN;
        return Float_operator_Float(entry1, transfer, operator);
    }

    public static FloatEntry Float_operator_Float(FloatEntry entry1, FloatEntry entry2, char operator) throws RuntimeException{
        FloatEntry entrynew;
        if (entry1.NaN || entry2.NaN)
            entrynew = new FloatEntry();
        else if (entry1.NOVALUE && entry2.NOVALUE)
            entrynew = new FloatEntry("NOVALUE");
        else if (entry1.NOVALUE || entry2.NOVALUE)
            entrynew = new FloatEntry("0.0");
        else {
            float x1 = entry1.realvalue;
            float x2 = entry2.realvalue;
            if (operator == '/') {
                if (x2 == 0.0)
                    entrynew = new FloatEntry();
                else
                    entrynew = new FloatEntry(((Float) (float) (x1 / x2)).toString());
            } else if (operator == '+') {
                entrynew = new FloatEntry(((Float) (float) (x1 + x2)).toString());
            } else if (operator == '-') {
                entrynew = new FloatEntry(((Float) (float) (x1 - x2)).toString());
            } else {
                entrynew = new FloatEntry(((Float) (float) (x1 * x2)).toString());
            }
        }
        return entrynew;
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
