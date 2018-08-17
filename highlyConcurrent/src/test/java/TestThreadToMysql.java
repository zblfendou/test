public class TestThreadToMysql {
    public static void main(String[] args) {
        for (int i = 1; i <=100; i++) {
            String teacherName=String.valueOf(i);
            new ThreadToMysql(teacherName, "123456").start();
        }
    }
}
