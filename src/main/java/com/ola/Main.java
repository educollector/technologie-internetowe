package com.ola;

import com.ola.model.Cart;
import com.ola.model.CartItem;
import com.ola.model.Person;
import com.ola.model.Product;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.awt.font.ShapeGraphicAttribute;
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
    private static Cart cart;

    public static void main(String[] args) {
        cart = new Cart();
        try {
            url = "jdbc:mysql://localhost:3306/wwsiti";
            connect = DriverManager.getConnection(url, "wwsiti", "wwsi2015!");
            PreparedStatement preparedStatement = connect.prepareStatement("SELECT * FROM PRODUCT");
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt(1));
                product.setName(resultSet.getString(2));
                product.setBrand(resultSet.getString(3));
                product.setPrice(resultSet.getDouble(4));
                productList.add(product);
            }

            PreparedStatement preparedStatement1 = connect.prepareStatement("SELECT * FROM CART");
            ResultSet resultSet1 = preparedStatement1.executeQuery();

            cart = new Cart();
            int counter = 0;
            while (resultSet1.next()){
                if(counter==0){
                    cart.setId(resultSet1.getInt(1));
                    break;
                }
                ++counter;
            }

            PreparedStatement preparedStatement2 = connect.prepareStatement("SELECT * FROM CARTSPRODUCTS");
            ResultSet resultSet2 = preparedStatement2.executeQuery();
            while (resultSet2.next()){
                CartItem item = new CartItem();
                item.setProductId(resultSet2.getInt(1));
                item.setAmout(resultSet2.getInt(3));
                cart.getCartItemsList().add(item);

            }

        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        Spark.staticFileLocation("/static");

        get("/home", (req, res) -> {
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
                model.put("wasAdded", "true".equals(req.queryParams("wasAddedToCart")));
            }
            return new ModelAndView(model, "product");
        }, new ThymeleafTemplateEngine());

        get("/hello", (req, res) -> "Hello World");

        get("/form", (req, res) -> {
            return getForm();
        });

        get("/form/auth", (req, res) -> {
            //Kliknięto guzik "Dodaj do koszyka"

//            CartItem item = new CartItem();
//            item.setProductId(resultSet2.getInt(1));
//            item.setAmout(resultSet2.getInt(3));
//            cart.getCartItemsList().add(item);

            return getJupiSite();
        });

        get("/addToCart/:productId", (req, res) -> {
            //Kliknięto guzik "Dodaj do koszyka"

            CartItem item = new CartItem();
            item.setProductId(Integer.parseInt(req.params(":productId")));
            item.setAmout(Integer.parseInt("1"));
            cart.getCartItemsList().add(item);
            saveCartItem(item,cart);
            res.redirect("/products/" + req.params(":productId") + "?wasAddedToCart=true");
            return null;
            //return getJupiSite();
        });

//        get("/numer/:id", (req, res) -> {
//            return "id: " + req.params(":id");
//        });
//
//        get("/showheaders", (req, res) -> {
//            StringBuilder stringBuilder = new StringBuilder();
//            for (String header : req.headers()) {
//                stringBuilder
//                        .append(header)
//                        .append("=")
//                        .append(req.headers(header))
//                        .append("<br/>");
//            }
//            return stringBuilder.toString();
//        });
//
//        get("/say/*/to/*", (req, resp) -> {
//            return "Nr of splat parameters: " + req.splat().length + "attr: " + req.attributes();
//        });

    }

    private static void saveCartItem(CartItem item, Cart cart) {
        try {
            url = "jdbc:mysql://localhost:3306/wwsiti";
            connect = DriverManager.getConnection(url, "wwsiti", "wwsi2015!");
            String checkQuery = "SELECT amount FROM CARTSPRODUCTS where id_cart="+  cart.getId() + " AND id_product=" + item.getProductId();
            PreparedStatement preparedStatementCheck = connect.prepareStatement(checkQuery);
            ResultSet resultSet = preparedStatementCheck.executeQuery();

            Integer i = 0;
            while (resultSet.next()) {
                i = resultSet.getInt(1);
            }

            Integer amount = item.getAmout();
            String query;
            if(i>0){
                amount +=i;
                query= "UPDATE CARTSPRODUCTS SET amount=" + amount + " WHERE id_product=" + item.getProductId()
                        + " AND id_cart=" + cart.getId();
            }else{
                query ="INSERT INTO CARTSPRODUCTS (id_product, amount, id_cart) VALUES ("
                        +item.getProductId() + "," + amount  + "," + cart.getId() + ")";
            }
            PreparedStatement preparedStatement = connect.prepareStatement(query);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return;
        }
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

//    if ((String.valueOf(name).endsWith("a"))) {
//        int n = rand.nextInt(PORADYWOMAN.length - 1) + 0;
//        tip = PORADYWOMAN[n];
//        linkToSexSite = "<a href=\"womansite\">Woman</a>";
//    }


}
