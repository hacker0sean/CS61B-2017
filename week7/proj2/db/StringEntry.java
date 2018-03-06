package db;

public class StringEntry extends DateEntry<String> {

    public StringEntry(String value){
        super(false, false, value, "String");
    }

    @Override
    public String value(){
        return "\'" + value + "\'";
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
