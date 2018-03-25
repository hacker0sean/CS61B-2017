package db;

public class StringEntry extends DateEntry<String> {

    public StringEntry(String value) throws RuntimeException{
        super(false, false, value.trim(), "string");
        if (value.equals("NOVALUE")){
            value = null;
            super.NOVALUE = true;
            return ;
        }
        if (value.charAt(0) != '\'' || value.charAt(value.length() - 1) != '\'')
            throw new RuntimeException("Error : Invalid String");
        realvalue = value();
    }


    @Override
    public String toString() {
        if (NOVALUE)
            return "NOVALUE";
        return value;
    }

    @Override
    public String value(){
        return value;
    }

    public static StringEntry String_plus_String(StringEntry entry1, StringEntry entry2) throws RuntimeException{
        if (entry1.NOVALUE && entry1.NOVALUE)
            return new StringEntry("NOVALUE");
        else if (entry1.NOVALUE || entry2.NOVALUE)
            return new StringEntry("''");
        else {
            String x1 = entry1.realvalue;
            String x2 = entry2.realvalue;
            return new StringEntry(x1.substring(0, x1.length() - 1).concat(x2.substring(1)));
        }
    }

    public static StringEntry String_plus_Literal(StringEntry entry1, String b) throws RuntimeException{
        StringEntry entry2 = new StringEntry(b);
        return String_plus_String(entry1, entry2);
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
