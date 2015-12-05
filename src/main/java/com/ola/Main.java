package com.ola;

import com.ola.model.Cart;
import com.ola.model.Order;
import com.ola.model.Person;
import com.ola.model.Product;
import spark.ModelAndView;
import spark.Spark;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Created by olaskierbiszewska on 24.10.15.
 */
public class Main {

    public static void main(String[] args) throws SQLException {

        DatabaseManager databaseManager = new DatabaseManager();
        ViewModelMappingManager viewModelMappingManager = new ViewModelMappingManager(databaseManager);

        Spark.staticFileLocation("/static");

        get("/home", (req, res) -> {
            Map<String, Object> model = viewModelMappingManager.getSharedModel();
            model.put("person", new Person("Test Person"));
            model.put("productList", databaseManager.getAllProducts());
            return new ModelAndView(model, "hello"); //"hello" to nazwa temlejta html
        }, new ThymeleafTemplateEngine());

        get("/products/:id", (req, resp) -> {
            Product product = databaseManager.getProductById(parseInt(req.params(":id")));
            Map<String, Object> model = viewModelMappingManager.getSharedModel();
            if(product != null) {
                model.put("product", product);
                model.put("wasAdded", "true".equals(req.queryParams("wasAddedToCart")));
            }
            return new ModelAndView(model, "product");
        }, new ThymeleafTemplateEngine());

        get("/cartsummary", (req, res) -> {
            Cart cart = databaseManager.getCurrentCart();
            Map<String, Object> model = viewModelMappingManager.getSharedModel();
            model.put("cartItemsList", cart.getCartItemsList());
            model.put("value", String.format("SUMA: %1.2f zł", cart.getCartValue()));
            return new ModelAndView(model, "cartsummary");
        }, new ThymeleafTemplateEngine());

        get("/hello", (req, res) -> "Hello World");

        get("/regulamin", (req, res) -> {
            Map<String, Object> model = viewModelMappingManager.getSharedModel();
            return new ModelAndView(model, "regulamin");
        }, new ThymeleafTemplateEngine());

        get("/kontakt", (req, res) -> {
            Map<String, Object> model = viewModelMappingManager.getSharedModel();
            return new ModelAndView(model, "kontakt");
        }, new ThymeleafTemplateEngine());

        post("/sendMail", (req, res) -> {
            System.out.print("sendMail");
            return null;
        });

        post("/productOrdered", (req, res) -> {
            Cart cart = databaseManager.getCurrentCart();
            Order order = Order.fromRequest(req, cart);
            databaseManager.placeOrder(order);
            // todo powinno wyswietlic zrobiono zamowienie i podsumowanie (albo liste ostatnich zamowien)
            res.redirect("/orderfinished");
            return "productOrdered";
        });

        get("/orderfinished", (req, res) -> {
            System.out.print("przyjęto zamówienie");
            Map<String, Object> model = viewModelMappingManager.getSharedModel();
            return new ModelAndView(model, "orderfinished");
        }, new ThymeleafTemplateEngine());

        get("/addToCart/:productId", (req, res) -> {
            //Kliknięto guzik "Dodaj do koszyka"
            Product product = databaseManager.getProductById(parseInt(req.params(":productId")));
            databaseManager.addToCurrentCart(product);
            res.redirect("/products/" + req.params(":productId") + "?wasAddedToCart=true");
            return null;
        });
    }
}
