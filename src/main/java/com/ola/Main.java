package com.ola;

import com.ola.model.Person;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


import static spark.Spark.get;

/**
 * Created by olaskierbiszewska on 24.10.15.
 */
public class Main {

    static final String[] PORADYWOMAN = {
            "Porada woman nr. 1",
            "Porada woman nr. 2",
            "Porada woman nr. 3",
            "Porada woman nr. 4",
            "Porada woman nr. 5",
            "Porada woman nr. 6",
            "Porada woman nr. 7",
            "Porada woman nr. 8"
    };

    static final String[] PORADYMAN = {
            "Porada man nr. 1",
            "Porada man nr. 3",
            "Porada man nr. 4",
            "Porada man nr. 5",
            "Porada man nr. 6",
            "Porada man nr. 7",
            "Porada man nr. 8"
    };

    public static void main(String[] args) {

        get("/thymeleaf", (req, res) -> {
            Map<String, Object> model = new HashMap<>();

            model.put("person", new Person("Foobar"));

            return new ModelAndView(model, "hello");
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

        get("/yolo/:name", (req, res) -> {
            return "Yolo" + req.params(":name" + "attr: " + req.attributes());
        });

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
