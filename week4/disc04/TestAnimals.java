public class TestAnimals {
    public static void main(String[] args) {
        Animal a = new Animal("Pluto", 10);
        Cat c = new Cat("Garfield", 6);
        Dog d = new Dog("Fido", 4);

        a.greet(); // (A) Huh?
        c.greet(); // (B) Meow!
        d.greet(); // (C) WOOF!
        a = c;
        ((Cat) a).greet(); // (D) Meow!
        a.greet(); // (E) Meow!
        a = new Dog("Spot", 10);
        d = a;
    }
}
