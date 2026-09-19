import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Receive data from front.html
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String college_name = request.getParameter("college_name");

        // MySQL connection details
        String url = "jdbc:mysql://student-matcher-db-deepikasidda1504-511b.k.aivencloud.com:23378/defaultdb?sslMode=REQUIRED";
String user = "avnadmin";
String dbPassword = System.getenv("DB_PASSWORD");
        // SQL query
        String sql = "INSERT INTO students "
                   + "(name, email, password, college_name) "
                   + "VALUES (?, ?, ?, ?)";

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        try {

            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            Connection con = DriverManager.getConnection(
                    url,
                    user,
                    dbPassword
            );

            // Prepare SQL query
            PreparedStatement ps = con.prepareStatement(sql);

            // Put values into ?
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, college_name);

            // Execute INSERT query
            ps.executeUpdate();

            // ==============================
            // SEND REGISTRATION EMAIL
            // ==============================

            String subject =
                    "Student Matcher - Registration Successful";

            String message =
                    "Hello " + name + ",\n\n"
                  + "Your registration in Student Matcher "
                  + "was successful.\n\n"
                  + "Welcome to Student Matcher!\n"
                  + "You can now login and add your skills.\n\n"
                  + "Thank you,\n"
                  + "Student Matcher Team";

            EmailService.sendEmail(
                    email,
                    subject,
                    message
            );

            // ==============================
            // DISPLAY SUCCESS MESSAGE
            // ==============================

            out.println("<html>");
            out.println("<body>");

            out.println("<h1>Registration Successful!</h1>");

            out.println("<p>Welcome "
                    + name
                    + "</p>");

            out.println("<p>Thank you for registering.</p>");

            out.println("<p>"
                    + "A confirmation email has been sent to "
                    + email
                    + "</p>");

            out.println("<a href='login.html'>"
                    + "Go to Login"
                    + "</a>");

            out.println("</body>");
            out.println("</html>");

            // Close resources
            ps.close();
            con.close();

        } catch (Exception e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h1>Registration Failed</h1>");

            out.println("<p>Error: "
                    + e.getMessage()
                    + "</p>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}