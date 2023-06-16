package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    // 주문 form으로 이동
    // 주문페이지로 get(조회)할 때 고객정보와 상품정보를 가져와서 model객체에 담아서 뷰로 넘겨줌
    @GetMapping(value = "/order")
    public String createForm(Model model) {
        //모든 데이터를 받아오기
        List<Member> members =memberService.findMembers();
        List <Item> items = itemService.findItems();

        model.addAttribute("members",members);
        model.addAttribute("items", items);
        return "order/orderForm";
    }

    // 주문실행
    // submit버튼 누르면 Param정보를 받아서 주문서비스에 주문을 요청하고 redirect
    @PostMapping(value = "/order")
    //RequestParam: 내부 로직이 아니라 form submit 요청으로 같이 전달되는 파라미터
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {
        orderService.order(memberId, itemId,count);
        return "redirect:/orders";
    }

    //주문 검색
    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);

        return "order/orderList"; //orders로 들어오면 orderList뷰로 넘어간다

    }
    //주문취소
    @PostMapping(value = "/orders/{orderId}/cancel")
    //@PathVariable는 url에 변수로 넘기는 값을 지정해줌
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }

}
