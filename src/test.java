import java.util.Random;

public class test {
    public static void main(String[] args) {
        Random ran = new Random();
        for (int i = 0; i < 100; i++) {
            int j = ran.nextInt(10);
            System.out.println(j);
        }
    }
}
