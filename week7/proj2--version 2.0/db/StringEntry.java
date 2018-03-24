package db;

public class StringEntry extends DateEntry<String> {

    public StringEntry(String value) throws RuntimeException{
        super(false, false, value.trim(), "string");
        if (value.charAt(0) != '\'' || value.charAt(value.length() - 1) != '\'')
            throw new RuntimeException("Error : Invalid String");
        realvalue = value();
        if (value.equals(NOVALUE)){
            value = null;
            super.NOVALUE = true;
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public String value(){
        return realvalue;
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
