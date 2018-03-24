import lab14lib.Generator;

public class SawToothGenerator implements Generator{
    private int period;
    private int state;

    public SawToothGenerator(int period){
        this.period = period;
        state = 0;
    }

    private double normalize(){
        return state * 1.0 / period - 1;
    }

    @Override
    public double next() {
        state = (state + 1);
        state = state % period;
        return normalize();
    }
}
