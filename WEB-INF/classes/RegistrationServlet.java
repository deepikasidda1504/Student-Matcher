import java.io.*;
import java.sql.*;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/register")
public class RegistrationServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String collegeName = request.getParameter("college_name");

        try {

            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Aiven database
            String url =
                "jdbc:mysql://student-matcher-db-deepikasidda1504-511b.k.aivencloud.com:23378/defaultdb?sslMode=REQUIRED";

            String user = "avnadmin";

            String dbPassword = System.getenv("DB_PASSWORD");

            Connection con =
                DriverManager.getConnection(
                    url,
                    user,
                    dbPassword
                );

            // Insert student
            String sql =
                "INSERT INTO students "
              + "(name, email, password, college_name) "
              + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                con.prepareStatement(sql);

            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, collegeName);

            ps.executeUpdate();

            ps.close();
            con.close();

            // ------------------------------------------------
            // SHOW SUCCESS IMMEDIATELY
            // ------------------------------------------------

            out.println("<html>");
            out.println("<head>");
            out.println("<title>Registration Successful</title>");
            out.println("</head>");

            out.println("<body>");

            out.println("<h1>Registration Successful!</h1>");

            out.println("<p>Welcome, "
                    + name
                    + ".</p>");

            out.println("<p>Your account has been created successfully.</p>");

            out.println("<a href='login.html'>Go to Login</a>");

            out.println("</body>");
            out.println("</html>");

            // ------------------------------------------------
            // SEND EMAIL IN BACKGROUND
            // ------------------------------------------------

            final String registeredEmail = email;
            final String registeredName = name;

            new Thread(new Runnable() {

                public void run() {

                    try {

                        String subject =
                            "Student Matcher Registration Successful";

                        String message =
                            "Hello "
                            + registeredName
                            + ",\n\n"
                            + "Your Student Matcher account has been "
                            + "created successfully.\n\n"
                            + "You can now login and use Student Matcher.\n\n"
                            + "Thank you.";

                        EmailService.sendEmail(
                            registeredEmail,
                            subject,
                            message
                        );

                        System.out.println(
                            "Registration email sent to "
                            + registeredEmail
                        );

                    } catch (Exception emailError) {

                        System.out.println(
                            "Registration email failed: "
                            + emailError.getMessage()
                        );
                    }
                }

            }).start();

        } catch (Exception e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>Registration Failed</h2>");

            out.println("<p>Error: "
                    + e.getMessage()
                    + "</p>");

            out.println("<a href='registration.html'>Try Again</a>");

            out.println("</body>");
            out.println("</html>");
        }
    }
}