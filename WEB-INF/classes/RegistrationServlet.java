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

            Class.forName("com.mysql.cj.jdbc.Driver");

            String url =
                "jdbc:mysql://student-matcher-db-deepikasidda1504-511b.k.aivencloud.com:23378/defaultdb?sslMode=REQUIRED";

            String user = "avnadmin";

            String dbPassword =
                System.getenv("DB_PASSWORD");

            Connection con =
                DriverManager.getConnection(
                    url,
                    user,
                    dbPassword
                );

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


            // ==========================================
            // REGISTRATION SUCCESS PAGE
            // ==========================================

            out.println("<!DOCTYPE html>");
            out.println("<html>");

            out.println("<head>");

            out.println("<title>Registration Successful</title>");

            // EmailJS Browser SDK
            out.println(
                "<script src='https://cdn.jsdelivr.net/npm/@emailjs/browser@4/dist/email.min.js'></script>"
            );

            out.println("</head>");

            out.println("<body>");

            out.println(
                "<h1>Registration Successful!</h1>"
            );

            out.println(
                "<p>Welcome, "
                + escapeHtml(name)
                + ".</p>"
            );

            out.println(
                "<p>Your account has been created successfully.</p>"
            );


            // ==========================================
            // SEND REGISTRATION EMAIL USING EMAILJS
            // ==========================================

            String emailSubject =
                "Registration Successful";

            String emailMessage =
                "Your Student Matcher account has been "
                + "created successfully.\n\n"
                + "You can now login and use Student Matcher.\n\n"
                + "Thank you.";

            out.println("<script>");

            out.println(
                "emailjs.init({"
                + "publicKey: 'dCGkD7eJKsjTOzVmt'"
                + "});"
            );

            out.println(
                "emailjs.send("
                + "'service_5ae6909',"
                + "'template_400i7hb',"
                + "{"
                + "to_email: '" + escapeJavaScript(email) + "',"
                + "subject: '" + escapeJavaScript(emailSubject) + "',"
                + "message: '" + escapeJavaScript(emailMessage) + "',"
                + "name: '" + escapeJavaScript(name) + "'"
                + "}"
                + ").then("
                + "function(response) {"
                + "console.log('Registration email sent successfully');"
                + "},"
                + "function(error) {"
                + "console.log('Registration email failed:', error);"
                + "}"
                + ");"
            );

            out.println("</script>");


            out.println("<br>");

            out.println(
                "<a href='login.html'>Go to Login</a>"
            );

            out.println("</body>");
            out.println("</html>");


        } catch (Exception e) {

            out.println("<html>");
            out.println("<body>");

            out.println("<h2>Registration Failed</h2>");

            out.println(
                "<p>Error: "
                + escapeHtml(e.getMessage())
                + "</p>"
            );

            out.println(
                "<a href='front.html'>Try Again</a>"
            );

            out.println("</body>");
            out.println("</html>");
        }
    }


    // ==========================================
    // ESCAPE JAVASCRIPT
    // ==========================================

    private String escapeJavaScript(String text) {

        if (text == null) {
            return "";
        }

        return text
            .replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("\r", "\\r")
            .replace("\n", "\\n");
    }


    // ==========================================
    // ESCAPE HTML
    // ==========================================

    private String escapeHtml(String text) {

        if (text == null) {
            return "";
        }

        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;");
    }
}