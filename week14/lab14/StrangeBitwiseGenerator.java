import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period){
        this.period = period;
        state = 0;
    }

    private double normalize(){
        return state * 1.0 / period - 1;
    }

    @Override
    public double next() {
        state = (state + 1);
        int weirdState = state & (state >> 7) % period;
        return weirdState* 1.0 / period - 1;
    }
}
