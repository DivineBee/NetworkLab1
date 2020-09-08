/**
 * @author Beatrice V.
 * @created 07.09.2020 - 16:34
 * @project NetworkLab1
 */
public class Main {
    public static void main(String[] args) {
        try {
            HttpTest.getToken();
            HttpTest.getPageContent("http://localhost:5000/home");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
