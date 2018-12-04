package com.test.ishop.controller;

import com.test.ishop.config.SessionData;
import com.test.ishop.domain.*;
import com.test.ishop.repos.CartRepo;
import com.test.ishop.repos.ClientRepo;
import com.test.ishop.repos.OrderRepo;
import com.test.ishop.repos.ProductRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
//@Scope("session")
public class CartController {

    private static final Logger LOGGER = Logger.getLogger(CartController.class);

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private SessionData sessionData;

    @ResponseBody
    @PostMapping("/getCartSum")
    public Map<String,String> getCartSum (
            Model model
    ) {
        LOGGER.debug("getCartSum is called");
        Map<String,String> result = new HashMap<>();
        Double sum = sessionData.getCart().getCartSum();
        result.put("sum",sum.toString());
        return result;
    }

    private boolean isCartContainProduct(Product product) {
        boolean result=false;
        if (sessionData.getCart().getProducts() == null)
            sessionData.getCart().setProducts(new HashSet<>());
        for(Product p:sessionData.getCart().getProducts()) {
            if (p.getId().equals(product.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private boolean verifyProduct(
            Long id,
            Map<String,String> map,
            Optional<Product> optionalProduct
            ) {
        boolean result = false;
        String status="";

        Product product;

        if (optionalProduct.isPresent()) {
            product = optionalProduct.get();
            if (product.getQuantity()>0) {
                if (isCartContainProduct(product)) {
                    status = "Товар уже есть в корзине";
                } else
                    result = true;
            } else
                status = "Товара нет в наличии";
        } else
            status ="Продукт #"+id+" отсутствует";

        if (!status.isEmpty()) LOGGER.debug("Ошибка добавления продукта:"+status);
        map.put("status", status);

        return result;
    }

    @ResponseBody
    @PostMapping("/addProduct/{id}")
    public Map<String,String> addProduct(
            @PathVariable("id") Long id,
            Model model
    ) {

        LOGGER.debug("addProduct is called. Id="+id);
        Map<String,String> map = new HashMap<>();
        Double sum;

        Optional<Product> optionalProduct = productRepo.findById(id);

        if (verifyProduct(id,map,optionalProduct)) {
            sessionData.getCart().getProducts().add(optionalProduct.get());
        }

        sum = sessionData.getCart().getCartSum();
        map.put("sum", sum.toString());

        return map;
    }

    @PostMapping("/cart")
    public String showCart(
            Model model
    ) {
        LOGGER.debug("cart is called");
        if (sessionData.getCart().getProducts() == null) sessionData.getCart().setProducts(new HashSet<>());
        model.addAttribute("cart",sessionData.getCart());
        return "cart";
    }

    @PostMapping("/deleteProduct/{pid}")
    public String deleteProduct(
            @PathVariable long pid,
            Model model
    ) {
        LOGGER.debug("deleteProduct is called. Id="+pid);
        Product product;
        Optional<Product> optionalProduct;
        optionalProduct = productRepo.findById(pid);

        if (optionalProduct.isPresent()) {
            product=optionalProduct.get();
            for (Iterator<Product> i = sessionData.getCart().getProducts().iterator(); i.hasNext();) {
                Product p = i.next();
                if (p.getId().equals(product.getId())) {
                    i.remove();
                }
            }
        } else {
            LOGGER.debug("Ошибка удаления: продукт #"+pid+" не найден");
        }

        model.addAttribute("cart",sessionData.getCart());
        return "cart";
    }

    @PostMapping("/clearCart")
    public String clearCart(
            Model model
    ) {
        LOGGER.debug("clearCart is called. Deleting "+sessionData.getCart().getProducts().size()+" products");
        sessionData.getCart().getProducts().clear();
        model.addAttribute("cart",sessionData.getCart());
        return "cart";
    }

    @PostMapping("/order")
    public String fillOrder(
            Model model
    ) {
        LOGGER.debug("order is called.");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Client client = clientRepo.findByUsername(userName);
        if (client!=null) {
            model.addAttribute("fio",client.getFullName());
            model.addAttribute("address",client.getAddress());
            model.addAttribute("email",client.getEmail());
            model.addAttribute("ordersum",sessionData.getCart().getCartSum());
        }
        return "order";
    }

    @PostMapping("/myorders")
    public String myOrders(
            Model model
    ) {
        LOGGER.debug("myorders is called");
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Client client = clientRepo.findByUsername(userName);
        Iterable<Order> orders;

        String requestString="SELECT id, address, delivery_status, email, fio, order_status, summa, cart_id FROM orders "+
                             "WHERE cart_id IN (SELECT id FROM cart WHERE client_id="+client.getId()+")";
        orders = orderRepo.findByNativeQuery(requestString);
        if (orders!=null)
            model.addAttribute("orders",orders);
        return "orderlist";
    }

    private boolean verifyData(Cart cart,String fio,String address,String delivery) {
        String errorMessage = "";
        boolean result = true;

        if ((cart==null)||(cart.getProducts().isEmpty())) {
            errorMessage ="Не возможно оформить заказ. Корзина пуста.";
        } else {
            Integer deliveryStatus = DeliveryStatus.getStatusByName(delivery);
            if ((fio.isEmpty()) || (address.isEmpty()) || (deliveryStatus == null)) {
                errorMessage = "Не возможно оформить заказ. Не заполнены все поля.";
            }
        }
        if (!errorMessage.isEmpty()) {
            LOGGER.debug("order error:" + errorMessage);
            result=false;
        }
        return result;
    }

    private void saveCart(Cart cart) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = clientRepo.findByUsername(userName);

        cart.setClient(client);
        cartRepo.save(cart);
    }
    private void saveOrder(Cart cart, String fio, String address, String email, String delivery) {
        Order order = new Order();
        order.setAddress(address);
        order.setFio(fio);
        order.setEmail(email);
        order.setSumma(cart.getCartSum());
        order.setOrderStatus(OrderStatus.CONFIRMED);
        order.setDeliveryStatus(DeliveryStatus.values()[DeliveryStatus.getStatusByName(delivery)]);
        order.setCart(cart);
        orderRepo.save(order);
        LOGGER.debug("new order created:"+order);

    }


    @PostMapping("/confirm")
    public String confirmOrder(
            @RequestParam String fio,
            @RequestParam String address,
            @RequestParam String email,
            @RequestParam String delivery,
            Model model
    ) {
        LOGGER.debug("confirm is called.");

        if (!verifyData(sessionData.getCart(),fio,address,delivery)) {
            return "order";
        }

        saveCart(sessionData.getCart());
        saveOrder(sessionData.getCart(),fio,address,email,delivery);
        sessionData.setCart(new Cart());

        String message="Заказ оформлен. Ожидайте доставки.";

        return "forward:/alert/"+message;
    }


}
