package ua.opnu;

import ua.opnu.util.Customer;
import ua.opnu.util.DataProvider; // Передбачається, що цей клас у вас є
import ua.opnu.util.Order;
import ua.opnu.util.Product;

import java.util.*;
import java.util.stream.Collectors;

public class HardTasks {

    private final List<Customer> customers = DataProvider.customers;
    private final List<Order> orders = DataProvider.orders;
    private final List<Product> products = DataProvider.products;

    public static void main(String[] args) {
        HardTasks tasks = new HardTasks();

        // Завдання 1
        System.out.println("Task 1:");
        Objects.requireNonNull(tasks.getBooksWithPrice(),"Method getBooksWithPrice() returns null").forEach(System.out::println);

        // Завдання 2
        System.out.println("\nTask 2:");
        Objects.requireNonNull(tasks.getOrdersWithBabyProducts(),"Method getOrdersWithBabyProducts() returns null").forEach(System.out::println);

        // Завдання 3
        System.out.println("\nTask 3:");
        Objects.requireNonNull(tasks.applyDiscountToToys(),"Method applyDiscountToToys() returns null").forEach(System.out::println);

        // Завдання 4
        System.out.println("\nTask 4:");
        tasks.getCheapestBook().ifPresent(System.out::println);

        // Завдання 5
        System.out.println("\nTask 5:");
        Objects.requireNonNull(tasks.getRecentOrders(),"Method getRecentOrders() returns null").forEach(System.out::println);

        // Завдання 6
        System.out.println("\nTask 6:");
        DoubleSummaryStatistics statistics = Objects.requireNonNull(tasks.getBooksStats(), "Method getBooksStats() returns null");
        System.out.printf("count = %1$d, average = %2$f, max = %3$f, min = %4$f, sum = %5$f%n", statistics.getCount(), statistics.getAverage(), statistics.getMax(), statistics.getMin(), statistics.getSum());

        // Завдання 7
        System.out.println("\nTask 7:");
        Objects.requireNonNull(tasks.getOrdersProductsMap(),"Method getOrdersProductsMap() returns null").forEach((id, size) -> System.out.printf("%1$d : %2$d\n", id, size));

        // Завдання 8
        System.out.println("\nTask 8:");
        Objects.requireNonNull(tasks.getProductsByCategory(), "Method getProductsByCategory() returns null").forEach((name, list) -> System.out.printf("%1$s : %2$s\n", name, list.toString()));
    }

    public List<Product> getBooksWithPrice() {
        // Товари категорії Books з ціною > 100
        return products.stream()
                .filter(product -> "Books".equals(product.getCategory()))
                .filter(product -> product.getPrice() > 100)
                .collect(Collectors.toList());
    }

    public List<Order> getOrdersWithBabyProducts() {
        // Замовлення, що містять хоча б один товар категорії Baby
        return orders.stream()
                .filter(order -> order.getProducts().stream()
                        .anyMatch(product -> "Baby".equals(product.getCategory())))
                .collect(Collectors.toList());
    }

    public List<Product> applyDiscountToToys() {
        // Знайти Toys, застосувати знижку 50%, повернути список
        return products.stream()
                .filter(product -> "Toys".equals(product.getCategory()))
                .map(product -> {
                    product.setPrice(product.getPrice() * 0.5);
                    return product;
                })
                .collect(Collectors.toList());
    }

    public Optional<Product> getCheapestBook() {
        // Знайти найдешевшу книгу
        return products.stream()
                .filter(product -> "Books".equals(product.getCategory()))
                .min(Comparator.comparing(Product::getPrice));
    }

    public List<Order> getRecentOrders() {
        // 3 останні замовлення за датою
        return orders.stream()
                .sorted(Comparator.comparing(Order::getOrderDate).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public DoubleSummaryStatistics getBooksStats() {
        // Статистика цін по книгам
        return products.stream()
                .filter(product -> "Books".equals(product.getCategory()))
                .mapToDouble(Product::getPrice)
                .summaryStatistics();
    }

    public Map<Integer, Integer> getOrdersProductsMap() {
        // Map: ID замовлення -> Кількість товарів
        return orders.stream()
                .collect(Collectors.toMap(
                        Order::getId,
                        order -> order.getProducts().size()
                ));
    }

    public Map<String, List<Integer>> getProductsByCategory() {
        // Map: Категорія -> Список ID товарів (List<Integer>)
        return products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory,
                        Collectors.mapping(Product::getId, Collectors.toList())
                ));
    }
}