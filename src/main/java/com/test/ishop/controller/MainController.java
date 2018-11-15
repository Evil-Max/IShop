package com.test.ishop.controller;

import com.test.ishop.config.FakeClass;
import com.test.ishop.domain.Attribute;
import com.test.ishop.domain.Category;
import com.test.ishop.domain.ProdStatus;
import com.test.ishop.domain.Product;
import com.test.ishop.repos.CategoryRepo;
import com.test.ishop.repos.ProductRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;


@Controller
public class MainController {

    private final static Logger LOGGER = Logger.getLogger(MainController.class);

    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private FakeClass fakeClass;

    private void fillModel(Model model,Iterable<Product> products,Long activeCategory,String alert) {
        model.addAttribute("activeCategory",activeCategory);
        model.addAttribute("categories",categoryRepo.findAll());
        model.addAttribute("products",products);
        if (!alert.isEmpty())
        model.addAttribute("message",alert);
    }

    @GetMapping("/")
    public String main(
            Model model
    ) {
        LOGGER.info("main is called");
        LOGGER.info(fakeClass.toString());

        fillModel(model,productRepo.findAll(),0L,"");
        return "main";
    }

    @GetMapping("/category/{cid}")
    public String categoryFilter(
            @PathVariable long cid,
            Model model
    ) {
        LOGGER.info("category/"+cid+" is called");
        Category category = categoryRepo.getOne(cid);
        Iterable<Product> products;

        if (category!=null)
            products = productRepo.findByCategoryId(cid);
        else {
            LOGGER.debug("Категория не найдена");
            products = productRepo.findAll();
            cid=0L;
        }

        fillModel(model,products,cid,"");
        return "main";
    }

    @PostMapping("/alert/{message}")
    public String alertShow(
            @PathVariable("message") String message,
            Model model
    ) {
        LOGGER.info("alert is called. message="+message);

        fillModel(model,productRepo.findAll(),0L,message);
        return "main";
    }

    @PostMapping("/filter")
    public String productFilter(
            @RequestParam Double price1,
            @RequestParam Double price2,
            @RequestParam Integer qnty1,
            @RequestParam Integer qnty2,
            @RequestParam String status,
            @RequestParam Long activeCategory,
            HttpServletRequest request,
            Model model
    ) {
        LOGGER.info("filter is called");
        String requestString=createSqlRequestString(price1,price2,qnty1,qnty2,status,activeCategory,request);

        LOGGER.info("filter sql="+requestString);
        Iterable<Product> products = productRepo.findByNativeQuery(requestString);

        fillModel(model,products,activeCategory,"");

        return "main";
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

    private String createSqlRequestString(
            Double price1,
            Double price2,
            Integer qnty1,
            Integer qnty2,
            String status,
            Long activeCategory,
            HttpServletRequest request
    ) {
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
            if (category!=null) {
                for (Attribute a : category.get().getAttributes()) {
                    String sqlFilter = "";
                    String aId = "" + a.getId();
                    if (a.getType() == 'N') {
                        sqlFilter = getSQLString("value_n", 'N', request.getParameter("A1_" + aId), request.getParameter("A2_" + aId), "");
                    } else if (a.getType() == 'S') {
                        sqlFilter = getSQLString("value_s", 'S', request.getParameter("A_" + aId), null, "");
                    }
                    if (!sqlFilter.isEmpty()) {
                        sqlFilter = "select distinct product_id from product_attribute where attribute_id=" + aId + " and " + sqlFilter;
                        sqlAttr = sqlAttr + (!sqlAttr.isEmpty() ? " intersect " : "") + sqlFilter;
                    }
                }
                sqlForAll += (!sqlAttr.isEmpty() ? " and id in (" + sqlAttr + ")" : "");
            }
        }
        requestString="select id, name, price, quantity, status, category_id from product"+(!sqlForAll.isEmpty()?" where "+sqlForAll:"");
        return requestString;
    }

}
