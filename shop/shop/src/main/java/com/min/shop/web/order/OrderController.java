package com.min.shop.web.order;

import com.min.shop.entity.member.Member;
import com.min.shop.entity.item.Book;
import com.min.shop.service.BookService;
import com.min.shop.service.MemberService;
import com.min.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final BookService bookService;

    @GetMapping(value = "/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Book> items = bookService.findAll();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping(value = "/order")
    public String order(@Validated @ModelAttribute("dto") OrderRequestDto form,
                        BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order/orderFail";
        }

        Book findBook = bookService.findById(form.getItemId());
        if (findBook.getStockQuantity() - form.getCount() < 0) {
            bindingResult.reject("orderFail", "수량을 확인하십시오");
            return "order/orderFail";
        }

        orderService.order(form.getMemberId(), form.getItemId(), form.getCount());


        model.addAttribute("book", findBook);
        return "order/orderSuccess";
    }

//    @GetMapping(value = "/orders")
//    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
//        List<Order> orders = orderService.findOrders(orderSearch);
//        model.addAttribute("orders", orders);
//
//        return "order/orderList";
//    }

    @PostMapping(value = "/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {

        orderService.cancelOrder(orderId);

        return "redirect:/orders";
    }
}