/**
 * @author Beatrice V.
 * @created 07.09.2020 - 16:34
 * @project NetworkLab1
 */
public class Main {
    public static void main(String[] args) {
        try {
            HttpTest.sendGET();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
