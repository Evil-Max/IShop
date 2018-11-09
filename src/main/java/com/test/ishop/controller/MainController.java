package com.test.ishop.controller;

import com.test.ishop.domain.*;
import com.test.ishop.repos.CategoryRepo;
import com.test.ishop.repos.ProductRepo;
import com.test.ishop.config.FakeClass;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@Controller
public class MainController {

    private final static Logger LOGGER = Logger.getLogger(MainController.class);

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private FakeClass fakeClass;

    private Long activeCategory;

    private Iterable<Category> categories;
    private Iterable<Product> products;

    private String alert="";

    private void fillModel(Model model) {
        model.addAttribute("activeCategory",activeCategory);
        model.addAttribute("categories",categories);
        model.addAttribute("products",products);
        if (!alert.isEmpty())
        model.addAttribute("message",alert);
        alert="";
    }

    @GetMapping("/")
    public String main(Model model) {
        LOGGER.info("main is called");
        LOGGER.info(fakeClass.toString());
        categories = categoryRepo.findAll();
        products = productRepo.findAll();
        activeCategory=0L;
        fillModel(model);
        return "main";
    }

    @GetMapping("/category/{category}")
    public String categoryFilter(
            @PathVariable Category category,
            Model model
    ) {
        LOGGER.info("category/"+category.getId()+" is called");

        categories = categoryRepo.findAll();
        Long id=0L;

        if (category!=null) {
            products = productRepo.findByCategoryId(category.getId());
            id=category.getId();
        } else {
            products = productRepo.findAll();
        }
        activeCategory=id;
        fillModel(model);
        return "main";
    }

    @PostMapping("/alert/{message}")
    public String alertShow(
            @PathVariable("message") String message,
            Model model
    ) {
        LOGGER.info("alert is called. message="+message);
        alert = message.replace('+',' ');

        return "redirect:/";
    }

    private String getSQLString(String field, char type, Object o1,Object o2,String last) {
        String result="";
        String var=field;
        if (!((o1==null)&&(o2==null))) {
            if (type == 'N') {
                if (field==null) var="value_n";
                if ((o1!=null)&&(o2!=null)&&(o1.equals(o2))&&(!o1.toString().isEmpty())) {
                    result=var+"="+o1.toString();
                } else {
                    if ((o1 != null) && (!o1.toString().isEmpty())) result = var + ">=" + o1.toString();
                    if ((o1 != null) && (o2 != null) && (!o1.toString().isEmpty()) && (!o2.toString().isEmpty()))
                        result += " and ";
                    if ((o2 != null) && (!o2.toString().isEmpty())) result += var + "<=" + o2.toString();
                }
            } else if (type == 'S') {
                if (field==null) var="value_s";
                if (!o1.toString().isEmpty()) result=var+"='"+o1.toString()+"'";
            }
        }
        if ((last!=null)&&(!last.isEmpty())) result=last + (result.isEmpty()?"":" and (" +result+")");

        return result;
    }

    @PostMapping("/filter")
    public String productFilter(
            @RequestParam Double price1,
            @RequestParam Double price2,
            @RequestParam Integer qnty1,
            @RequestParam Integer qnty2,
            @RequestParam String status,
            HttpServletRequest request,
            Model model
    ) {
        LOGGER.info("filter is called");
        String requestString="";
        String sqlForAll =
                getSQLString("price",'N',price1,price2,
                        getSQLString("quantity",'N',qnty1,qnty2,
                                getSQLString("status",'N',ProdStatus.getStatusByName(status),ProdStatus.getStatusByName(status),""
                                                )));
        String sqlAttr="";
        sqlForAll=(activeCategory==0?"":"category_id="+activeCategory+(!sqlForAll.isEmpty()?" and ":""))+sqlForAll;

        if (activeCategory!=0) {
            Optional<Category> category = categoryRepo.findById(activeCategory);
            //Category category = categoryRepo.getOne(activeCategory);
            for(Attribute a:category.get().getAttributes()) {
                String sqlFilter="";
                String aId=""+a.getId();
                if (a.getType()=='N') {
                    sqlFilter=getSQLString("value_n",'N',request.getParameter("A1_"+aId),request.getParameter("A2_"+aId),"");
                } else if (a.getType()=='S') {
                    sqlFilter=getSQLString("value_s",'S',request.getParameter("A_"+aId),null,"");
                }
                if (!sqlFilter.isEmpty()) {
                    sqlFilter="select distinct product_id from product_attribute where attribute_id="+aId+" and "+sqlFilter;
                    sqlAttr=sqlAttr+(!sqlAttr.isEmpty()?" intersect ":"")+sqlFilter;
                }
            }
            sqlForAll+=(!sqlAttr.isEmpty()?" and id in ("+sqlAttr+")":"");
        }

        requestString="select id, name, price, quantity, status, category_id from product"+(!sqlForAll.isEmpty()?" where "+sqlForAll:"");

        //System.out.println(requestString);

        categories = categoryRepo.findAll();
        LOGGER.info("filter sql="+requestString);
        products = productRepo.findByNativeQuery(requestString);

        fillModel(model);

        return "main";
    }

}