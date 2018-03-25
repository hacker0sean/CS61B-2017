package db;

import javax.management.relation.RoleUnresolved;

public class notEquals implements Comparison {
    @Override
    public boolean compare(DateEntry Num1, String Num2) throws RuntimeException {
        try {
            if (Num1.NOVALUE)
                return true;
            if (Num1.NaN)
                return true;
            if (Num1.type().equals("string")) {
                StringEntry x = new StringEntry(Num2);
                return ((StringEntry)Num1).realvalue.compareTo(Num2) != 0;
            }
            else {
                if (Num1.type().equals("int"))
                    return ((IntEntry) (Num1)).realvalue() != (Float.parseFloat(Num2));
                else
                    return ((FloatEntry) (Num1)).realvalue() != (Float.parseFloat(Num2));
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Error: Type dismatch!");
        }
    }

    @Override
    public boolean compare2(DateEntry Num1, DateEntry Num2) throws RuntimeException{
        if (Num1.NOVALUE && Num2.NOVALUE)
            return false;
        if (Num1.NOVALUE || Num2.NOVALUE)
            return true;
        if (Num1.type().equals("string")) {
            if (Num2.type().equals("string"))
                return ((StringEntry) Num1).realvalue.compareTo(((StringEntry) Num2).realvalue) != 0;
            else
                throw new RuntimeException("Error: Type dismatch!");
        }
        else {
            if (Num1.NaN && Num2.NaN)
                return false;
            else if (Num1.NaN || Num2.NaN)
                return true;
            if (Num1.type().equals("int") && Num2.type().equals("int"))
                return ((IntEntry) (Num1)).realvalue() != ((IntEntry) (Num2)).realvalue();
            else if (Num1.type().equals("int") && Num2.type().equals("float"))
                return ((IntEntry) (Num1)).realvalue() != ((FloatEntry) (Num2)).realvalue();
            else if (Num1.type().equals("float") && Num2.type().equals("int"))
                return ((FloatEntry) (Num1)).realvalue() != ((IntEntry) (Num2)).realvalue();
            else
                return ((FloatEntry) (Num1)).realvalue() != ((FloatEntry) (Num2)).realvalue();
        }
    }
}
