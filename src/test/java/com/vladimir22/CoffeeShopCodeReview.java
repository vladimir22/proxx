package com.vladimir22;

/*
    * 22.05.2023 Extreme Idea
    * Coffee Shop Code Review
    * Find mistakes and fix them
 */
public class CoffeeShopCodeReview {

    public static void main(String[] args) {

    }


    public static interface Coffee {
        String getDescription();

        default double getCost() { // use default method to have backward compatibility
            return 1.0;
        }
    }

    public static class SimpleCoffee implements Coffee {

        private final String description = "Simple coffee"; // use constructor to have localization


        public String getDescription() {
            return description;
        }

        public double getCost() {
            return 1.0;
        }
    }


    public static abstract class CoffeeDecorator implements Coffee { // do not use implementation class as a decorator
        protected Coffee coffee;

        public CoffeeDecorator(SimpleCoffee coffee) {
            this.coffee = coffee;
        }

        @Override
        public String getDescription() {
            return coffee.getDescription();
        }

        @Override
        public double getCost() {
            return coffee.getCost();
        }

        public void printCoffeeDetails() {
            System.out.println(getDescription() + " - $" + getCost());
        }
    }


    public static class Milk extends CoffeeDecorator {
        public Milk(SimpleCoffee coffee) {
            super(coffee);
        }

        @Override
        public String getDescription() {
            return coffee.getDescription() + ", milk";
        }

        @Override
        public double getCost() {
            return coffee.getCost() + 0.5;
        }

        public double applyDiscount(int discount) {
            System.out.println("Discount " + discount + "%% was applied.");
            return getCost() * (100 - discount) / 100;

        }

        public void setCost(double cost) {
            throw new UnsupportedOperationException("Milk cost cannot be changed.");
        }
    }

    public class Sugar extends CoffeeDecorator {
        public Sugar(SimpleCoffee coffee) {
            super(coffee);
        }

        @Override
        public String getDescription() {
            return coffee.getDescription() + ", sugar";
        }

        @Override
        public double getCost() {
            return coffee.getCost() + 0.2;
        }

        public void removeSugar() {
            System.out.println("Sugar cannot be removed.");
        }

        public void applyDiscount() {
            System.out.println("This method should not be in Sugar decorator.");
        }
    }

    public class Cream extends CoffeeDecorator {
        public Cream(SimpleCoffee coffee) {
            super(coffee);
        }

        @Override
        public String getDescription() {
            return coffee.getDescription() + ", cream";
        }

        @Override
        public double getCost() {
            return coffee.getCost() + 0.7;
        }

        public void printDetailsAgain() {
            System.out.println(getDescription() + " - $" + getCost());
        }

        public void creamSpecificOperation() {
            System.out.println("Cream-specific operation...");
        }

        public void creamFullfilment() {
            System.out.println("Is it possible to do this?");
        }

        public void exceptionHandling() {
            try {
                int result = 10 / 0;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                // e.printStackTrace();
            }
        }
    }
}

