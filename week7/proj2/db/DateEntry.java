package db;

public abstract class DateEntry<T> {
    protected boolean NaN;
    protected boolean NOVALUE;
    protected String value;
    protected String type;
    protected T realvalue;

    public DateEntry(boolean NaN, boolean NOVALUE, String value, String type){
        this.NaN = NaN;
        this.NOVALUE = NOVALUE;
        this.value = value;
        this.type = type;
    }

    public abstract T value();

    public String type(){
        return type;
    }


    public boolean NaN(){
        return NaN;
    }

    public boolean NOVALUE(){
        return NOVALUE;
    }

    public void setNaN(){
        NaN = !NaN;
    }

    public void setNOVALUE(){
        NOVALUE = !NOVALUE;
    }
}
