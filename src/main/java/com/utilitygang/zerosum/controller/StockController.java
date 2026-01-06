package com.utilitygang.zerosum.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import jakarta.servlet.http.HttpServletRequest;

import com.utilitygang.zerosum.model.*;
import com.utilitygang.zerosum.repository.*;
import static com.utilitygang.zerosum.data.PriceData.getPriceForStockAmount;

@Controller
public class StockController {
    @Autowired
    UserRepository userRepo;
    @Autowired
    StockRepository stockRepo;
    @Autowired
    CompanyRepository companyRepo;

    @GetMapping("/stocks/{company_id}/live")
    public String stockLiveUpdate(@PathVariable String company_id, @AuthenticationPrincipal DefaultOidcUser principal) {
        return "hi";

    }

    @GetMapping("/stocks/{company_id}")
    public String stocksIdPage(@PathVariable String company_id, RedirectAttributes attr,
            @AuthenticationPrincipal DefaultOidcUser principal, Model model) {
        User owner = userRepo.findUserByUsername((String) principal.getAttributes().get("email")).get();
        Company company = companyRepo.findById(company_id).orElse(null);
        if (company == null) {
            attr.addFlashAttribute("msg", "This stock doesn't exist/isn't currently tradeable on ZeroSum.");
            return "redirect:/stocks";
        }
        Stock stock = stockRepo.findByOwnerAndCompany(owner, company).orElse(new Stock(0.0, null, company));
        model.addAttribute("company", company);
        model.addAttribute("maxValue", stock.getAmount());
        return "stocks/idIndex";
    }

    @PostMapping("/stocks/{company_id}/buy")
    public String stocksBuy(@PathVariable String company_id, @RequestParam(required = true) Double amount,
            RedirectAttributes attr, @AuthenticationPrincipal DefaultOidcUser principal, HttpServletRequest req) {
        // get the stock value first thing
        BigDecimal stock_value = getPriceForStockAmount(company_id, amount);
        User owner = userRepo.findUserByUsername((String) principal.getAttributes().get("email")).get();
        // find out if the user can actually afford the stock or not
        if (owner.getCash().subtract(stock_value).compareTo(BigDecimal.ZERO) < 0) {
            attr.addFlashAttribute("msg", "You do not have enough money.");
        } else {
            Stock stock;
            if (stockRepo.findByOwnerAndCompanySymbol(owner, company_id).isEmpty())
                stock = new Stock(amount, owner, companyRepo.findById(company_id).get());
            else
                stock = stockRepo.findByOwnerAndCompanySymbol(owner, company_id).get();
            stockRepo.save(stock);
            owner.setCash(owner.getCash().subtract(stock_value));
        }
        return "redirect:" + req.getHeader("Referer");
    }

    @PostMapping("/stocks/{company_id}/sell")
    public String stocksSell(@PathVariable String company_id, @RequestParam(required = true) Double amount,
            RedirectAttributes attr, @AuthenticationPrincipal DefaultOidcUser principal, HttpServletRequest req) {
        // get the stock value first thing
        BigDecimal stock_value = getPriceForStockAmount(company_id, amount);
        User owner = userRepo.findUserByUsername((String) principal.getAttributes().get("email")).get();
        // find out if the user actually has those stocks or not
        if (stockRepo.findByOwnerAndCompanySymbol(owner, company_id).isEmpty()) {
            attr.addFlashAttribute("msg", "You do not own any of this stock.");
        } else {
            Stock stock = stockRepo.findByOwnerAndCompanySymbol(owner, company_id).get();
            // then check if the user has enough stocks or not
            if (stock.getAmount() < amount) {
                attr.addFlashAttribute("msg", "You do not hold this much " + company_id + " stock.");
            } else {
                owner.setCash(stock_value.add(owner.getCash()));
                stock.setAmount(stock.getAmount() - amount);
                stockRepo.save(stock);
            }
        }
        return "redirect:" + req.getHeader("Referer");
    }

}
