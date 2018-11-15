package com.test.ishop.controller;

import com.test.ishop.domain.*;
import com.test.ishop.repos.CartRepo;
import com.test.ishop.repos.ClientRepo;
import com.test.ishop.repos.OrderRepo;
import com.test.ishop.repos.ProductRepo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

@Controller
@Scope("session")
public class CartController {

    private final static Logger LOGGER = Logger.getLogger(CartController.class);

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private Cart cart;

    @ResponseBody
    @PostMapping("/getCartSum")
    public Map<String,String> getCartSum (
            Model model
    ) {
        LOGGER.debug("getCartSum is called");
        Map<String,String> result = new HashMap<String, String>();
        Double sum = cart.getCartSum();
        result.put("sum",sum.toString());
        return result;
    }

    @ResponseBody
    @PostMapping("/addProduct/{id}")
    public Map<String,String> addProduct(
            @PathVariable("id") Long id,
            Model model
    ) {

        Product product = productRepo.findById(id).get();
        LOGGER.debug("addProduct is called. Id="+id);
        Map<String,String> result = new HashMap<String, String>();
        Double sum=0d;
        String status="";

        if (product!=null) {
            if (product.getQuantity()>0) {
                if (cart.getProducts() == null) cart.setProducts(new HashSet<Product>());
                for(Product p:cart.getProducts()) {
                    if (p.getId().equals(product.getId())) {
                        status = "Товар уже есть в корзине";
                        break;
                    }
                }
                if (status.isEmpty()) cart.getProducts().add(product);
            } else status = "Товара нет в наличии";
        } else status ="Ошибка при добавлении товара в корзину";

        if (!status.isEmpty()) LOGGER.debug("Product add error="+status);
        
        sum = cart.getCartSum();
        result.put("sum", sum.toString());
        result.put("status", status);
        LOGGER.debug("Sum="+sum);
        return result;
    }

    @PostMapping("/cart")
    public String showCart(
            Model model
    ) {
        LOGGER.debug("cart is called");
        if (cart.getProducts() == null) cart.setProducts(new HashSet<Product>());
        model.addAttribute("cart",cart);
        return "cart";
    }

    @PostMapping("/deleteProduct/{pid}")
    public String deleteProduct(
            @PathVariable long pid,
            Model model
    ) {
        LOGGER.debug("deleteProduct is called. Id="+pid);
        Product product = productRepo.findById(pid).get();
        for (Iterator<Product> i = cart.getProducts().iterator(); i.hasNext();) {
            Product p = i.next();
            if (p.getId().equals(product.getId())) {
                i.remove();
            }
        }
        model.addAttribute("cart",cart);
        return "cart";
    }

    @PostMapping("/clearCart")
    public String clearCart(
            Model model
    ) {
        LOGGER.debug("clearCart is called. Deleting "+cart.getProducts().size()+" products");
        cart.getProducts().clear();
        model.addAttribute("cart",cart);
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
            model.addAttribute("ordersum",cart.getCartSum());
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

        if (!verifyData(cart,fio,address,delivery)) {
            return "order";
        }

        saveCart(cart);
        saveOrder(cart,fio,address,email,delivery);
        cart = new Cart();

        String message="Заказ оформлен. Ожидайте доставки.";

        return "forward:/alert/"+message;
    }


}
