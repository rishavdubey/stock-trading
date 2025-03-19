package com.example.stocktrading.service;

import com.example.stocktrading.entity.UserDetails;
import com.example.stocktrading.exception.CustomException;
import com.example.stocktrading.response.trade.StockBuyResponse;
import com.example.stocktrading.response.trade.StockSellResponse;
import com.example.stocktrading.entity.Portfolio;
import com.example.stocktrading.entity.Stock;
import com.example.stocktrading.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TradeService {

    @Autowired
    private StockService stockService;
    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;

    private final Map<String, Object> userLocks = new ConcurrentHashMap<>();

    @Transactional
    public StockBuyResponse buyStock(String username, String stockSymbol, int quantity) {
        synchronized (getUserLock(username)) {
            UserDetails user = userService.getUserById(username);
            Stock stock = stockService.getStockBySymbol(stockSymbol);
            BigDecimal totalCost = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
            if (user.getBalance().compareTo(totalCost) < 0) {
                throw new CustomException("Insufficient balance");
            }
            user.setBalance(user.getBalance().subtract(totalCost));
            Portfolio portfolio = portfolioService.getPortfolioByUserIdAndStockId(username, stockSymbol).orElse(new Portfolio());
            portfolio.setUserId(username);
            portfolio.setStockId(stockSymbol);
            portfolio.setBuyPrice(stock.getCurrentPrice());
            portfolio.setQuantity(portfolio.getQuantity() == null ? quantity : portfolio.getQuantity() + quantity);

            // Update buyPriceAndQuantity map
            portfolio.getBuyPriceAndQuantity().merge(stock.getCurrentPrice(), quantity, Integer::sum);

            Transaction transaction = new Transaction();
            transaction.setUserId(username);
            transaction.setStockId(stockSymbol);
            transaction.setQuantity(quantity);
            transaction.setPrice(stock.getCurrentPrice());
            transaction.setType(Transaction.TransactionType.BUY);

            transactionService.addTransaction(transaction);
            userService.updateUser(user);
            portfolioService.updatePortfolio(portfolio);

            Map<String, String> values = Map.of(
                    "firstName", user.getName(),
                    "stockName", stock.getName(),
                    "stockSymbol", stock.getSymbol(),
                    "price", String.valueOf(stock.getCurrentPrice()),
                    "quantity", String.valueOf(quantity),
                    "totalAmount", String.valueOf(totalCost),
                    "date", String.valueOf(new Date())
            );
            emailService.sendSimpleEmail(user.getEmail(), "Stock Purchase Successfully!!", "stockPurchase.txt", values);

            StockBuyResponse stockBuyResponse = new StockBuyResponse();
            stockBuyResponse.setStockName(stock.getName());
            stockBuyResponse.setStockSymbol(stock.getSymbol());
            stockBuyResponse.setQuantity(quantity);
            stockBuyResponse.setPurchasePrice(stock.getCurrentPrice());
            stockBuyResponse.setTotalAmount(totalCost);
            stockBuyResponse.setMessage("Stock purchased successfully");

            return stockBuyResponse;
        }
    }

    @Transactional
    public StockSellResponse sellStock(String username, String stockSymbol, int quantity) {
        synchronized (getUserLock(username)) {
            UserDetails userDetails = userService.getUserById(username);
            Stock stock = stockService.getStockBySymbol(stockSymbol);
            Portfolio portfolio = portfolioService.getPortfolioByUserIdAndStockId(username, stockSymbol).orElseThrow(() -> new CustomException("Stock not owned"));
            if (portfolio.getQuantity() < quantity) {
                throw new CustomException("Not enough stock to sell");
            }
            BigDecimal totalValue = stock.getCurrentPrice().multiply(BigDecimal.valueOf(quantity));
            userDetails.setBalance(userDetails.getBalance().add(totalValue));
            portfolio.setQuantity(portfolio.getQuantity() - quantity);

            // Update buyPriceAndQuantity map
            Map<BigDecimal, Integer> buyPriceAndQuantity = portfolio.getBuyPriceAndQuantity();
            for (Map.Entry<BigDecimal, Integer> entry : buyPriceAndQuantity.entrySet()) {
                BigDecimal buyPrice = entry.getKey();
                int qty = entry.getValue();
                if (qty >= quantity) {
                    buyPriceAndQuantity.put(buyPrice, qty - quantity);
                    if (buyPriceAndQuantity.get(buyPrice) == 0) {
                        buyPriceAndQuantity.remove(buyPrice);
                    }
                    break;
                } else {
                    quantity -= qty;
                    buyPriceAndQuantity.remove(buyPrice);
                }
            }

            if (portfolio.getQuantity() == 0) {
                portfolioService.removePortfolio(portfolio);
            } else {
                portfolioService.updatePortfolio(portfolio);
            }

            Transaction transaction = new Transaction();
            transaction.setUserId(username);
            transaction.setStockId(stockSymbol);
            transaction.setQuantity(quantity);
            transaction.setPrice(stock.getCurrentPrice());
            transaction.setType(Transaction.TransactionType.SELL);

            transactionService.addTransaction(transaction);
            userService.updateUser(userDetails);

            Map<String, String> values = Map.of(
                    "firstName", userDetails.getName(),
                    "stockName", stock.getName(),
                    "stockSymbol", stock.getSymbol(),
                    "price", String.valueOf(stock.getCurrentPrice()),
                    "quantity", String.valueOf(quantity),
                    "totalAmount", String.valueOf(totalValue),
                    "date", String.valueOf(new Date())
            );
            emailService.sendSimpleEmail(userDetails.getEmail(), "Stock Sell Confirmation", "stockSell.txt", values);

            StockSellResponse stockSellResponse = new StockSellResponse();
            stockSellResponse.setStockName(stock.getName());
            stockSellResponse.setStockSymbol(stock.getSymbol());
            stockSellResponse.setQuantity(quantity);
            stockSellResponse.setSalePrice(stock.getCurrentPrice());
            stockSellResponse.setTotalAmount(totalValue);
            stockSellResponse.setMessage("Stock sold successfully");

            return stockSellResponse;
        }
    }

    private Object getUserLock(String username) {
        return userLocks.computeIfAbsent(username, k -> new Object());
    }
}
