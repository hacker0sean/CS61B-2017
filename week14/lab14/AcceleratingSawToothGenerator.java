import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    double multicand;
    int period;
    int state;

    public AcceleratingSawToothGenerator(int period, double multicand) {
        this.period = period;
        this.multicand = multicand;
    }

    private double normalize(){
        return state * 1.0 / period - 1;
    }

    @Override
    public double next() {
        state++;
        if (state % period == 0){
            period = (int) (period * multicand);
            state = 0;
        }
        return normalize();
    }
}
