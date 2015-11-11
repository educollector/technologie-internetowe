package com.ola;

import com.ola.model.Person;
import com.ola.model.Product;
import spark.*;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.*;
import java.util.*;


import static spark.Spark.get;

/**
 * Created by olaskierbiszewska on 24.10.15.
 */
public class Main {

    private static Connection connect = null;
    private static String url;
    private static Statement statement = null;
    private static List<Product> productList = new ArrayList<>();

    static final String[] PORADYWOMAN = {
            "Porada woman nr. 1",
            "Porada woman nr. 2",
    };

    static final String[] PORADYMAN = {
            "Porada man nr. 1",
            "Porada man nr. 3",
    };

    public static void main(String[] args) {
        try {
            url = "jdbc:mysql://localhost:3306/wwsiti";
            connect = DriverManager.getConnection(url, "wwsiti", "wwsi2015!");
            //statement = connect.createStatement();
            //resultSet = statement.executeQuery("SELECT * FROM PRODUCT");

            PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM PRODUCT");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setBrand(resultSet.getString(3));
                product.setPrice(resultSet.getDouble(4));

                productList.add(product);
//                System.out.println("yolo");
//                System.out.print("result 1: " + resultSet.getInt(1));
//                System.out.print("\n");
//                System.out.println("result 2: " + resultSet.getString(2));
//                System.out.print("\n");
//                System.out.println("result 3: " + resultSet.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Spark.staticFileLocation("/static");

        get("/thymeleaf", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("person", new Person("Test Person"));
            model.put("productList", productList);

            return new ModelAndView(model, "hello"); //"hello" to nazwa temlejta html
        }, new ThymeleafTemplateEngine());

        get("/products/:id", (req, resp) -> {
            Product product = null;
            int prodId = Integer.parseInt(req.params(":id"));
            for (int i = 0; i < productList.size(); ++i) {
                product = productList.get(i);
                if(product.getId() == prodId) break;
            }
            Map<String, Object> model = new HashMap<>();
            if(product != null) {
                model.put("product", product);
            }else {

            }

            return new ModelAndView(model, "product");
        }, new ThymeleafTemplateEngine());

        get("/hello", (req, res) -> "Hello World");

        get("/form", (req, res) -> {
            return getForm();
        });

        get("/form/auth", (req, res) -> {
            return getJupiSite();
        });

        get("/numer/:id", (req, res) -> {
            return "id: " + req.params(":id");
        });

        get("/showheaders", (req, res) -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (String header : req.headers()) {
                stringBuilder
                        .append(header)
                        .append("=")
                        .append(req.headers(header))
                        .append("<br/>");
            }
            return stringBuilder.toString();
        });

//        get("/yolo/:name", (req, res) -> {
//            return "Yolo" + req.params(":name" + "attr: " + req.attributes());
//        });

        get("/say/*/to/*", (req, resp) -> {
            return "Nr of splat parameters: " + req.splat().length + "attr: " + req.attributes();
        });

        get("/tips/:name", (req, resp) -> {
                    String tip;
                    String linkToSexSite;
                    Random rand = new Random();
                    String name = req.params(":name");

                    if ((String.valueOf(name).equals("womansite"))) {
                        return getWomanSite();
                    }

                    if ((String.valueOf(name).equals("mansite"))) {
                        return getmanSite();
                    }


                    if ((String.valueOf(name).endsWith("a"))) {
                        int n = rand.nextInt(PORADYWOMAN.length - 1) + 0;
                        tip = PORADYWOMAN[n];
                        linkToSexSite = "<a href=\"womansite\">Woman</a>";
                    } else {
                        int n = rand.nextInt(PORADYMAN.length - 1) + 0;
                        tip = PORADYMAN[n];
                        linkToSexSite = "<a href=\"mansite\">Man</a>";
                    }

                    return
                            "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                                    "<HTML>\n" +
                                    "<BODY>\n" +
                                    "   <H1>Hi</H1>\n" +
                                    "   <P>This is website with tips. \nTip for you" + tip + "</P> \n" +
                                    linkToSexSite +
                                    "</BODY>\n" +
                                    "</HTML>";
                }
        );

    }

    private static String getWomanSite() {
        return "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                "<HTML>\n" +
                "<BODY>\n" +
                "   <H1>Strona super Kobietki</H1>\n" +
                "</BODY>\n" +
                "</HTML>";
    }

    private static String getmanSite() {
        return "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                "<HTML>\n" +
                "<BODY>\n" +
                "   <H1>Strona super Faceta</H1>\n" +
                "</BODY>\n" +
                "</HTML>";
    }

    private static String getJupiSite() {
        return "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                "<HTML>\n" +
                "<BODY>\n" +
                "   <H1>Form valid</H1>\n" +
                "</BODY>\n" +
                "</HTML>";
    }

    private static String getForm() {
        return "<!DOCTYPE html PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\n" +
                "<HTML>\n" +
                "<BODY>\n" +
                "   <H1>Wypełnij formularz</H1>\n" +
                "<form action=\"form/auth\" method=\"get\">" +
                "First name:<br> <input type=\"text\" name=\"firstname\"> <br>" +
                "Last name:<br><input type=\"text\" name=\"lastname\"><br>" +
                "Password:<br><input type=\"text\" name=\"password\">" +
                "<button type=\"submit\">Submit</button><br>" +
                "</form> " +
                "</BODY>\n" +
                "</HTML>";
    }


}
